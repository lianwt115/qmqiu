package com.guoxiaoxing.phoenix.picker.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import com.guoxiaoxing.phoenix.R
import com.guoxiaoxing.phoenix.core.common.PhoenixConstant
import com.guoxiaoxing.phoenix.picker.listener.*
import com.guoxiaoxing.phoenix.picker.model.*
import com.guoxiaoxing.phoenix.picker.ui.BaseFragment
import com.guoxiaoxing.phoenix.picker.util.*
import com.guoxiaoxing.phoenix.picker.widget.dialog.PhoenixLoadingDialog
import com.guoxiaoxing.phoenix.picker.widget.editor.ColorSeekBar
import com.guoxiaoxing.phoenix.picker.widget.editor.DragToDeleteView
import com.guoxiaoxing.phoenix.picker.widget.editor.EditDelegate
import com.guoxiaoxing.phoenix.picker.widget.editor.blur.BlurDetailView
import com.guoxiaoxing.phoenix.picker.widget.editor.blur.BlurMode
import com.guoxiaoxing.phoenix.picker.widget.editor.blur.BlurView
import com.guoxiaoxing.phoenix.picker.widget.editor.crop.CropDetailView
import com.guoxiaoxing.phoenix.picker.widget.editor.crop.CropHelper
import com.guoxiaoxing.phoenix.picker.widget.editor.hierarchy.BaseHierarchyView
import com.guoxiaoxing.phoenix.picker.widget.editor.hierarchy.BasePastingHierarchyView
import com.guoxiaoxing.phoenix.picker.widget.editor.hierarchy.HierarchyComposite
import com.guoxiaoxing.phoenix.picker.widget.editor.paint.PaintView
import com.guoxiaoxing.phoenix.picker.widget.editor.paint.PaintlDetailsView
import com.guoxiaoxing.phoenix.picker.widget.editor.stick.StickDetailsView
import com.guoxiaoxing.phoenix.picker.widget.editor.stick.StickView
import com.guoxiaoxing.phoenix.picker.widget.editor.text.TextPastingView
import kotlinx.android.synthetic.main.action_bar_editor.*
import kotlinx.android.synthetic.main.fragment_picture_edit.*
import java.io.File
import java.util.*

/**
 * The picture edit activity
 * <p>
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingse@163.com.
 *
 * @author guoxiaoxing
 */
