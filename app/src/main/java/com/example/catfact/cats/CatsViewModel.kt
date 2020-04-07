package com.example.catfact.cats

import android.util.Log
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

    private val catFacts = MutableLiveData<List<CatFact>>()

    fun downloadCats() {
        GlobalScope.launch {
            when (val result = catFactsFacade.getCatFacts()) {
                is Result.Success -> { catFacts.postValue(result.data) }
                is Result.Failure -> { }
            }
        }
    }

    fun details() {
        Log.d("CatFact", "Details")
        Log.d("CatFact", this.toString())
    }

    fun catFacts(): LiveData<List<CatFact>> = catFacts
}