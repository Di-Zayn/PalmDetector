package com.tongji.palmdetection.model

import java.io.File

object GlobalModel {
    private var path: File? = null
    private var userName: String = ""
    private var picNum: Int = 0

    public fun setPath(p:File) {
        path = p
    }

    public fun getPath(): File? {
        return path
    }

    public fun setUserName(n: String) {
        userName = n
    }

    public fun getUserName(): String? {
        return userName
    }

    fun addNum() {
        picNum++
    }


    fun resetNum() {
        picNum = 0
    }

    fun isRegister(): Boolean {
        return picNum == 4
    }

}