class PictureEditFragment : BaseFragment(), LayerViewProvider, OperationProcessor, OperationListener,
        OperationDetailListener, OnRevokeListener {

    //data
    private lateinit var mEditDelegate: EditDelegate
    private lateinit var mActionBarAnimUtils: ActionBarAnimUtils
    private lateinit var mDragToDeleteView: DragToDeleteView
    private lateinit var mCropHelper: CropHelper

    private lateinit var mEditorId: String
    private var mEditorWidth: Int = 0
    private var mEditorHeight: Int = 0
    private var mOriginOrientation: Int = 0
    private lateinit var mOriginPath: String
    private lateinit var mOriginBitmap: Bitmap
    private lateinit var mSavePath: String

    private val mTextInputResultCode = 301
    private var mStickDetailsView: StickDetailsView? = null
    private var mStickerDetailsShowing = false

    //operation
    private var mSelectedOperation: Operation? = null
    private val mOperationListeners = ArrayList<OperationListener>()
    private val mOperationDetailListeners = ArrayList<OperationDetailListener>()
    private val mOnRevokeListeners = ArrayList<OnRevokeListener>()

    companion object {
        fun newInstance(): PictureEditFragment {
            return PictureEditFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (Build.VERSION.SDK_INT >= 21) {
            this.activity!!.window.statusBarColor = MatrixUtils.getResourceColor(activity!!, R.color.black);
        }
        //transparent necessary
        activity!!.window.setBackgroundDrawableResource(R.color.transparent)
        //flag necessary
        if (Build.VERSION.SDK_INT >= 19) {
            activity!!.window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        val mBackgroundColor = ColorDrawable(Color.BLACK)
        val view = View.inflate(activity, R.layout.fragment_picture_edit, null)
        if (Build.VERSION.SDK_INT >= 16) {
            view.background = mBackgroundColor
        } else {
            view.setBackgroundDrawable(mBackgroundColor)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupData()
    }

    private fun setupView() {
        mEditDelegate = EditDelegate(photoView, layerEditorParent)
        this.mActionBarAnimUtils = ActionBarAnimUtils(layerActionView, editorBar, rlFunc, activity!!)
        mCropHelper = CropHelper(layerCropView, CropDetailView(layoutCropDetails), this)

        val operationList = listOf(Operation.PaintOperation, Operation.StickOperation, Operation.TextOperation,
                Operation.BlurOperation, Operation.CropOperation)

        for (index in operationList.indices) {
            val mode = operationList[index]
            if (mode.getIcon() <= 0) {
                continue
            }
            val item = LayoutInflater.from(activity).inflate(R.layout.item_operation, llOperation, false)
            val ivOperation = item.findViewById(R.id.ivOperation) as ImageView
            ivOperation.setImageResource(mode.getIcon())
            item.tag = mode
            llOperation.addView(item)
            item.setOnClickListener {
                onOperationClick(mode, index, item)
            }
        }

        mDragToDeleteView = DragToDeleteView(layoutDragDelete)

        getView<TextPastingView>(Operation.TextOperation)?.let {
            setUpPastingView(it)
            it.onLayerViewDoubleClick = { _, sharableData ->
                go2InputView(sharableData as InputTextModel)
            }
        }
        getView<StickView>(Operation.StickOperation)?.let {
            setUpPastingView(it)
        }

        mOperationListeners.add(this)
        mOperationDetailListeners.add(this)
        mOnRevokeListeners.add(this)

        tvCancel.setOnClickListener {
            cancel()
        }

        tvComplete.setOnClickListener {
            imageCompose()
        }

        mDragToDeleteView.onLayoutRectChange = { _, rect ->
            getView<TextPastingView>(Operation.TextOperation)?.dragViewRect = rect
            getView<StickView>(Operation.StickOperation)?.dragViewRect = rect
        }

    }

    /**
     * restore data for redraw all cache ,and undo or other func
     * it's important to get right editor path and editorId...
     */
    private fun setupData() {

        mOriginPath = arguments!!.getString(PhoenixConstant.KEY_FILE_PATH)
        mOriginOrientation = arguments!!.getInt(PhoenixConstant.KEY_ORIENTATION)
        val byteData = arguments!!.getByteArray(PhoenixConstant.KEY_FILE_BYTE)
        if (byteData != null) {
            mOriginBitmap = PictureUtils.roatePicture(mOriginOrientation, byteData, activity!!)
        }

        mSavePath = savePath + System.currentTimeMillis() + ".png"

        val sourceBitmap = PictureUtils.getImageBitmap(mOriginPath)

        photoView.setImageBitmap(sourceBitmap)
        blurView.setBitmap(sourceBitmap)

        val cropState = mCropHelper.getSavedCropState()
        photoView.addOnLayoutChangeListener(LayerImageOnLayoutChangeListener(cropState))
    }

    inner class LayerImageOnLayoutChangeListener(val state: CropSaveState?) : View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            val matrix = state?.originalMatrix ?: photoView.getBaseLayoutMatrix()
            state?.let {
                mCropHelper.resetEditorSupportMatrix(it)
            }
            blurView.initializeMatrix = matrix
            photoView.removeOnLayoutChangeListener(this)
        }
    }

    override fun findLayerByEditorMode(operation: Operation): View? {
        when (operation) {
            Operation.PaintOperation -> return paintView
            Operation.StickOperation -> return stickView
            Operation.TextOperation -> return textPastingView
            Operation.BlurOperation -> return blurView
            Operation.CropOperation -> return layerCropView
            else -> return null
        }
    }

    override fun getActivityContext(): Context {
        return activity!!
    }

    override fun getEditorSizeInfo(): Pair<Int, Int> {
        return Pair(mEditorWidth, mEditorHeight)
    }

    override fun getScreenSizeInfo(): Pair<Int, Int> {
        return Pair(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun getFuncAndActionBarAnimHelper(): ActionBarAnimUtils = this.mActionBarAnimUtils

    override fun getCropHelper(): CropHelper = mCropHelper

    override fun getRootEditorDelegate(): EditDelegate = mEditDelegate

    override fun getLayerCompositeView(): HierarchyComposite = layerComposite

    override fun getSetupEditorId() = mEditorId

    override fun getResultEditorId() = mOriginPath + mSavePath

    private var imageComposeTask: ImageComposeTask? = null

    private fun imageCompose() {
        val path = mSavePath
        val parentFile = File(path).parentFile
        parentFile?.mkdirs()
        imageComposeTask?.cancel(true)
        imageComposeTask = ImageComposeTask(this)
        imageComposeTask?.execute(path)
    }

    /**
     * image compose cancel
     */
    private fun cancel() {
        supportRecycle()
        activity!!.supportFragmentManager.popBackStack()
    }

    /**
     * image compose result success or fail
     */
    private fun finish(editStatus: Boolean) {
        supportRecycle()
        val intent = Intent()
        intent.putExtra(PhoenixConstant.KEY_FILE_PATH, mSavePath)
        targetFragment!!.onActivityResult(PhoenixConstant.REQUEST_CODE_PICTURE_EDIT, PhoenixConstant.REQUEST_CODE_PICTURE_EDIT, intent)
        activity!!.supportFragmentManager.popBackStackImmediate()
    }

    private fun supportRecycle() {
        recycleBitmap(mEditDelegate.getDisplayBitmap())
        mCropHelper.getSavedCropState()?.reset()
    }

    private fun onOperationClick(operation: Operation, position: Int, clickView: View) {
        if (mSelectedOperation == operation) {
            operation.onOperation(false, this)
            MatrixUtils.changeSelectedStatus(llOperation, -1)
            callback2Listeners(mOperationListeners) {
                it.onFuncModeUnselected(operation)
            }
            mSelectedOperation = null
        } else {
            operation.onOperation(true, this)
            if (operation.canPersistMode()) {
                MatrixUtils.changeSelectedStatus(llOperation, position)
                mSelectedOperation = operation
            }
            callback2Listeners(mOperationListeners) {
                it.onOperationSelected(operation)
            }
        }
    }


    /**
     * operation - paint
     */
    override fun operatePaint(selected: Boolean) {
        if (selected) {
            val paintlDetail = PaintlDetailsView(activity!!)
            paintlDetail.onColorChangeListener = object : ColorSeekBar.OnColorChangeListener {
                override fun onColorChangeListener(colorBarPosition: Int, alphaBarPosition: Int, color: Int) {
                    callback2Listeners(mOperationDetailListeners) {
                        it.onReceiveDetails(Operation.PaintOperation, PaintDetail(color))
                    }
                }
            }
            paintlDetail.onRevokeListener = object : OnRevokeListener {
                override fun revoke(operation: Operation) {
                    callback2Listeners(mOnRevokeListeners) {
                        it.revoke(Operation.PaintOperation)
                    }
                }
            }
            showOrHideDetailsView(Operation.PaintOperation, paintlDetail)
        }
        showOrHideDetails(selected)
    }

    /**
     * operation - paint
     */
    override fun operateStick(selected: Boolean) {

    }

    /**
     * operation - paint
     */
    override fun operateText(selected: Boolean) {

    }

    /**
     * operation - paint
     */
    override fun operateBlur(selected: Boolean) {
        if (selected) {
            val listener = object : BlurDetailView.OnMosaicChangeListener {
                override fun onChange(blurMode: BlurMode) {
                    callback2Listeners(mOperationDetailListeners) {
                        it.onReceiveDetails(Operation.BlurOperation, BlurDetal(blurMode))
                    }
                }
            }
            val blurDetail = BlurDetailView(activity!!, listener)
            blurDetail.onRevokeListener = object : OnRevokeListener {
                override fun revoke(operation: Operation) {
                    callback2Listeners(mOnRevokeListeners) {
                        it.revoke(Operation.BlurOperation)
                    }
                }
            }
            showOrHideDetailsView(Operation.BlurOperation, blurDetail)
        }
        showOrHideDetails(selected)
    }

    /**
     * operation - paint
     */
    override fun operateCrop(selected: Boolean) {

    }

    private fun <T> callback2Listeners(listeners: List<T>, callback: (T) -> Unit) {
        listeners.forEach {
            callback(it)
        }
    }

    private fun showOrHideDetails(show: Boolean) {
        flOperationDetail.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showOrHideDetailsView(operation: Operation, view: View) {
        val count = flOperationDetail.childCount
        var toRemoveView: View? = null
        var handled = false
        if (count > 0) {
            val topView = flOperationDetail.getChildAt(count - 1)
            val tag = topView.tag
            if (tag != operation) {
                toRemoveView = topView
            } else {
                handled = true
            }
        }
        if (!handled) {
            flOperationDetail.addView(view)
            toRemoveView?.let {
                flOperationDetail.removeView(toRemoveView)
            }
        }
    }

    private inline fun <reified T : View> getView(operation: Operation) = findLayerByEditorMode(operation) as? T

    private fun setUpPastingView(layer: BasePastingHierarchyView<*>) {
        layer.showOrHideDragCallback = {
            showOrHideDrag2Delete(it)
        }
        layer.setOrNotDragCallback = {
            mDragToDeleteView.setDrag2DeleteText(it)
        }
    }


    private fun setScrawlDetails(details: PaintDetail) {
        getView<PaintView>(Operation.PaintOperation)?.setPaintColor(details.color)
    }

    private fun setMosaicDetails(details: BlurDetal) {
        getView<BlurView>(Operation.BlurOperation)?.setMosaicMode(details.blurMode, null)
    }


    private fun showOrHideDrag2Delete(show: Boolean) {
        this.mActionBarAnimUtils.showOrHideFuncAndBarView(!show)
        mDragToDeleteView.showOrHide(show)
    }

    private fun go2InputView(prepareModel: InputTextModel?) {
        this.mActionBarAnimUtils.showOrHideFuncAndBarView(false)
        val intent = TextInputActivity.intent(activity!!, prepareModel)
        startActivityForResult(intent, mTextInputResultCode)
        activity!!.overridePendingTransition(R.anim.animation_bottom_to_top, 0)
    }

    private fun resultFromInputView(resultCode: Int, data: Intent?) {
        val result = data?.getSerializableExtra(resultCode.toString()) as? InputTextModel
        logD1("resultFromInputView is $result")
        result?.let {
            getView<TextPastingView>(Operation.TextOperation)?.onTextPastingChanged(it)
        }
        this.mActionBarAnimUtils.showOrHideFuncAndBarView(true)
    }

    private fun enableOrDisableEditorMode(operation: Operation, enable: Boolean) {
        val view = findLayerByEditorMode(operation)
        if (view is BaseHierarchyView<*>) {
            view.isLayerInEditMode = enable
        }
    }

    private fun go2StickerPanel() {
        this.mActionBarAnimUtils.showOrHideFuncAndBarView(false)
        mStickDetailsView ?: let {
            mStickDetailsView = StickDetailsView(activity!!)
            mStickDetailsView!!.onStickerClickListener = object : StickDetailsView.OnStickerClickResult {
                override fun onResult(stickModel: InputStickModel) {
                    getView<StickView>(Operation.StickOperation)?.onStickerPastingChanged(stickModel)
                    closeStickerPanel()
                }
            }
            this.mActionBarAnimUtils.addFunBarAnimateListener(object : ActionBarAnimUtils.OnFunBarAnimationListener {
                override fun onFunBarAnimate(show: Boolean) {
                    if (show && mStickerDetailsShowing) {
                        hideStickerPanel()
                    }
                }
            })
        }
        val layoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
        ((activity as Activity).window.decorView as ViewGroup).addView(mStickDetailsView!!, layoutParam)
        mStickerDetailsShowing = true
    }

    private fun closeStickerPanel() {
        this.mActionBarAnimUtils.showOrHideFuncAndBarView(true)
        hideStickerPanel()
    }

    private fun hideStickerPanel() {
        mStickDetailsView?.let {
            ((activity as Activity).window.decorView as ViewGroup).removeView(it)
            mStickerDetailsShowing = false
        }
    }

    override fun onOperationSelected(operation: Operation) {
        when (operation) {
            Operation.CropOperation -> mCropHelper.showCropDetails()
            Operation.PaintOperation -> {
                enableOrDisableEditorMode(Operation.PaintOperation, true)
                enableOrDisableEditorMode(Operation.BlurOperation, false)
            }
            Operation.TextOperation -> go2InputView(null)
            Operation.BlurOperation -> {
                enableOrDisableEditorMode(Operation.PaintOperation, false)
                enableOrDisableEditorMode(Operation.BlurOperation, true)
            }
            Operation.StickOperation -> go2StickerPanel()
        }
    }

    override fun onFuncModeUnselected(operation: Operation) {
        when (operation) {
            Operation.PaintOperation -> enableOrDisableEditorMode(Operation.PaintOperation, false)
            Operation.BlurOperation -> enableOrDisableEditorMode(Operation.BlurOperation, false)
            else -> logD1("operation=$operation,Unselected !")
        }
    }

    override fun onReceiveDetails(operation: Operation, funcDetailsMarker: FuncDetailsMarker) {
        when (operation) {
            Operation.PaintOperation -> setScrawlDetails(funcDetailsMarker as PaintDetail)
            Operation.BlurOperation -> setMosaicDetails(funcDetailsMarker as BlurDetal)
            else -> logD1("operation=$operation,onReceiveDetails !")
        }
    }

    override fun revoke(operation: Operation) {
        val view = findLayerByEditorMode(operation)
        if (view is BaseHierarchyView<*>) {
            view.revoke()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mTextInputResultCode) {
            resultFromInputView(resultCode, data)
        }
    }

    fun onBackPress(): Boolean {
        if (mStickerDetailsShowing) {
            closeStickerPanel()
            return true
        }
        return false
    }

    /**
     * AsyncTask for image Compose
     */
    inner class ImageComposeTask(private val mProvider: LayerViewProvider) : AsyncTask<String, Void, Boolean>() {

        private var mLoadingDialog = PhoenixLoadingDialog(mProvider.getActivityContext())
        private var mPath: String? = null
        private val layerComposite = mProvider.getLayerCompositeView()
        private val mEditorId = mProvider.getResultEditorId()

        override fun doInBackground(vararg params: String): Boolean {
            mPath = params[0]
            val cropState = mProvider.getCropHelper().getSavedCropState()
            val delegate = mProvider.getRootEditorDelegate()
            //draw image data layer by layer
            val rootBit = cropState?.cropBitmap ?: delegate.getDisplayBitmap()
            val compose = Bitmap.createBitmap(layerComposite.width, layerComposite.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(compose)
            canvas.drawBitmap(rootBit, delegate.getBaseLayoutMatrix(), null)
            MatrixUtils.callChildren(BaseHierarchyView::class.java, layerComposite) {
                val (supportMatrix, bitmap) = it.getEditorResult()
                bitmap?.let {
                    val matrix = Matrix()
                    matrix.set(supportMatrix)
                    canvas.drawBitmap(bitmap, matrix, null)
                }
            }
            val rect = delegate.getOriginalRect()!!
            val result = Bitmap.createBitmap(compose, rect.left.toInt(), rect.top.toInt(), rect.width().toInt(), rect.height().toInt())
            result.compress(Bitmap.CompressFormat.JPEG, 85, File(mPath).outputStream())
            recycleBitmap(compose)
            recycleBitmap(result)
            recycleBitmap(rootBit)
            //Save cached data.
            val cacheData = CacheUtils.getCache(mEditorId)
            MatrixUtils.callChildren(BaseHierarchyView::class.java, layerComposite) {
                it.saveLayerData(cacheData)
            }
            mProvider.getCropHelper().saveLayerData(cacheData)
            return true
        }

        override fun onPreExecute() {
            super.onPreExecute()
            MatrixUtils.callChildren(BaseHierarchyView::class.java, layerComposite) {
                it.onStartCompose()
            }
            mLoadingDialog.show()
        }

        override fun onCancelled() {
            super.onCancelled()
            mLoadingDialog.dismiss()
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            mLoadingDialog.dismiss()
            finish(result)
        }
    }
}
