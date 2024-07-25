<h1 align="center">Lazy Pagination - Compose Multiplatform</h1>

<p align="center">
    <a href="https://opensource.org/licenses/MIT"><img alt="API" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
    <a href="https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose"><img alt="API" src="https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose/badge.svg"/></a>
</p>
<br>

<p align="center">
    An intuitive and customizable Compose Multiplatform pagination solution built on top of lazy lists (Currently LazyColumn) and handles pagination states automatically as you scroll.
</p>
<p align="center">
    Available on Android, iOS, and JVM Desktop
</p>

## What to expect? ##

The library is build on top of lazy composables such as `LazyColumn` and it extends its API ot be `PaginatedLazyColumn`, and provides you with a custom `PaginationState` that you can control in your Compose UI or in a ViewModel to handle different pagination states as the user scroll.

## Available on Android, iOS, and Desktop ##

In a Compose Multiplaform Project, You can now add your composable to the `commonMain` source set only

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExaDZtN3dzajNicXpxZjYwNWdlMTZuNmEydzJqeXI4bzhlZThmYmVyayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/UQtqa7NP2DedMIQQE0/giphy.gif" width="32%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHZydHgyNmJyYjI2bXA3M3VsbnRvbXYwcTdkMWVycWlwa3ZudTMxaiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/hyOaVKanSlkesmRWLS/giphy.gif" width="32%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExb3ozdXpubDA2enptdW81aHhucndpZ2Y2MGw5cTFuMmNneDcxM3JocyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RxUB3WW69I3N65pQuv/giphy.gif" width="32%" align="top" />
</p>


# Setup #

Get the latest version via Maven Central:

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose)

Add Maven Central repository to your root build.gradle at the end of repositories:

```kotlin
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

Then add the dependency

```toml
[versions]
lazy-pagination-compose = "1.0.0"

[libraries]
lazyPaginationCompose = { module = "io.github.ahmad-hamwi:lazy-pagination-compose", version.ref = "lazy-pagination-compose" }
```

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation(libs.lazyPaginationCompose)
    }
}
```

# Usage #

## 1- Initilize your pagination state ##

### Create a `PaginationState` by remembering it in your composable or adding it to you ViewModel.

```kotlin
val paginationState = rememberPaginationState<Model>(...)
```

### Pass your `onRequestPage` callback when creating your `PaginationState` and call your data source ###

```kotlin
val paginationState = rememberPaginationState<Model>(
    onRequestPage = { pageNumber: Int ->
        val page = DataSource.getPage(pageNumber) // A coroutine that's tied to a composable scope
    }
)
```

### Append data using `appendPage` and flag the end of your list using `isLastPage` ###
```kotlin
val paginationState = rememberPaginationState(
    onRequestPage = { pageNumber: Int ->
        val page = DataSource.getPage(pageNumber)

        appendPage(
            items = page.items,
            isLastPage = page.isLastPage // optional, defaults to false
        )
    }
)
```

### Handle errors using `setError` ###
```kotlin
val paginationState = rememberPaginationState(
    onRequestPage = { pageNumber: Int ->
        try {
            val page = DataSource.getPage(pageNumber)

            appendPage(
                items = page.items,
                isLastPage = page.isLastPage
            )
        } catch (e: Exception) {
            setError(e)
        }
    }
)
```

### Retrying your last failed request can be through `retryLastFailedRequest` ###
```kotlin
paginationState.retryLastFailedRequest()
```

### Refreshing can be through `refresh` method ###
```kotlin
paginationState.refresh(
    initialPage = 1 // optional and defaults to 1
)
```

## 2- Define your paginated `LazyColumn` ##

### Provide your composables for every pagination state you would like to render ###

```kotlin
PaginatedLazyColumn(
    paginationState = paginationState,
    firstPageProgressIndicator = { ... },
    newPageProgressIndicator = { ... },
    firstPageErrorIndicator = { e -> ... },
    newPageErrorIndicator = { e -> ... },
) {
    itemsIndexed(
        paginationState.allItems,
    ) { _, item ->
        Item(value = item)
    }
}
```
**Altogether**


**Altogether with a ViewModel**
>```kotlin
>class MyViewModel : ViewModel() {
>    val paginationState = PaginationState<Model>(
>        onRequestPage = { loadPage(it) }
>    )
>    
>    fun loadPage(pageNumber: Int) {
>        viewModelScope.launch {
>           try {
>               val page = DataSource.getPage(pageNumber)
>    
>               paginationState.appendPage(
>                   items = page.items,
>                   isLastPage = page.isLastPage
>               )
>           } catch (e: Exception) {
>               paginationState.setError(e)
>           }
>        }
>    }
>}
>
>@Composable
>fun Content(viewModel: MyViewModel) {
>    val paginationState = viewModel.paginationState
>
>    PaginatedLazyColumn(
>        paginationState = paginationState,
>        firstPageProgressIndicator = { ... },
>        newPageProgressIndicator = { ... },
>        firstPageErrorIndicator = { e ->
>            ... onRetry = { paginationState.retryLastFailedRequest() } ...
>        },
>        newPageErrorIndicator = { e ->
>            ... onRetry = { paginationState.retryLastFailedRequest() } ...
>        },
>    ) {
>        itemsIndexed(
>            paginationState.allItems,
>        ) { _, item ->
>            Item(value = item)
>        }
>    }
>}
>```

# Contributing #
This library is made to help other developers out in their app developments, feel free to contribute by suggesting ideas and creating issues and PRs that would make this repository more helpful.

# License #
You can show support by either contributing to the repository or by buying me a cup of coffee!

<p>
    <a href="https://www.buymeacoffee.com/ahmadhamwi" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" />
</p>

# License

Copyright (C) 2024 Ahmad Hamwi

Licensed under the MIT License
