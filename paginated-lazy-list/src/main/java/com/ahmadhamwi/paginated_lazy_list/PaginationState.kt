package com.ahmadhamwi.paginated_lazy_list

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
class PaginationState<T>(
    internal val pageRequestListener: ((Int) -> Unit)? = null
) {
    internal var internalState =
        mutableStateOf<PaginationInternalState<T>>(PaginationInternalState.Initial())

    internal var requestedPageNumber = 0

    val allItems: List<T>
        get() = if (internalState.value.items == null) {
            throw NoSuchElementException("No pages are appeneded yet")
        } else {
            internalState.value.items!!
        }

    val totalItemsSize: Int?
        get() = internalState.value.totalItemCount

    fun setError(error: Exception) {
        internalState.value = PaginationInternalState.Error(error, internalState.value.items)
    }

    fun appendPage(page: List<T>, isLastPage: Boolean = false, totalItemCount: Int? = null) {
        val pages = internalState.value.items
        val newPages = (pages ?: listOf()) + page

        requestedPageNumber++
        internalState.value = PaginationInternalState.Loaded(newPages, totalItemCount, isLastPage)
    }

    fun retryLastFailedRequest() {
        internalState.value = PaginationInternalState.Loading(internalState.value.items)
    }

    fun refresh() {
        requestedPageNumber = 0
        internalState.value = PaginationInternalState.Initial()
    }
}