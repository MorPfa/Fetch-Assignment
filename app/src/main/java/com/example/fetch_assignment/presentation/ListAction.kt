package com.example.fetch_assignment.presentation

sealed interface ListAction {

    data object OnLoadList : ListAction

}