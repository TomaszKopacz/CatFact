package com.example.catfact.sources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.model.CatFact
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class LocalCatFactsRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var repository: LocalCatFactsRepository

    @Mock
    private lateinit var api: CatFactsLocalApi

    @Mock
    private lateinit var testFacts: List<CatFact>

    @Before
    fun setUp() {
        repository = LocalCatFactsRepository(api)
    }

    @Test
    fun `GIVEN LocalRepository WHEN api 'get' query return empty list THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(api.getAll())
                .thenReturn(ArrayList())

            val result = repository.getAll()
            assertTrue(result is Result.Failure)
            assertTrue((result as Result.Failure).message == Message(Message.LOCAL_DATABASE_EMPTY))
        }
    }

    @Test
    fun `GIVEN LocalRepository WHEN api 'get' query return list THEN result is Success`() {
        GlobalScope.launch {
            Mockito
                .`when`(api.getAll())
                .thenReturn(testFacts)

            val result = repository.getAll()
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data[0] == testFacts[0])
        }
    }

    @Test
    fun `GIVEN LocalRepository WHEN api 'update' query called THEN database is cleared and repopulated`() {
        GlobalScope.launch {
            repository.updateAll(testFacts)

            Mockito.verify(api.deleteAll())
            Mockito.verify(api.createAll(testFacts))
        }
    }
}