package com.example.catfact.sources.remote

import com.example.catfact.model.CatFact
import com.example.catfact.model.Error
import com.example.catfact.model.Result
import com.example.catfact.sources.CatFactsRepository
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class RemoteCatFactsRepository (
    private val remoteApi: RemoteApi
) : CatFactsRepository {

    override fun getCatFacts(number: Int): Observable<Result<List<CatFact>>> {

        return Observable.create<Result<List<CatFact>>> { emitter ->

            val call = remoteApi.getSomeFacts(CAT, number)

            call.enqueue(object : Callback<List<CatFact>> {

                override fun onFailure(call: Call<List<CatFact>>, t: Throwable) {
                    emitter.onNext(Result.Failure(Error.General(Error.General.GENERAL_ERROR_CODE)))
                }

                override fun onResponse(
                    call: Call<List<CatFact>>,
                    response: Response<List<CatFact>>
                ) {
                    if (!response.isSuccessful)
                        emitter.onNext(Result.Failure(Error.Http(response.code())))

                    else
                        emitter.onNext(Result.Success(response.body()))
                }
            })

        }
    }

    companion object {
        private const val CAT: String = "cat"
    }
}