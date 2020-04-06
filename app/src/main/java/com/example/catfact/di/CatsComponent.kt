package com.example.catfact.di

import com.example.catfact.cats.CatsFragment
import com.example.catfact.cats.DetailsFragment
import com.example.catfact.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface CatsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CatsComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: CatsFragment)
    fun inject(fragment: DetailsFragment)
}