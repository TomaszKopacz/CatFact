package com.example.catfact.model

import com.google.gson.annotations.SerializedName

class CatFactsResponse(
    @SerializedName("all") val facts: List<CatFact>)