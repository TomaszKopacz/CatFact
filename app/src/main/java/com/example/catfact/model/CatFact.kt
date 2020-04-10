package com.example.catfact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

@Entity(tableName = "cat_facts")
class CatFact (

    @SerializedName(value = "_id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @SerializedName(value = "text")
    @ColumnInfo(name = "text")
    val text: String,

    @SerializedName(value = "updatedAt")
    @ColumnInfo(name = "updatedAt")
    val updatedAt: Timestamp
)