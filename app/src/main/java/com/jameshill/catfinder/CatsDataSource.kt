package com.jameshill.catfinder

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

//declare class and constructor
class CatsDataSource (retrofit: Retrofit) {
    private val api: CatsApi = retrofit.create(CatsApi::class.java)

    fun getNumberOfRandomCats(limit:Int, category_ids:Int?) =
        api.getNumberOfRandomCats(limit, category_ids)

    interface CatsApi {
        @GET("images/search")
        fun getNumberOfRandomCats(
            @Query("limit") limit: Int,
            @Query("category_ids") category_ids: Int?
        ): Single<List<CatData>>

    }
}