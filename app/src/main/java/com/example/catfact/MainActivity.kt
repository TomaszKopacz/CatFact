package com.example.catfact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catfact.R
import com.example.catfact.di.AppComponent
import com.example.catfact.di.CatsComponent

class MainActivity : AppCompatActivity() {

    lateinit var catsComponent: CatsComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        catsComponent = (application as App).appComponent.catsComponent().create()
        catsComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
