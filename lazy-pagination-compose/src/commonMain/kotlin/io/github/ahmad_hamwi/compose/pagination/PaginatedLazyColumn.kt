package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object LazyListKeys {
    const val NEW_PAGE_PROGRESS_INDICATOR_KEY = "newPageProgressIndicatorKey"
    const val NEW_PAGE_ERROR_INDICATOR_KEY = "newPageErrorIndicatorKey"
}

@Composable
fun <T> PaginatedLazyColumn(
    paginationState: PaginationState<T>,
    modifier: Modifier = Modifier,
    firstPageProgressIndicator: @Composable () -> Unit = {},
    newPageProgressIndicator: @Composable () -> Unit = {},
    firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    var internalState by paginationState.internalState

    LaunchedEffect(internalState) {
        (internalState as? PaginationInternalState.Loading)?.also {
            paginationState.run {
                onRequestPage.invoke(this, it.requestedPageNumber)
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

                val newlyRequestedPageNumber =
                    (internalState as? PaginationInternalState.Loaded)?.nextPageNumber

                val previouslyRequestedPageNumber =
                    (internalState as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber

                if (hasReachedLastItem && !isLastPage) {
                    internalState = PaginationInternalState.Loading(
                        initialPageNumber = internalState.initialPageNumber,
                        requestedPageNumber = newlyRequestedPageNumber
                            ?: previouslyRequestedPageNumber
                            ?: internalState.initialPageNumber,
                        items = internalState.items
                    )
                }
            }
        }

        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
        ) {
            val internalStateRef = internalState

            if (internalStateRef.items != null) {
                content()
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
            val requestedPageNumber =
                (internalState as? PaginationInternalState.IHasRequestedPageNumber)?.requestedPageNumber

            internalState = PaginationInternalState.Loading(
                initialPageNumber = internalState.initialPageNumber,
                requestedPageNumber = requestedPageNumber ?: internalState.initialPageNumber,
                items = internalState.items
            )
        }
    }
}