package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <KEY, T> PaginatedLazyHorizontalGrid(
    paginationState: PaginationState<KEY, T>,
    rows: GridCells,
    modifier: Modifier = Modifier,
    firstPageProgressIndicator: @Composable () -> Unit = {},
    newPageProgressIndicator: @Composable () -> Unit = {},
    firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit
) {
    PaginatedLazyScrollable<KEY, T, LazyGridState, LazyGridScope>(
        paginationState,
        modifier,
        firstPageProgressIndicator,
        newPageProgressIndicator,
        firstPageErrorIndicator,
        newPageErrorIndicator,
        state,
    ) { paginatedItemsHandler ->
        LazyHorizontalGrid(
            rows,
            modifier,
            state,
            contentPadding,
            reverseLayout,
            horizontalArrangement,
            verticalArrangement,
            flingBehavior,
            userScrollEnabled,
        ) {
            paginatedItemsHandler {
                content()
            }
        }
    }
}