package com.example.catfact.cats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.model.CatFact
import com.example.catfact.util.test.observeOnce
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.sql.Timestamp

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

        testCatFact = CatFact("id", "text", Timestamp(0))
    }

    @Test
    fun `GIVEN CatsViewModel WHEN viewModel created THEN cat facts are fetched`() {
        GlobalScope.launch {
            CatsViewModel(catFactsFacade)
            Mockito.verify(catFactsFacade).getCatFacts()
        }
    }

    @Test
    fun `GIVEN CatsViewModel WHEN download cat facts requested THEN cat facts are fetched`() {
        GlobalScope.launch {
            catsViewModel.downloadCatFacts()
            Mockito.verify(catFactsFacade).getCatFacts()
        }
    }

    @Test
    fun `GIVEN CatsViewModel WHEN cat fact chosen THEN emit cat fact`() {
        catsViewModel.onCatFactChosen(testCatFact)

        catsViewModel.chosenCatFactObservable().observeOnce { catFact ->
            assertTrue(catFact == testCatFact)
        }
    }
}