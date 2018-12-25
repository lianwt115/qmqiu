package com.guoxiaoxing.phoenix.picker.listener

import com.guoxiaoxing.phoenix.picker.listener.Operation

interface OperationListener {

    fun onOperationSelected(operation: Operation)

    fun onFuncModeUnselected(operation: Operation)

}