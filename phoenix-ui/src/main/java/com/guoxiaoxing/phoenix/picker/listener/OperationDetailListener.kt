package com.guoxiaoxing.phoenix.picker.listener

import com.guoxiaoxing.phoenix.picker.listener.Operation
import com.guoxiaoxing.phoenix.picker.model.FuncDetailsMarker

/**
 * ## UI function details result marker
 *
 * Created by lxw
 */
interface OperationDetailListener {

    fun onReceiveDetails(operation: Operation, funcDetailsMarker: FuncDetailsMarker)
}