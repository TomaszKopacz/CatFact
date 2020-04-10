package com.example.catfact.model

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    class Success<out T>(val data: T) : Result<T>()
    class Failure(val message: Message) : Result<Nothing>()
    class Warning(val message: Message) : Result<Nothing>()
}