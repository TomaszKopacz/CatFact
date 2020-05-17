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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class CatsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var catsViewModel: CatsViewModel

    @Mock
    private lateinit var catFactsFacade: CatFactsFacade

    @Mock
    private lateinit var testCatFact: CatFact

    @Before
    fun setUp() {
        catsViewModel = CatsViewModel(catFactsFacade)
    }

    @Test
    fun `GIVEN CatsViewModel WHEN viewModel created THEN cat facts are fetched`() {
        GlobalScope.launch {
            CatsViewModel(catFactsFacade)

            Mockito.verify(catFactsFacade).downloadRandomCatFacts(NUM_OF_ELEMENTS)
        }
    }

    @Test
    fun `GIVEN CatsViewModel WHEN downloadCatFacts() called THEN cat facts are fetched`() {
        GlobalScope.launch {
            catsViewModel.downloadCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(catFactsFacade).downloadRandomCatFacts(NUM_OF_ELEMENTS)
        }
    }

    @Test
    fun `GIVEN CatsViewModel WHEN cat fact chosen THEN emit cat fact`() {
        catsViewModel.onCatFactChosen(testCatFact)

        catsViewModel.chosenCatFactData().observeOnce { catFact ->
            assertTrue(catFact == testCatFact)
        }
    }

    companion object {
        private const val NUM_OF_ELEMENTS = 30
    }
}