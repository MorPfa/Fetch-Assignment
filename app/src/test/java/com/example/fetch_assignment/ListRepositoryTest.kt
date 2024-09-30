package com.example.fetch_assignment

import com.example.fetch_assignment.data.ListRepositoryImpl
import com.example.fetch_assignment.data.network.ListItemDto
import com.example.fetch_assignment.data.network.ListService
import com.example.fetch_assignment.data.network.toListItem
import com.example.fetch_assignment.domain.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class ListRepositoryTest {

    private val listService = mock<ListService>()
    private val listRepository = ListRepositoryImpl(listService)
    private lateinit var expectedList: List<ListItemDto>


    @Before
    fun setup() {
        expectedList = listOf(
            ListItemDto(
                name = "Item 1",
                id = "1",
                listId = "1"
            ),
            ListItemDto(
                name = "Item 2",
                id = "2",
                listId = "2"
            ),
            ListItemDto(
                name = "Item 3",
                id = "3",
                listId = "3"
            ),
        )
    }

    @Test
    fun testLoadImages() {
        runTest {
            val response = Response.success(expectedList)
            val formattedList = expectedList.map {
                it.toListItem()
            }
            whenever(listService.loadList()).thenReturn(response)
            when (val result = listRepository.loadList()) {
                is Result.Success -> {
                    assertEquals(formattedList, result.data)
                }
                is Result.Error -> {
                    fail("Expected a successful result but got an error: ${result.error}")
                }
            }
        }
    }

}