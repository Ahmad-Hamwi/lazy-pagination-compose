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

    fun setError(exception: Exception) {
        val internalStateSnapshot = internalState.value
        val nextPageNumberOfLoadingState: Int? =
            (internalStateSnapshot as? PaginationInternalState.Loaded)?.nextPageNumber
        val requestedPageNumberOfLoadingOrErrorState: Int? =
            (internalStateSnapshot as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber

        // requestedPageNumber is going to either be
        // 1- the nextPageNumber of the loaded state: because the page has already been loaded and showing an error means a new page error state
        // OR
        // 2- the requestedPageNumber of either a loading or a previous error state
        internalState.value = PaginationInternalState.Error(
            initialPageNumber = internalStateSnapshot.initialPageNumber,
            requestedPageNumber = nextPageNumberOfLoadingState
                ?: requestedPageNumberOfLoadingOrErrorState
                ?: internalStateSnapshot.initialPageNumber,
            exception = exception,
            items = internalState.value.items
        )
    }

    fun appendPage(items: List<T>, nextPageNumber: Int, isLastPage: Boolean = false) {
        val newItems = (internalState.value.items ?: listOf()) + items
        val internalStateSnapshot = internalState.value
        val requestedPageNumberOfLoadingOrErrorState: Int? =
            (internalStateSnapshot as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber

        internalState.value = PaginationInternalState.Loaded(
            initialPageNumber = internalState.value.initialPageNumber,
            requestedPageNumber = requestedPageNumberOfLoadingOrErrorState
                ?: internalStateSnapshot.initialPageNumber,
            nextPageNumber = nextPageNumber,
            items = newItems,
            isLastPage = isLastPage
        )
    }

    fun retryLastFailedRequest() {
        val internalStateSnapshot = internalState.value

        require(internalStateSnapshot is PaginationInternalState.Error) {
            "retryLastFailedRequest is invoked while there were no errors has been set using setError"
        }

        internalState.value = PaginationInternalState.Loading(
            initialPageNumber = internalStateSnapshot.initialPageNumber,
            requestedPageNumber = internalStateSnapshot.requestedPageNumber,
            items = internalStateSnapshot.items
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