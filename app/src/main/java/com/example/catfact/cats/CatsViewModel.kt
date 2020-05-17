package com.example.catfact.cats

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

    private val randomCatFacts = MutableLiveData<Result<List<CatFact>>>()
    private val chosenCatFact = MutableLiveData<CatFact>()

    private val isLoading = MutableLiveData<Boolean>()

    fun downloadCatFacts(number: Int) {
        catFactsFacade.downloadRandomCatFacts(number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                emitLoadingStatus(true)
                it
            }
            .observeOn(Schedulers.io())
            .map {
                Thread.sleep(1000)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                emitLoadingStatus(false)
                emitRandomCatFacts(result)
            }
    }

    fun onCatFactChosen(catFact: CatFact) = emitCatFact(catFact)

    private fun emitRandomCatFacts(result: Result<List<CatFact>>) = randomCatFacts.postValue(result)

    private fun emitCatFact(catFact: CatFact) = chosenCatFact.postValue(catFact)

    private fun emitLoadingStatus(status: Boolean) = isLoading.postValue(status)

    fun randomCatFactsData(): LiveData<Result<List<CatFact>>> = randomCatFacts

    fun chosenCatFactData(): LiveData<CatFact> = chosenCatFact

    fun loadingStatusData(): LiveData<Boolean> = isLoading

}