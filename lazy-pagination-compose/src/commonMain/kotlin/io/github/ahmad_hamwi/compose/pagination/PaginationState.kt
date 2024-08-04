package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Stable
class PaginationState<T>(
    initialPageNumber: Int,
    internal val onRequestPage: PaginationState<T>.(Int) -> Unit
) {
    internal var internalState =
        mutableStateOf<PaginationInternalState<T>>(PaginationInternalState.Initial(initialPageNumber))

    val requestedPageNumber: Int?
        get() = (internalState.value as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber

    val allItems: List<T>
        get() = if (internalState.value.items == null) {
            throw NoSuchElementException("No pages are appeneded yet")
        } else {
            internalState.value.items!!
        }

    fun setError(error: Exception) {
        internalState.value = PaginationInternalState.Error(
            internalState.value.initialPageNumber,
            (internalState.value as? PaginationInternalState.Loaded)?.nextPageNumber
                ?: (internalState.value as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber
                ?: internalState.value.initialPageNumber,
            error,
            internalState.value.items
        )
    }

    fun appendPage(items: List<T>, nextPageNumber: Int, isLastPage: Boolean = false) {
        val pages = internalState.value.items
        val newPages = (pages ?: listOf()) + items

        internalState.value = PaginationInternalState.Loaded(
            internalState.value.initialPageNumber,
            (internalState.value as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber
                ?: internalState.value.initialPageNumber,
            nextPageNumber,
            newPages,
            isLastPage
        )
    }

    fun retryLastFailedRequest() {
        internalState.value = PaginationInternalState.Loading(
            internalState.value.initialPageNumber,
            (internalState.value as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber
                ?: internalState.value.initialPageNumber,
            internalState.value.items
        )
    }

    fun refresh(initialPageNumber: Int? = null) {
        internalState.value = PaginationInternalState.Initial(
            initialPageNumber ?: internalState.value.initialPageNumber
        )
    }
}

@Composable
fun <T> rememberPaginationState(
    initialPageNumber: Int,
    onRequestPage: PaginationState<T>.(Int) -> Unit
): PaginationState<T> {
    return remember {
        PaginationState(
            initialPageNumber = initialPageNumber,
            onRequestPage = onRequestPage
        )
    }
}