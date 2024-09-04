package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <KEY, T> PaginatedLazyHorizontalStaggeredGrid(
    paginationState: PaginationState<KEY, T>,
    rows: StaggeredGridCells,
    modifier: Modifier = Modifier,
    firstPageProgressIndicator: @Composable () -> Unit = {},
    newPageProgressIndicator: @Composable () -> Unit = {},
    firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    horizontalItemSpacing: Dp = 0.dp,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyStaggeredGridScope.() -> Unit
) {
    PaginatedLazyScrollable<KEY, T, LazyStaggeredGridState, LazyStaggeredGridScope>(
        paginationState,
        modifier,
        firstPageProgressIndicator,
        newPageProgressIndicator,
        firstPageErrorIndicator,
        newPageErrorIndicator,
        state,
    ) { paginatedItemsHandler ->
        LazyHorizontalStaggeredGrid(
            rows,
            modifier,
            state,
            contentPadding,
            reverseLayout,
            verticalArrangement,
            horizontalItemSpacing,
            flingBehavior,
            userScrollEnabled,
        ) {
            paginatedItemsHandler {
                content()
            }
        }
    }
}