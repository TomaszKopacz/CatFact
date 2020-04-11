package com.example.catfact.sources.remote

import com.example.catfact.model.CatFact
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import com.example.catfact.util.network.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import retrofit2.Response
import java.sql.Timestamp

class RemoteCatFactsRepositoryTest {

    private lateinit var repository: RemoteCatFactsRepository

    private lateinit var api: RemoteApi
    private lateinit var networkManager: NetworkManager

    private lateinit var testFacts: List<CatFact>

    private lateinit var apiResponseBody: ResponseBody

    private val numOfElements = 30
    private val animalType = "cat"

    private val testErrorCode = 0

    @Before
    fun setUp() {
        api = Mockito.mock(RemoteApi::class.java)
        networkManager = Mockito.mock(NetworkManager::class.java)

        repository = RemoteCatFactsRepository(api, networkManager)

        apiResponseBody = Mockito.mock(ResponseBody::class.java)

        populateFactsList()
    }

    private fun populateFactsList() {
        val fact1 = CatFact("id1", "text1", Timestamp(0))
        val fact2 = CatFact("id2", "text2", Timestamp(0))
        val fact3 = CatFact("id3", "text3", Timestamp(0))

        testFacts = listOf(fact1, fact2, fact3)
    }

    @Test
    fun `GIVEN RemoteRepository WHEN no internet connection THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(networkManager.isConnected())
                .thenReturn(false)

            val result = repository.getSome(numOfElements)
            assertTrue(result is Result.Failure)
            assertTrue((result as Result.Failure).message == Message(Message.NO_INTERNET_CONNECTION))
        }
    }

    @Test
    fun `GIVEN RemoteRepository WHEN api 'get' query failed THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(networkManager.isConnected())
                .thenReturn(false)

            Mockito
                .`when`(api.getSomeFacts(anyString(), anyInt()))
                .thenReturn(Response.error(testErrorCode, apiResponseBody))

            val result = repository.getSome(numOfElements)
            assertTrue(result is Result.Failure)
            assertTrue((result as Result.Failure).message == Message(Message.REMOTE_DATABASE_QUERY_FAILED))
        }
    }

    @Test
    fun `GIVEN RemoteRepository WHEN internet connection established THEN fetch data`() {
        Mockito.`when`(networkManager.isConnected()).thenReturn(true)

        GlobalScope.launch {
            repository.getSome(numOfElements)
            Mockito.verify(api).getSomeFacts(animalType, numOfElements)
        }
    }

    @Test
    fun `GIVEN RemoteRepository WHEN api 'get' query succeed THEN result is Success`() {
        GlobalScope.launch {
            Mockito
                .`when`(networkManager.isConnected())
                .thenReturn(false)

            Mockito
                .`when`(api.getSomeFacts(anyString(), anyInt()))
                .thenReturn(Response.success(testFacts))

            val result = repository.getSome(numOfElements)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data[0] == testFacts[0])
        }
    }
}