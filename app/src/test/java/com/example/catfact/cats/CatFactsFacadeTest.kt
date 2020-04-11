package com.example.catfact.cats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.model.Message
import com.example.catfact.model.Result
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

class CatFactsFacadeTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var catFactsFacade: CatFactsFacade

    private lateinit var remoteRepo: CatFactsRepository
    private lateinit var localRepo: CatFactsRepository

    private lateinit var testFactsList: List<CatFact>

    companion object {
        private const val ERROR_MESSAGE = "ERROR"
        private const val NUM_OF_ELEMENTS = 30
    }

    @Before
    fun setup() {
        remoteRepo = Mockito.mock(CatFactsRepository::class.java)
        localRepo = Mockito.mock(CatFactsRepository::class.java)

        catFactsFacade = CatFactsFacade(remoteRepo, localRepo)

        populateList()
    }

    private fun populateList() {
        val fact1 = CatFact("id1", "text1", Timestamp(0))
        val fact2 = CatFact("id2", "text2", Timestamp(0))
        val fact3 = CatFact("id3", "text3", Timestamp(0))

        testFactsList = listOf(fact1, fact2, fact3)
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called THEN remote data is fetched`() {
        GlobalScope.launch {
            catFactsFacade.getCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(remoteRepo).getSome(NUM_OF_ELEMENTS)
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called THEN local data is updated`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getSome(NUM_OF_ELEMENTS))
                .thenReturn(Result.Success(testFactsList))

            catFactsFacade.getCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(localRepo).updateAll(testFactsList)
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called THEN local data is fetched`() {
        GlobalScope.launch {
            catFactsFacade.getCatFacts(NUM_OF_ELEMENTS)

            Mockito.verify(localRepo).getAll()
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called and all repo queries succeed THEN result is Success`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getSome(NUM_OF_ELEMENTS))
                .thenReturn(Result.Success(testFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Success(testFactsList))

            catFactsFacade.getCatFacts(NUM_OF_ELEMENTS)

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Success)
                assertTrue((result as Result.Success).data[0] == testFactsList[0])
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called and remote repo query failed THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getSome(NUM_OF_ELEMENTS))
                .thenReturn(Result.Failure(Message(ERROR_MESSAGE)))

            catFactsFacade.getCatFacts(NUM_OF_ELEMENTS)

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Failure)
                assertTrue((result as Result.Failure).message.text == ERROR_MESSAGE)
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN getCatFacts() called and local repo query failed THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getSome(NUM_OF_ELEMENTS))
                .thenReturn(Result.Success(testFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Failure(Message(ERROR_MESSAGE)))

            catFactsFacade.getCatFacts(NUM_OF_ELEMENTS)

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Failure)
                assertTrue((result as Result.Failure).message.text == ERROR_MESSAGE)
            }
        }
    }
}