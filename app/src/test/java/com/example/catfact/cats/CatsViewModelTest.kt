package com.example.catfact.cats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.data.CatFactsRepository
import com.example.catfact.model.CatFact
import com.example.catfact.util.test.observeOnce
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class CatsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var catsViewModel: CatsViewModel
    private lateinit var catFactsFacade: CatFactsFacade

    private lateinit var testCatFact: CatFact

    @Before
    fun setUp() {
        catFactsFacade = Mockito.mock(CatFactsFacade::class.java)
        catsViewModel = CatsViewModel(catFactsFacade)

        testCatFact = CatFact("id", "text")
    }

    @Test
    fun `GIVEN CatsViewModel WHEN viewModel created THEN cat facts are fetched`() {
        GlobalScope.launch {
            CatsViewModel(catFactsFacade)
            Mockito.verify(catFactsFacade).getCatFacts()
        }
    }

    @Test
    fun `GIVEN cat facts WHEN cat fact chosen THEN emit cat fact`() {
        catsViewModel.onCatFactChosen(testCatFact)

        catsViewModel.chosenCatFactObservable().observeOnce { catFact ->
            assertTrue(catFact == testCatFact)
        }
    }
}