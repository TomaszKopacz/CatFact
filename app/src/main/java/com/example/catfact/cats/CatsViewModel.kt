package com.example.catfact.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catfact.data.Result
import com.example.catfact.di.ActivityScope
import com.example.catfact.model.CatFact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class CatsViewModel @Inject constructor(private val catFactsFacade: CatFactsFacade): ViewModel() {

    private val catFactsResult = MutableLiveData<Result<List<CatFact>>>()

    private val catFactChosen = MutableLiveData<CatFact>()

    init {
        downloadCats()
    }

    fun downloadCats() {
        GlobalScope.launch {
            val result = catFactsFacade.getCatFacts()
            catFactsResult.postValue(result)
        }
    }

    fun onCatFactChosen(catFact: CatFact) {
        catFactChosen.postValue(catFact)
    }

    fun catFactsResult(): LiveData<Result<List<CatFact>>> = catFactsResult

    fun catFact(): LiveData<CatFact> = catFactChosen
}