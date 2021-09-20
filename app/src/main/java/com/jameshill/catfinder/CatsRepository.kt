package com.jameshill.catfinder

import io.reactivex.Single

//extends Repository class so the retrofit variable will
//be available to use as it's instantiated in the init!
class CatsRepository(
    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String
) : repository(baseUrl, isDebugEnabled, apiKey) {

    private val catsDataSource: CatsDataSource = CatsDataSource(retrofit)

    // a class to wrap around the response to make things easier later.
    inner class Result(
        val catData: List<CatData>? = null,
        val errorMessage: String? = null
    ) {
        fun hasCats(): Boolean {
            return catData != null && !catData.isEmpty()
        }

        fun hasError(): Boolean {
            return errorMessage != null
        }
    }

    // the method called by our activity
    fun getNumberOfRandomCats(limit: Int, category_ids: Int?): Single<Result> {

        return catsDataSource.getNumberOfRandomCats(limit, category_ids)
            .map { catData: List<CatData> -> Result(catData = catData) }
            .onErrorReturn { t: Throwable ->
                Result(errorMessage = t.message)
            }
    }
}

