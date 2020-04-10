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

    private lateinit var catFactsList: List<CatFact>

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

        catFactsList = listOf(fact1, fact2, fact3)
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN data being fetched THEN status is Loading`() {
        GlobalScope.launch {
            Mockito
                .`when`(remoteRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            catFactsFacade.getCatFacts()

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Loading)
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN data fetched correctly THEN status is Success`() {
        GlobalScope.launch {
            catFactsFacade.disableLoadingStatusSending()

            Mockito
                .`when`(remoteRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            catFactsFacade.getCatFacts()

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Success)
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN remote source query failed THEN status is Warning`() {
        GlobalScope.launch {
            catFactsFacade.disableLoadingStatusSending()

            Mockito
                .`when`(remoteRepo.getAll())
                .thenReturn(Result.Failure(Message("Some Error Text")))

            catFactsFacade.getCatFacts()

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Warning)
            }
        }
    }

    @Test
    fun `GIVEN CatFactsFacade WHEN local query failed THEN status is Failure`() {
        GlobalScope.launch {
            catFactsFacade.disableLoadingStatusSending()

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Failure(Message("Some Error Text")))

            catFactsFacade.getCatFacts()

            catFactsFacade.catFactsObservable().observeOnce { result ->
                assertTrue(result is Result.Failure)
            }
        }
    }
}