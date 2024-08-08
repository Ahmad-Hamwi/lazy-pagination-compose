package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier

internal typealias LazyList = @Composable (PaginatedItemsHandler) -> Unit

internal typealias PaginatedItemsHandler = LazyListScope.(ClientLoadedContent) -> Unit

internal typealias ClientLoadedContent = LazyListScope.() -> Unit

@Suppress("UNCHECKED_CAST")
@Composable
internal fun <KEY, T> PaginatedLazyList(
    paginationState: PaginationState<KEY, T>,
    modifier: Modifier = Modifier,
    firstPageProgressIndicator: @Composable () -> Unit = {},
    newPageProgressIndicator: @Composable () -> Unit = {},
    firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    state: LazyListState,
    concreteLazyList: LazyList,
) {
    var internalState by paginationState.internalState

    LaunchedEffect(internalState) {
        (internalState as? PaginationInternalState.Loading)?.also {
            paginationState.run {
                onRequestPage.invoke(this, it.requestedPageKey)
            }
        }
    }

    if (internalState is PaginationInternalState.Loading && internalState.items == null) {
        firstPageProgressIndicator()
    }

    if (internalState is PaginationInternalState.Error && internalState.items == null) {
        firstPageErrorIndicator(
            (internalState as PaginationInternalState.Error).exception
        )
    }

    if (internalState.items != null) {
        LaunchedEffect(state) {
            snapshotFlow { state.layoutInfo.visibleItemsInfo.lastOrNull() }.collect { firstVisibleItemIndex ->
                val hasReachedLastItem = (firstVisibleItemIndex?.index ?: Int.MIN_VALUE) >=
                        (internalState.items?.lastIndex ?: Int.MAX_VALUE)

                val isLastPage =
                    (internalState as? PaginationInternalState.Loaded)?.isLastPage != false

                val newlyRequestedPageKey =
                    (internalState as? PaginationInternalState.Loaded)?.nextPageKey

                val previouslyRequestedPageKey =
                    (internalState as? PaginationInternalState.IHasRequestedPageKey<KEY>)?.requestedPageKey

                if (hasReachedLastItem && !isLastPage) {
                    internalState = PaginationInternalState.Loading(
                        initialPageKey = internalState.initialPageKey,
                        requestedPageKey = newlyRequestedPageKey
                            ?: previouslyRequestedPageKey
                            ?: internalState.initialPageKey,
                        items = internalState.items
                    )
                }
            }
        }

        concreteLazyList { clientContent ->
            val internalStateRef = internalState

            if (internalStateRef.items != null) {
                clientContent()
            }

            if (internalStateRef is PaginationInternalState.Loading) {
                item(
                    key = LazyListKeys.NEW_PAGE_PROGRESS_INDICATOR_KEY
                ) {
                    newPageProgressIndicator()
                }
            }

            if (internalStateRef is PaginationInternalState.Error) {
                item(
                    key = LazyListKeys.NEW_PAGE_ERROR_INDICATOR_KEY
                ) {
                    newPageErrorIndicator(
                        internalStateRef.exception
                    )
                }
            }
        }
    }

    LaunchedEffect(internalState) {
        if (internalState is PaginationInternalState.Initial) {
            val requestedPageKey =
                (internalState as? PaginationInternalState.IHasRequestedPageKey<KEY>)?.requestedPageKey

            internalState = PaginationInternalState.Loading(
                initialPageKey = internalState.initialPageKey,
                requestedPageKey = requestedPageKey ?: internalState.initialPageKey,
                items = internalState.items
            )
        }
    }
}
