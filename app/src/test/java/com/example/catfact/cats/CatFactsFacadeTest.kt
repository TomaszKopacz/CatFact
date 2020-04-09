package com.example.catfact.cats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Message
import com.example.catfact.data.Result
import com.example.catfact.model.CatFact
import com.example.catfact.util.test.observeOnce
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class CatFactsFacadeTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var remoteRepo: CatFactsRepository
    private lateinit var localRepo: CatFactsRepository

    private lateinit var catFactsList: List<CatFact>

    @Before
    fun setup() {
        remoteRepo = Mockito.mock(CatFactsRepository::class.java)
        localRepo = Mockito.mock(CatFactsRepository::class.java)

        populateList()
    }

    private fun populateList() {
        val fact1 = CatFact("id1", "text1")
        val fact2 = CatFact("id2", "text2")
        val fact3 = CatFact("id3", "text3")

        catFactsList = listOf(fact1, fact2, fact3)
    }

    @Test
    fun `GIVEN download attempt WHEN data being fetched THEN status is Loading`() {

        val catFactsFacade = CatFactsFacade(remoteRepo, localRepo)

        runBlocking {
            Mockito
                .`when`(remoteRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            catFactsFacade.getCatFacts()
        }

        catFactsFacade.catFactsObservable().observeOnce { result ->
            assertTrue(result is Result.Loading)
        }
    }

    @Test
    fun `GIVEN download attempt WHEN data fetched correctly THEN status is Success`() {

        val catFactsFacade = CatFactsFacade(remoteRepo, localRepo)
        catFactsFacade.disableLoadingStatusSending()

        runBlocking {
            Mockito
                .`when`(remoteRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Success(catFactsList))

            catFactsFacade.getCatFacts()
        }

        catFactsFacade.catFactsObservable().observeOnce { result ->
            assertTrue(result is Result.Success)
        }
    }

    @Test
    fun `GIVEN download attempt WHEN remote source query failed THEN status is Warning`() {

        val catFactsFacade = CatFactsFacade(remoteRepo, localRepo)
        catFactsFacade.disableLoadingStatusSending()

        runBlocking {
            Mockito
                .`when`(remoteRepo.getAll())
                .thenReturn(Result.Failure(Message("Some Error Text")))

            catFactsFacade.getCatFacts()
        }

        catFactsFacade.catFactsObservable().observeOnce { result ->
            assertTrue(result is Result.Warning)
        }
    }

    @Test
    fun `GIVEN download attempt WHEN local query failed THEN status is Failure`() {

        val catFactsFacade = CatFactsFacade(remoteRepo, localRepo)
        catFactsFacade.disableLoadingStatusSending()

        runBlocking {
            Mockito
                .`when`(localRepo.getAll())
                .thenReturn(Result.Failure(Message("Some Error Text")))

            catFactsFacade.getCatFacts()
        }

        catFactsFacade.catFactsObservable().observeOnce { result ->
            assertTrue(result is Result.Failure)
        }
    }
}