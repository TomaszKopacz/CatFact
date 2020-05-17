package com.example.catfact.cats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.model.CatFact
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import com.example.catfact.sources.CatFactsRepository
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

class CatFactsFacadeTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var catFactsFacade: CatFactsFacade

    @Mock
    private lateinit var remoteRepo: CatFactsRepository

    @Mock
    private lateinit var localRepo: CatFactsRepository

    @Mock
    private lateinit var testFactsList: List<CatFact>

    @Before
    fun setup() {
        catFactsFacade = CatFactsFacade(remoteRepo, localRepo)
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called THEN remote data is fetched`() {
        GlobalScope.launch {
            catFactsFacade.downloadRandomCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(remoteRepo).getCatFacts(NUM_OF_ELEMENTS)
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called THEN local data is updated`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getCatFacts(NUM_OF_ELEMENTS))
                .thenReturn(Result.Success(testFactsList))

            catFactsFacade.downloadRandomCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(localRepo).updateAll(testFactsList)
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called THEN local data is fetched`() {
        GlobalScope.launch {
            catFactsFacade.downloadRandomCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(localRepo).getAll()
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called and all repo queries succeed THEN result is Success`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getCatFacts(NUM_OF_ELEMENTS))
                .thenReturn(Result.Success(testFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Success(testFactsList))

            catFactsFacade.downloadRandomCatFacts(NUM_OF_ELEMENTS)

            catFactsFacade.randomCatFactsData().observeOnce { result ->
                assertTrue(result is Result.Success)
                assertTrue((result as Result.Success).data[0] == testFactsList[0])
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called and remote repo query failed THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getCatFacts(NUM_OF_ELEMENTS))
                .thenReturn(Result.Failure(Message(ERROR_MESSAGE)))

            catFactsFacade.downloadRandomCatFacts(NUM_OF_ELEMENTS)

            catFactsFacade.randomCatFactsData().observeOnce { result ->
                assertTrue(result is Result.Failure)
                assertTrue((result as Result.Failure).message.text == ERROR_MESSAGE)
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called and local repo query failed THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getCatFacts(NUM_OF_ELEMENTS))
                .thenReturn(Result.Success(testFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Failure(Message(ERROR_MESSAGE)))

            catFactsFacade.downloadRandomCatFacts(NUM_OF_ELEMENTS)

            catFactsFacade.randomCatFactsData().observeOnce { result ->
                assertTrue(result is Result.Failure)
                assertTrue((result as Result.Failure).message.text == ERROR_MESSAGE)
            }
        }
    }

    companion object {
        private const val ERROR_MESSAGE = "ERROR"
        private const val NUM_OF_ELEMENTS = 30
    }
}