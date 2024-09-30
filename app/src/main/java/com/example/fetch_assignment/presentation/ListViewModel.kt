package com.example.fetch_assignment.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch_assignment.domain.ListRepository
import com.example.fetch_assignment.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListViewModel(private val listRepository: ListRepository) : ViewModel() {

    var state by mutableStateOf(ListState())
        private set

    private val eventChannel = Channel<ListEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ListAction) {
        when (action) {
            ListAction.OnLoadList -> {
                if (state.currentList.isNotEmpty()) {
                    state = state.copy(currentList = emptyList())
                }
                loadList()
            }
        }
    }

    private fun loadList() {
        viewModelScope.launch {
            when (val result = listRepository.loadList()) {
                is Result.Error -> eventChannel.send(ListEvent.LoadError)
                is Result.Success -> {
                    state = state.copy(currentList = result.data)
                }
            }
        }
    }
}