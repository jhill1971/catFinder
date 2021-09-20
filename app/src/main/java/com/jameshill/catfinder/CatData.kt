package com.jameshill.catfinder

import com.google.gson.annotations.SerializedName
// declare data class and constructor
// This class represents the JSON response

data class CatData(
    //@SerializedName is an annotation that indicates this member should be serialized to JSON with
    // the provided name value as its field name.
    @SerializedName("id") val id:String,
    @SerializedName("url") val url: String,
    @SerializedName("breeds") val breeds: List<Any>,
    @SerializedName("categories") val categories: List<Any>
)
{
    override fun toString(): String {
        //override default toString function and return the following:
        return "CatData (id = $id, url = $url, breed = $breeds, category = $categories"
    }
}
