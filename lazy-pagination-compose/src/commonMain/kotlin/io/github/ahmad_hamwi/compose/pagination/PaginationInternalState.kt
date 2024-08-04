package io.github.ahmad_hamwi.compose.pagination

internal sealed class PaginationInternalState<T>(
    open val initialPageNumber: Int,
    open val items: List<T>?,
) {
    class Initial<T>(
        override val initialPageNumber: Int,
        override val items: List<T>? = null,
    ) : PaginationInternalState<T>(initialPageNumber, null)

    class Loading<T>(
        override val initialPageNumber: Int,
        override val requestedPageNumber: Int,
        override val items: List<T>? = null,
    ) : PaginationInternalState<T>(initialPageNumber, items), IHasRequestedPageNumber

    class Loaded<T>(
        override val initialPageNumber: Int,
        override val requestedPageNumber: Int,
        override val items: List<T>,
        val isLastPage: Boolean,
    ) : PaginationInternalState<T>(initialPageNumber, items), IHasRequestedPageNumber

    class Error<T>(
        override val initialPageNumber: Int,
        override val requestedPageNumber: Int,
        val error: Exception,
        override val items: List<T>? = null,
    ) : PaginationInternalState<T>(initialPageNumber, items), IHasRequestedPageNumber

    interface IHasRequestedPageNumber {
        val requestedPageNumber: Int
    }
}