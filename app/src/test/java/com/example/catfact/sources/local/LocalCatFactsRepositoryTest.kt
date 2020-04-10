package com.example.catfact.sources.local

import com.example.catfact.model.CatFact
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.sql.Timestamp

class LocalCatFactsRepositoryTest {

    private lateinit var repository: LocalCatFactsRepository

    private lateinit var api: CatFactsLocalApi

    private lateinit var testFacts: List<CatFact>

    @Before
    fun setUp() {
        api = Mockito.mock(CatFactsLocalApi::class.java)

        repository = LocalCatFactsRepository(api)

        populateFactsList()
    }

    private fun populateFactsList() {
        val fact1 = CatFact("id1", "text1", Timestamp(0))
        val fact2 = CatFact("id2", "text2", Timestamp(0))
        val fact3 = CatFact("id3", "text3", Timestamp(0))

        testFacts = listOf(fact1, fact2, fact3)
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