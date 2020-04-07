package com.example.catfact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catfact.di.CatFactsComponent

class MainActivity : AppCompatActivity() {

    lateinit var catFactsComponent: CatFactsComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        catFactsComponent = (application as App).appComponent.catsComponent().create()
        catFactsComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
