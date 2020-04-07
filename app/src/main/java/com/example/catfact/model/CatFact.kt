package com.example.catfact.model

import com.google.gson.annotations.SerializedName

class CatFact (
    @SerializedName("_id") val id: String,
    @SerializedName("text") val text: String
)