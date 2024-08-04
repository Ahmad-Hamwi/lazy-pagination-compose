package io.github.ahmad_hamwi.compose.pagination

internal sealed class PaginationInternalState<KEY, T>(
    open val initialPageKey: KEY,
    open val items: List<T>?,
) {
    class Initial<KEY, T>(
        override val initialPageKey: KEY,
        override val items: List<T>? = null,
    ) : PaginationInternalState<KEY, T>(initialPageKey, null)

    class Loading<KEY, T>(
        override val initialPageKey: KEY,
        override val requestedPageKey: KEY,
        override val items: List<T>? = null,
    ) : PaginationInternalState<KEY, T>(initialPageKey, items), IHasRequestedPageKey<KEY>

    class Loaded<KEY, T>(
        override val initialPageKey: KEY,
        override val requestedPageKey: KEY,
        val nextPageKey: KEY,
        override val items: List<T>,
        val isLastPage: Boolean,
    ) : PaginationInternalState<KEY, T>(initialPageKey, items), IHasRequestedPageKey<KEY>

    class Error<KEY, T>(
        override val initialPageKey: KEY,
        override val requestedPageKey: KEY,
        val exception: Exception,
        override val items: List<T>? = null,
    ) : PaginationInternalState<KEY, T>(initialPageKey, items), IHasRequestedPageKey<KEY>

    interface IHasRequestedPageKey<KEY> {
        val requestedPageKey: KEY
    }
}