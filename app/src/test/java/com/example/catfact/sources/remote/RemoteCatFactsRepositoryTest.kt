package com.example.catfact.sources.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.catfact.model.CatFact
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import com.example.catfact.util.network.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response

class RemoteCatFactsRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var repository: RemoteCatFactsRepository

    @Mock
    private lateinit var api: RemoteApi

    @Mock
    private lateinit var networkManager: NetworkManager

    @Mock
    private lateinit var testFacts: List<CatFact>

    @Mock
    private lateinit var apiResponseBody: ResponseBody

    @Before
    fun setUp() {
        repository = RemoteCatFactsRepository(api, networkManager)
    }

    @Test
    fun `GIVEN RemoteRepository WHEN no internet connection THEN result is Failure`() {
        GlobalScope.launch {
            Mockito
                .`when`(networkManager.isConnected())
                .thenReturn(false)

            val result = repository.getSome(NUM_OF_ELEMENTS)
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
                .thenReturn(Response.error(ERROR_CODE, apiResponseBody))

            val result = repository.getSome(NUM_OF_ELEMENTS)
            assertTrue(result is Result.Failure)
            assertTrue((result as Result.Failure).message == Message(Message.REMOTE_DATABASE_QUERY_FAILED))
        }
    }

    @Test
    fun `GIVEN RemoteRepository WHEN internet connection established THEN fetch data`() {
        Mockito.`when`(networkManager.isConnected()).thenReturn(true)

        GlobalScope.launch {
            repository.getSome(NUM_OF_ELEMENTS)
            Mockito.verify(api).getSomeFacts(ANIMAL_TYPE, NUM_OF_ELEMENTS)
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

            val result = repository.getSome(NUM_OF_ELEMENTS)
            assertTrue(result is Result.Success)
            assertTrue((result as Result.Success).data[0] == testFacts[0])
        }
    }

    companion object {
        private const val NUM_OF_ELEMENTS = 30
        private const val ANIMAL_TYPE = "cat"
        private const val ERROR_CODE = 0
    }
}