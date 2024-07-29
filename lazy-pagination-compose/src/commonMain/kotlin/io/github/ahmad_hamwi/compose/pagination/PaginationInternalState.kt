package io.github.ahmad_hamwi.compose.pagination

internal sealed class PaginationInternalState<T>(
    open val items: List<T>?,
) {
    class Initial<T> : PaginationInternalState<T>(null)

    class Loading<T>(
        override val items: List<T>? = null,
    ) : PaginationInternalState<T>(items)

    class Loaded<T>(
        override val items: List<T>,
        val isLastPage: Boolean,
    ) : PaginationInternalState<T>(items)

    class Error<T>(
        val error: Exception,
        override val items: List<T>? = null,
    ) : PaginationInternalState<T>(items)
}