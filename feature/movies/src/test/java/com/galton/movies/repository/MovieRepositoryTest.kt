package com.galton.movies.repository

import androidx.paging.PagingSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.galton.database.movie.MovieDao
import com.galton.database.movie.MovieTable
import com.galton.network.MoviesApiService
import com.galton.test.PagingSourceTestUtil
import com.galton.test.RemoteServiceTestUtil
import com.galton.test.UnitTest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import retrofit2.HttpException
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class MovieRepositoryTest : UnitTest() {

    companion object {
        private const val MOCK_MOVIE_ID = 0
    }

    private lateinit var repository: MovieRepository
    private var mockWebserver = MockWebServer()

    @MockK
    private lateinit var api: MoviesApiService

    @MockK
    private lateinit var movieDao: MovieDao

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxed = true)
        mockWebserver.start()
        api = spyk(
            RemoteServiceTestUtil.getRetrofit(
                mockWebserver.url("/").toString(),
                MoviesApiService::class.java
            )
        )
        startKoin {
            modules(
                module {
                    single { api }
                    single { movieDao }
                    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
                }
            )
        }
        repository = MovieRepository(api, movieDao)
    }

    @After
    fun finalise() {
        stopKoin()
        mockWebserver.shutdown()
        unmockkAll()
    }

    @Test
    fun `addFavoriteMovie should call insertFavoriteMovie in movieDao`() = runTest {
        repository.addFavoriteMovie(MOCK_MOVIE_ID)

        coVerify {
            movieDao.insertFavoriteMovie(MOCK_MOVIE_ID)
        }
    }

    @Test
    fun `deleteFavoriteMovie should call insertFavoriteMovie in movieDao`() = runTest {
        repository.deleteFavoriteMovie(MOCK_MOVIE_ID)

        coVerify {
            movieDao.deleteFavoriteMovie(MOCK_MOVIE_ID)
        }
    }

    @Test
    fun `test getMovieById should call getById in movieDao and result should not be null`() = runTest {
        every { movieDao.getById(MOCK_MOVIE_ID) } returns flowOf(mockMovieTableResult())

        val result = repository.getMovieById(MOCK_MOVIE_ID)

        result.apply {
            shouldNotBeNull()
            collect {
                it.shouldNotBeNull()
                it.id?.shouldBeEqual(MOCK_MOVIE_ID)
            }
        }
        coVerify {
            movieDao.getById(MOCK_MOVIE_ID)
        }
    }

    @Test
    fun `test getPagingMovies should call pagingMovies in movieDao and result should not be null`() = runTest {
        every { movieDao.pagingMovies() } returns mockPaging()
        val result = repository.getPagingMovies().load(PagingSource.LoadParams.Refresh(null, 20, false))
        result.shouldNotBeNull()
        val expectedLoadResultPage = PagingSource.LoadResult.Page(
            data = mockMoviesList(),
            prevKey = null,
            nextKey = null
        )
        result.shouldBe(expectedLoadResultPage)
        verify {
            movieDao.pagingMovies()
        }
    }

    @Test
    fun `test getFavoriteMovies should call pagingFavoriteMovies in movieDao and result should not be null`() =
        runTest {
            every { movieDao.pagingFavoriteMovies() } returns mockPaging()
            val result = repository.getFavoriteMovies().load(PagingSource.LoadParams.Refresh(null, 20, false))
            result.shouldNotBeNull()
            val expectedLoadResultPage = PagingSource.LoadResult.Page(
                data = mockMoviesList(),
                prevKey = null,
                nextKey = null
            )
            result.shouldBe(expectedLoadResultPage)
            verify {
                movieDao.pagingFavoriteMovies()
            }
        }

    @Test
    fun `test getSearchedMovies should call pagingSearchedMovies in movieDao and result should not be null`() =
        runTest {
            val query = "a"
            every { movieDao.pagingSearchedMovies(query) } returns mockPaging()
            val result = repository.getSearchedMovies(query).load(PagingSource.LoadParams.Refresh(null, 20, false))
            result.shouldNotBeNull()
            val expectedLoadResultPage = PagingSource.LoadResult.Page(
                data = mockMoviesList(),
                prevKey = null,
                nextKey = null
            )
            result.shouldBe(expectedLoadResultPage)
            verify {
                movieDao.pagingSearchedMovies(query)
            }
        }

    @Test
    fun `test getAndUpdateMovies should call getMovies from api and clearThenInsert in movieDao without favorites`() =
        runTest {
            enqueueLiveStreamData(mockWebserver, HttpURLConnection.HTTP_OK, true)

            coEvery { movieDao.getFavorites() } returns emptyList()
            coJustRun { movieDao.clearThenInsert(any()) }

            repository.getAndUpdateMovies()

            coVerify {
                movieDao.getFavorites()
            }
            coVerify {
                movieDao.clearThenInsert(any())
            }
        }

    @Test
    fun `test getAndUpdateMovies should call getMovies from api and clearThenInsert in movieDao with favorites`() =
        runTest {
            enqueueLiveStreamData(mockWebserver, HttpURLConnection.HTTP_OK, true)

            coEvery { movieDao.getFavorites() } returns mockMoviesList()
            coJustRun { movieDao.clearThenInsert(any()) }

            repository.getAndUpdateMovies()

            coVerify {
                movieDao.getFavorites()
            }
            coVerify {
                movieDao.clearThenInsert(any())
            }
        }

    @Test
    fun `test getAndUpdateMovies should should not call database if there is a API error`() =
        runTest {
            enqueueLiveStreamData(mockWebserver, HttpURLConnection.HTTP_BAD_REQUEST, false)

            coEvery { movieDao.getFavorites() } returns mockMoviesList()
            coJustRun { movieDao.clearThenInsert(any()) }

            assertThrows(HttpException::class.java) {
                runBlocking {
                    repository.getAndUpdateMovies()
                }
            }

            coVerify(exactly = 0) {
                movieDao.getFavorites()
            }
            coVerify(exactly = 0) {
                movieDao.clearThenInsert(any())
            }
        }

    private fun mockMoviesList() = listOf(
        MovieTable(
            MOCK_MOVIE_ID,
            "A Star Is Born (2018)",
            null,
            "14.99",
            "Romance",
            "-",
            true,
            "Bradley Cooper",
            updatedAt = 0L
        )
    )

    private fun mockPaging(): PagingSourceTestUtil<MovieTable> {
        return PagingSourceTestUtil(mockMoviesList())
    }

    private fun mockMovieTableResult(): MovieTable {
        return MovieTable(
            MOCK_MOVIE_ID,
            "A Star Is Born (2018)",
            null,
            "14.99",
            "Romance",
            "-",
            false,
            "Bradley Cooper",
            updatedAt = 0L
        )
    }

    private fun enqueueLiveStreamData(
        mockWebServer: MockWebServer,
        responseCode: Int = HttpURLConnection.HTTP_OK,
        success: Boolean = true
    ) {
        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(
                RemoteServiceTestUtil.readResourceFileUnitTest(
                    if (success) {
                        "movies_success.json"
                    } else {
                        "movies_error.json"
                    }
                )
            )
        mockWebServer.enqueue(response)
    }
}
