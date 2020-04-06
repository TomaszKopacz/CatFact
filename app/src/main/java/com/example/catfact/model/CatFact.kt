package com.example.catfact.model

import com.google.gson.annotations.SerializedName

class CatFact (
    @SerializedName("text") val text: String
)