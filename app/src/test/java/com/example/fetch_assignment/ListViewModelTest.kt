@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.fetch_assignment

import com.example.fetch_assignment.domain.ListItem
import com.example.fetch_assignment.domain.ListRepository
import com.example.fetch_assignment.domain.util.DataError
import com.example.fetch_assignment.domain.util.Result
import com.example.fetch_assignment.presentation.ListAction
import com.example.fetch_assignment.presentation.ListEvent
import com.example.fetch_assignment.presentation.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ListViewModelTest {

    private val listRepository = mock<ListRepository>()
    private val viewModel = ListViewModel(listRepository)
    private lateinit var expectedList: List<ListItem>

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        expectedList = listOf(
            ListItem
                (
                name = "Item 1",
                id = "1",
                listId = "1"
            ),
            ListItem
                (
                name = "Item 2",
                id = "2",
                listId = "2"
            ),
            ListItem
                (
                name = "Item 3",
                id = "3",
                listId = "3"
            ),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test that state gets updated correctly when new list is fetched`(){
        runTest {
            whenever(listRepository.loadList()).thenReturn(Result.Success(expectedList))

            val stateBefore = viewModel.state
            viewModel.onAction(ListAction.OnLoadList)
            testDispatcher.scheduler.advanceUntilIdle()

            val stateAfter = viewModel.state
            assertNotEquals(stateBefore, stateAfter)
            verify(listRepository).loadList()
        }
    }

    @Test
    fun `test that list gets loaded when loadList() gets called`(){
        runTest {
            whenever(listRepository.loadList()).thenReturn(Result.Success(expectedList))

            viewModel.onAction(ListAction.OnLoadList)
            testDispatcher.scheduler.advanceUntilIdle()

            val result = viewModel.state.currentList
            assertEquals(expectedList, result)
        }
    }

    @Test
    fun `test that LoadError event is sent when loadList fails`(){
        runTest {
            whenever(listRepository.loadList()).thenReturn(Result.Error(DataError.Network.SERVER_ERROR))
            viewModel.onAction(ListAction.OnLoadList)
            val event = viewModel.events.first()
            assertEquals(ListEvent.LoadError, event)
        }
    }
}