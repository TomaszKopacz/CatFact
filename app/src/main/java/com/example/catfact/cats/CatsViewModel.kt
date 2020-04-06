package com.example.catfact.cats

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.catfact.di.ActivityScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class CatsViewModel @Inject constructor(private val catsManager: CatsFacade): ViewModel() {

    fun downloadCats() {
        GlobalScope.launch {
            val catsFacts = catsManager.getCatFacts()
            if (catsFacts.isSuccessful) {
                for (fact in catsFacts.body()!!.all) {
                    Log.d("CatFact", fact.text)
                }

            } else {

            }
        }
    }

    fun details() {
        Log.d("CatFact", "Details")
        Log.d("CatFact", this.toString())
    }
}