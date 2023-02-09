package com.example.testkoltin

import com.google.gson.annotations.SerializedName

data class Topic(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val name: String,
)