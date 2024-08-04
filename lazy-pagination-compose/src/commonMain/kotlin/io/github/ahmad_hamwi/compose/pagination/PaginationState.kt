package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Stable
class PaginationState<T>(
    internal val onRequestPage: (PaginationState<T>.(Int) -> Unit)? = null
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

    fun setError(error: Exception) {
        internalState.value = PaginationInternalState.Error(error, internalState.value.items)
    }

    fun appendPage(items: List<T>, isLastPage: Boolean = false) {
        val pages = internalState.value.items
        val newPages = (pages ?: listOf()) + items

        requestedPageNumber++
        internalState.value = PaginationInternalState.Loaded(newPages, isLastPage)
    }

    fun retryLastFailedRequest() {
        internalState.value = PaginationInternalState.Loading(internalState.value.items)
    }

    fun refresh() {
        requestedPageNumber = 0
        internalState.value = PaginationInternalState.Initial()
    }
}

@Composable
fun <T> rememberPaginationState(
    onRequestPage: PaginationState<T>.(Int) -> Unit
): PaginationState<T> {
    return remember { PaginationState(onRequestPage) }
}