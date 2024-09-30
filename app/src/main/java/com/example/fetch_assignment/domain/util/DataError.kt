package com.example.fetch_assignment.domain.util

sealed interface DataError : Error {
    enum class Network : DataError {
        PARSING_ERROR,
        NETWORK_FAILURE,
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        RESOURCE_NOT_FOUND,
        TOO_MANY_REQUESTS,
        REQUEST_TIMEOUT,
        EMPTY_RESPONSE_BODY,
        SERVER_ERROR,
        UNKNOWN
    }
}