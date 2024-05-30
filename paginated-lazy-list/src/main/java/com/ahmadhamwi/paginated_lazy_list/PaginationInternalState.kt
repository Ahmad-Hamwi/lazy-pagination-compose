package com.ahmadhamwi.paginated_lazy_list

internal sealed class PaginationInternalState<T>(
    open val items: List<T>?,
    open val totalItemCount: Int?,
) {
    class Initial<T> : PaginationInternalState<T>(null, null)

    class Loading<T>(
        override val items: List<T>? = null,
        override val totalItemCount: Int? = null,
    ) : PaginationInternalState<T>(items, totalItemCount)

    class Loaded<T>(
        override val items: List<T>,
        override val totalItemCount: Int? = null,
        val isLastPage: Boolean,
    ) : PaginationInternalState<T>(items, totalItemCount)

    class Error<T>(
        val error: Exception,
        override val items: List<T>? = null,
        override val totalItemCount: Int? = null
    ) : PaginationInternalState<T>(items, totalItemCount)
}