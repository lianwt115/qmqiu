package com.lwt.qmqiu.im

import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.orhanobut.logger.Logger
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import java.text.ParsePosition
import java.text.SimpleDateFormat















class IMUtils private constructor() {

    companion object {
        @Volatile
        private var mInstance: IMUtils? = null

        fun getInstance(): IMUtils {
            if (mInstance == null) {
                synchronized(IMUtils::class.java) {
                    if (mInstance == null) {
                        mInstance = IMUtils()
                    }
                }
            }
            return mInstance!!
        }

    }

    private var accountList =HashMap<String,String>()

    private fun addAccountData(){

        accountList["test0"] = "4726e55bff597b2f02ab5808a7543b35"
        accountList["test1"] = "90eb046711cdfef081fa65c40e3d1942"
        accountList["test2"] = "fb3863e73003476bb5a68397ba91fa07"
        accountList["test3"] = "4eb56795d4d19188717c0dddf49ed407"
        accountList["test4"] = "3529dc5a9b703e31b68e39aee7523cc3"
        accountList["test5"] = "6877a0f8e3bb40e5913ae2e36f2f15f3"
        accountList["test6"] = "e6256115efa6fa89c5b44e4d9c7bd13a"
        accountList["test7"] = "6bd6ad7eb645c45749f60725b8c60862"
        accountList["test8"] = "bbd2ab3b9994098d5f0b93989ac87b79"
        accountList["test9"] = "77bf9c8886528f74af0781aa836fa06c"
        accountList["test10"] = "82796b412569873b06385ec005414390"
        accountList["lwt"] = "3674ead4b2d927e99fe21179e9c9ac3a"
        accountList["cg"] = "86208e9066fb2ef3cd101d538317037f"
        accountList["wxjz"] = "0421bce3a802034725ff36eed4c6ca42"
        accountList["qjp"] = "be17fb4d514958c47873399ac053670a"
        accountList["zc"] = "faed61fc14205f566eb9f285f5342e7c"
        accountList["qmx"] = "33a60a753d4836d9a9b502dcf8ac93b4"
        accountList["hj"] = "be27c01657b503abd4a735522f50121f"
        accountList["tq"] = "45f4d9230af00a63924756f95a668f05"
    }

    private  var mIMEventListen:IMEventListen?=null


    fun doLogin(account: String?,mIMEventListen:IMEventListen) {

        if (accountList.size == 0)
            addAccountData()

        this.mIMEventListen = mIMEventListen

        val callback = object : RequestCallback<LoginInfo> {

            override fun onSuccess(param: LoginInfo?) {

                Logger.e("登录成功--account:${param?.account}--token:${param?.token}")

                getMessage()

                mIMEventListen?.login(true)

            }

            override fun onFailed(code: Int) {

                Logger.e("IM登录错误码:$code")
                mIMEventListen?.login(false)

            }

            override fun onException(exception: Throwable?) {
                mIMEventListen?.login(false)
                Logger.e("IM登录异常信息:${exception?.localizedMessage}")
            }

            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
        }

        NIMClient.getService(AuthService::class.java).login(getLoginInfo(account))
                .setCallback(callback)
    }

    private fun getLoginInfo(account:String?):LoginInfo?{


        var loginInfo: LoginInfo?


        if (accountList.containsKey(account))

            loginInfo =LoginInfo(account,accountList[account])

        else

            loginInfo = null


        return  loginInfo

    }


    private fun getMessage(){

        var incomingMessageObserver = Observer<List<IMMessage>> {

            mIMEventListen?.message(it)

        }

        NIMClient.getService(MsgServiceObserve::class.java).observeReceiveMessage(incomingMessageObserver, true)
    }


    fun sendTxtMessage(text:String){

        val sessionType = SessionTypeEnum.P2P

        accountList.keys.forEach {


            val textMessage = MessageBuilder.createTextMessage(it, sessionType, text)

            // 发送给对方
            NIMClient.getService(MsgService::class.java).sendMessage(textMessage, false)

        }

    }

    interface IMEventListen{

        fun login(success:Boolean)
        fun message(list:List<IMMessage>)

    }

    fun doLoginOut(){

        mIMEventListen = null

        NIMClient.getService(AuthService::class.java).logout()
    }


}