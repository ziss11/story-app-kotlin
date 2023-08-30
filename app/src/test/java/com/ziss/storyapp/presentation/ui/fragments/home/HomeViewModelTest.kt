package com.ziss.storyapp.presentation.ui.fragments.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.data.repositories.StoryRepository
import com.ziss.storyapp.presentation.adapters.StoryAdapter
import com.ziss.storyapp.utils.DummyData
import com.ziss.storyapp.utils.MainDispatcherRule
import com.ziss.storyapp.utils.ResultState
import com.ziss.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}

    override fun onRemoved(position: Int, count: Int) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(storyRepository)
    }

    @Test
    fun `when Get Story should not null and return data`() = runTest {
        val testToken = "token"
        val dummyStory = DummyData.generateDummyStories()
        val data: PagingData<StoryModel> = PagingData.from(dummyStory)
        val expectedStory =
            MutableLiveData<ResultState<PagingData<StoryModel>>>(ResultState.Success(data))

        `when`(storyRepository.getStories(testToken)).thenReturn(expectedStory)

        val result = viewModel.getStories(testToken).getOrAwaitValue() as ResultState.Success
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(result.data)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story should return no data`() = runTest {
        val testToken = "token"
        val data: PagingData<StoryModel> = PagingData.from(emptyList())
        val expectedStory =
            MutableLiveData<ResultState<PagingData<StoryModel>>>(ResultState.Success(data))

        `when`(storyRepository.getStories(testToken)).thenReturn(expectedStory)

        val result = viewModel.getStories(testToken).getOrAwaitValue() as ResultState.Success
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(result.data)

        assertEquals(0, differ.snapshot().size)
    }
}