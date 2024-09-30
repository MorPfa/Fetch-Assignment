package com.example.fetch_assignment.presentation

sealed interface ListEvent {

    data object LoadError : ListEvent
}