package com.example.catfact.model

class Message(val text: String) {

    companion object {
        const val NO_INTERNET_CONNECTION = "Oops! You don't have internet connection! Cannot fetch new facts."

        const val LOCAL_DATABASE_QUERY_FAILED = "Cannot load kitty facts!"

        const val LOCAL_DATABASE_EMPTY = "Kitty database is empty!"

        const val REMOTE_DATABASE_QUERY_FAILED = "Cannot load kitty facts!"

        const val REMOTE_DATABASE_EMPTY = "Kitty database is empty!"
    }
}