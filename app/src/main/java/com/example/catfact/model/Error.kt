package com.example.catfact.model

sealed class Error(val code: Int) {
    class Http(code: Int) : Error(code)

    class General(code: Int) : Error(code) {
        companion object {
            const val GENERAL_ERROR_CODE = 1000
        }
    }

}