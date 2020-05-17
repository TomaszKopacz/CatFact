package com.example.catfact.cats

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catfact.di.ActivityScope
import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ActivityScope
class CatsViewModel @Inject constructor(
    private val catFactsFacade: CatFactsFacade
) : ViewModel() {

    private val randomCatFact = MutableLiveData<CatFact>()
    private val chosenCatFact = MutableLiveData<CatFact>()
    private val loading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Boolean>()

    suspend fun downloadCatFacts(number: Int) = catFactsFacade.getCatFacts(number)

    @SuppressLint("CheckResult")
    fun downloadOneFact() {
        catFactsFacade.getOneFact()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                loading.postValue(true)
            }
            .observeOn(Schedulers.io())
            .map { fact ->
                Thread.sleep(1000)
                fact
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { fact ->
                loading.postValue(false)

                when (fact) {
                    is Result.Success -> randomCatFact.postValue(fact.data)
                    is Result.Failure -> error.postValue(true)
                }
            }
    }

    fun randomCatFactObservable(): LiveData<CatFact> = randomCatFact

    fun onCatFactChosen(catFact: CatFact) = emitCatFact(catFact)

    fun catFactsObservable(): LiveData<Result<List<CatFact>>> = catFactsFacade.catFactsObservable()

    fun chosenCatFactObservable(): LiveData<CatFact> = chosenCatFact

    fun loadingStatusObservable(): LiveData<Boolean> = loading

    fun errorStatusObservable(): LiveData<Boolean> = error

    private fun emitCatFact(catFact: CatFact) = chosenCatFact.postValue(catFact)
}