package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <KEY, T> PaginatedLazyVerticalStaggeredGrid(
    paginationState: PaginationState<KEY, T>,
    columns: StaggeredGridCells,
    modifier: Modifier = Modifier,
    firstPageProgressIndicator: @Composable () -> Unit = {},
    newPageProgressIndicator: @Composable () -> Unit = {},
    firstPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    newPageErrorIndicator: @Composable (e: Exception) -> Unit = {},
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalItemSpacing: Dp = 0.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
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
        LazyVerticalStaggeredGrid(
            columns,
            modifier,
            state,
            contentPadding,
            reverseLayout,
            verticalItemSpacing,
            horizontalArrangement,
            flingBehavior,
            userScrollEnabled,
        ) {
            paginatedItemsHandler {
                content()
            }
        }
    }
}