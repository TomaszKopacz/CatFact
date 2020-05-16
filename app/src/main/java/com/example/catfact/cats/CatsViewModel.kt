package com.example.catfact.cats

import android.annotation.SuppressLint
import android.util.Log
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

    private val chosenCatFact = MutableLiveData<CatFact>()
    private val loading = MutableLiveData<Boolean>()

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
                CatFact("MY ID", fact.text, fact.updatedAt)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                loading.postValue(false)
                Log.d("CatFact", "Cat fact id is: " + it.id)
                Log.d("CatFact", "Cat fact text is: " + it.text)
            }
    }

    fun onCatFactChosen(catFact: CatFact) = emitCatFact(catFact)

    fun catFactsObservable(): LiveData<Result<List<CatFact>>> = catFactsFacade.catFactsObservable()

    fun chosenCatFactObservable(): LiveData<CatFact> = chosenCatFact

    fun loadingStatusObservable(): LiveData<Boolean> = loading

    private fun emitCatFact(catFact: CatFact) = chosenCatFact.postValue(catFact)
}