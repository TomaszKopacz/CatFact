package com.example.catfact.data

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    class Success<out T>(val data: T) : Result<T>()
    class Failure(val message: String) : Result<Nothing>()
}