package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Suppress("UNCHECKED_CAST")
@Stable
class PaginationState<KEY, T>(
    initialPageKey: KEY,
    internal val onRequestPage: PaginationState<KEY, T>.(KEY) -> Unit
) {
    internal var internalState =
        mutableStateOf<PaginationInternalState<KEY, T>>(PaginationInternalState.Initial(initialPageKey))

    val requestedPageKey: KEY?
        get() = (internalState.value as? PaginationInternalState.IHasRequestedPageKey<KEY>)?.requestedPageKey

    val allItems: List<T>
        get() = if (internalState.value.items == null) {
            throw NoSuchElementException("No pages are appeneded yet")
        } else {
            internalState.value.items!!
        }

    fun setError(exception: Exception) {
        val internalStateSnapshot = internalState.value
        val nextPageKeyOfLoadingState: KEY? =
            (internalStateSnapshot as? PaginationInternalState.Loaded)?.nextPageKey
        val requestedPageKeyOfLoadingOrErrorState: KEY? =
            (internalStateSnapshot as? PaginationInternalState.IHasRequestedPageKey<KEY>)?.requestedPageKey

        // requestedPageKey is going to either be
        // 1- the nextPageKey of the loaded state: because the page has already been loaded and showing an error means a new page error state
        // OR
        // 2- the requestedPageKey of either a loading or a previous error state
        internalState.value = PaginationInternalState.Error(
            initialPageKey = internalStateSnapshot.initialPageKey,
            requestedPageKey = nextPageKeyOfLoadingState
                ?: requestedPageKeyOfLoadingOrErrorState
                ?: internalStateSnapshot.initialPageKey,
            exception = exception,
            items = internalState.value.items
        )
    }

    fun appendPage(items: List<T>, nextPageKey: KEY, isLastPage: Boolean = false) {
        val newItems = (internalState.value.items ?: listOf()) + items
        val internalStateSnapshot = internalState.value
        val requestedPageKeyOfLoadingOrErrorState: KEY? =
            (internalStateSnapshot as? PaginationInternalState.IHasRequestedPageKey<KEY>)?.requestedPageKey

        internalState.value = PaginationInternalState.Loaded(
            initialPageKey = internalState.value.initialPageKey,
            requestedPageKey = requestedPageKeyOfLoadingOrErrorState
                ?: internalStateSnapshot.initialPageKey,
            nextPageKey = nextPageKey,
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
            initialPageKey = internalStateSnapshot.initialPageKey,
            requestedPageKey = internalStateSnapshot.requestedPageKey,
            items = internalStateSnapshot.items
        )
    }

    fun refresh(initialPageKey: KEY? = null) {
        internalState.value = PaginationInternalState.Initial(
            initialPageKey ?: internalState.value.initialPageKey
        )
    }
}

@Composable
fun <KEY, T> rememberPaginationState(
    initialPageKey: KEY,
    onRequestPage: PaginationState<KEY, T>.(KEY) -> Unit
): PaginationState<KEY, T> {
    return remember {
        PaginationState(
            initialPageKey = initialPageKey,
            onRequestPage = onRequestPage
        )
    }
}