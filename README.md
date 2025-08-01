<h1 align="center">Lazy Pagination - Compose Multiplatform</h1>

<p align="center">
    <a href="https://opensource.org/licenses/MIT"><img alt="API" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
    <img alt="API" src="https://img.shields.io/maven-central/v/io.github.ahmad-hamwi/lazy-pagination-compose"/>
    <a href="http://kotlinlang.org"><img alt="API" src="https://img.shields.io/badge/kotlin-2.1.20-blue.svg?logo=kotlin"/></a>
    <img alt="API" src="https://github.com/Ahmad-Hamwi/lazy-pagination-compose/actions/workflows/unit-test.yml/badge.svg"/>
    <br/>
    <br/>
    <img alt="API" src="https://img.shields.io/badge/-Android-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-iOS-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-JVM-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-Windows-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-Linux-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-macOS-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-JS-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-WASM-gray.svg?style=flat"/>
</p>
<br>

An intuitive and customizable Compose Multiplatform pagination solution built on top of lazy composables and it extends their APIs.

Basically prefix your lazy composables such as `LazyColumn` or `LazyVerticalGrid` and more to add pagination support!

Two points where in mind while creating this library:
- Should have a simple and intuitive APIs with an easy learning curve.
- Can be placed in your Presentation/UI layer ONLY.

## Features ##

- Vertical & horizontal lists pagination using `PaginatedLazyColumn` & `PaginatedLazyRow` with the same original APIs.
- Vertical & horizontal grids pagination using `PaginatedLazyVerticalGrid` & `PaginatedLazyHorizontalGrid` with the same original APIs.
- Seamlessly integrate your pagination state with your your data layer.  
- Generic fetching strategies such as 
[offset-based](https://developer.box.com/guides/api-calls/pagination/offset-based/), 
[cursor-based](https://jsonapi.org/profiles/ethanresnick/cursor-pagination/), 
[time-based](https://developers.facebook.com/docs/graph-api/results/), etc...
- Resetting your pagination state using `refresh()` function
- Retrying your data fetches using `retryLastFailedRequest()` function

## Paginated Composables ##

### `PaginatedLazyColumn`: ###

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExaDZtN3dzajNicXpxZjYwNWdlMTZuNmEydzJqeXI4bzhlZThmYmVyayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/UQtqa7NP2DedMIQQE0/giphy.gif" width="27%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHZydHgyNmJyYjI2bXA3M3VsbnRvbXYwcTdkMWVycWlwa3ZudTMxaiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/hyOaVKanSlkesmRWLS/giphy.gif" width="25.62%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExb3ozdXpubDA2enptdW81aHhucndpZ2Y2MGw5cTFuMmNneDcxM3JocyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RxUB3WW69I3N65pQuv/giphy.gif" width="45.5%" align="top" />
</p>

<details open>
    <summary> 
        <h3>Composable</h3> 
    </summary>

```kotlin
PaginatedLazyColumn(
    paginationState = paginationState,
    firstPageProgressIndicator = { ... },
    newPageProgressIndicator = { ... },
    firstPageErrorIndicator = { e -> // from setError
        ... e.message ...
        ... onRetry = { paginationState.retryLastFailedRequest() } ...
    },
    newPageErrorIndicator = { e -> ... },
    firstPageEmptyIndicator = { ... },
    newPageEmptyIndicator = { ... },
    // The rest of LazyColumn params
) {
    itemsIndexed(
        paginationState.allItems!!, // safe to access here
    ) { _, item ->
        Item(value = item)
    }
}
```

</details>

### `PaginatedLazyRow`: ###

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExODIwZHduaXhhaGN6MWUxb3luZHlqN2xvMm9vNDBmcGoyNzF4bnhnYSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Tv7k3mPfmkDzss8EBk/giphy.gif" width="27%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExdXg2NXp1bHlmbWV1OXB0Nzd0eTRrOXV0eGo1eHRvZXA2Nmg3ejQ0dyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/U0EhwO1KN5keyfBFH1/giphy.gif" width="25.62%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExN2M3cWV3MWFtbGQxMmhmaWx1ejRtYnVoM202bHN0NzRiMnVyYW8xYiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/BfuyEzSm7ZcH6A4sYj/giphy.gif" width="45.5%" align="top" />
</p>

<details open>
    <summary> 
        <h3>Composable</h3> 
    </summary>

```kotlin
PaginatedLazyRow(
    paginationState = paginationState,
    firstPageProgressIndicator = { ... },
    newPageProgressIndicator = { ... },
    firstPageErrorIndicator = { e -> // from setError
        ... e.message ...
        ... onRetry = { paginationState.retryLastFailedRequest() } ...
    },
    newPageErrorIndicator = { e -> ... },
    firstPageEmptyIndicator = { ... },
    newPageEmptyIndicator = { ... },
    ... // The rest of LazyRow params
) {
    itemsIndexed(
        paginationState.allItems!!, // safe to access here
    ) { _, item ->
        Item(value = item)
    }
}
```

</details>

### `PaginatedLazyVerticalGrid`: ###

<p>
    <img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExZjd3aXdsdWJyODhpY3Ayd2swYTFzYmxiejRiNTI1bzh5b2U2aTJoYSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/VW3CpfV7F5TmUXYRnA/giphy.gif" width="27%" align="top" />
    <img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExbTByb3Q3Mmc4dm9hc2Vtb3RhaWM2M25hejduYTEwNnplNGt3dG5pNCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/ozegucGVk85Le0h0AA/giphy.gif" width="25.62%" align="top" />
    <img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExOWZqMHUzMno3cGVleHN4ZXJ5Y3JudWowaGJqdjN6cGk1eW4xbHFjaiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Jeh0aYUdtRGPqGU2LW/giphy.gif" width="45.5%" align="top">
</p>

<details open>
    <summary> 
        <h3>Composable</h3> 
    </summary>

```kotlin
PaginatedLazyVerticalGrid(
    paginationState = paginationState,
    firstPageProgressIndicator = { ... },
    newPageProgressIndicator = { ... },
    firstPageErrorIndicator = { e -> // from setError
        ... e.message ...
        ... onRetry = { paginationState.retryLastFailedRequest() } ...
    },
    newPageErrorIndicator = { e -> ... },
    firstPageEmptyIndicator = { ... },
    newPageEmptyIndicator = { ... },
    ... // The rest of LazyVerticalGrid params
) {
    itemsIndexed(
        paginationState.allItems!!, // safe to access here
    ) { _, item ->
        Item(value = item)
    }
}
```

</details>

### `PaginatedLazyHorizontalGrid`: ###

<p>
    <img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExbTJ1dWhsMWVkcGpmdGFyOTN1aDFjenBuNTl0bnR4cDV3MjdiN3Z4ZiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/5hLbIxhmiHkiMU963B/giphy.gif" width="27%" align="top" />
    <img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExYzJ4dnllYW5ncWNnbmtycjIycnU4d3pkbmkzbTM3azZsYXJlZjRrNCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/YTCL5P2a7NSJXRASvU/giphy.gif" width="25.62%" align="top" />
    <img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExdGNhZGduaGg5ZTc5aXhianh3bXdmbHhwZHE4a2x5cmZteW9yanNvZCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/w5nCrcuOCCydr3lknl/giphy.gif" width="45.5%" align="top">
</p>

<details open>
    <summary> 
        <h3>Composable</h3> 
    </summary>

```kotlin
PaginatedLazyHorizontalGrid(
    paginationState = paginationState,
    firstPageProgressIndicator = { ... },
    newPageProgressIndicator = { ... },
    firstPageErrorIndicator = { e -> // from setError
        ... e.message ...
        ... onRetry = { paginationState.retryLastFailedRequest() } ...
    },
    newPageErrorIndicator = { e -> ... },
    firstPageEmptyIndicator = { ... },
    newPageEmptyIndicator = { ... },
    ... // The rest of LazyHorizontalGrid params
) {
    itemsIndexed(
        paginationState.allItems!!, // safe to access here
    ) { _, item ->
        Item(value = item)
    }
}
```

## More supported composables: `PaginatedLazyVerticalGrid` and `PaginatedLazyHorizontalGrid` ##

<details>
    <summary> 
        <h1>State in UI (no ViewModel)</h1> 
    </summary>
    
```kotlin
// Int is passed as the KEY which represents your pagination fetching strategy
// Int example here means the page number but could really by anything such as a cursor, time, etc...
@Composable
fun Content() {
    val paginationState = rememberPaginationState<Int, Model>(
        initialPageKey = 1,
        onRequestPage = { pageKey: Int ->
            scope.launch {
                try {
                    val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10)

                    appendPage(
                        items = page.items,
                        nextPageKey = page.nextPageNumber,
                        isLastPage = page.isLastPage
                    )
                } catch (e: Exception) {
                    setError(e)
                }
            }
        }
    )

    // Your paginated composable here
}
```

</details>
<details open>
    <summary> 
        <h1>State in a ViewModel (Recommended)</h1> 
    </summary>

```kotlin
class MyViewModel : ViewModel() {
    // Int is passed as the KEY which represents your pagination fetching strategy
    // Int example here means the page number but could really by anything such as a cursor, time, etc...
    val paginationState = PaginationState<Int, Model>(
       initialPageKey = 1,
       onRequestPage = { loadPage(it) }
    )
    
    fun loadPage(pageKey: Int) {
       viewModelScope.launch {
          try {
              val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10)
    
              paginationState.appendPage(
                  items = page.items,
                  isLastPage = page.isLastPage
              )
          } catch (e: Exception) {
              paginationState.setError(e)
          }
       }
    }
}

@Composable
fun Content(viewModel: MyViewModel) {
    val paginationState = viewModel.paginationState

    // Your Paginated composable here
}
```

</details>

# Setup #

Get the latest version via Maven Central: 

> (badge may not always be up to date, use the toml declaration for the latest release)

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.ahmad-hamwi/lazy-pagination-compose)

Add Maven Central repository to your root build.gradle at the end of repositories:

```kotlin
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

### For Compose Multiplatform Project ###

```toml
[versions]
lazy-pagination-compose = "1.7.1"

[libraries]
lazyPaginationCompose = { module = "io.github.ahmad-hamwi:lazy-pagination-compose", version.ref = "lazy-pagination-compose" }
```

```kotlin
// Compose Multiplatform
sourceSets {
    commonMain.dependencies {
        implementation(libs.lazyPaginationCompose)
    }
}
```

For an Android Project use `io.github.ahmad-hamwi:lazy-pagination-compose-android`

# Usage #

### Full sample can be found in the [sample module](https://github.com/Ahmad-Hamwi/lazy-pagination-compose/tree/main/sample) ###

## 1- Prepare your pagination state ##

### Create a `PaginationState` by remembering it in your composable or holding it in your ViewModel.

```kotlin
// Int is the key which in this example represents the page number
val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    ...
)
```

### Pass your `onRequestPage` callback when creating your `PaginationState` and call your data source ###

```kotlin
val scope = rememberCoroutineScope()

val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10,)
        }
    }
)
```

### Append data using `appendPage` and flag the end of your list using `isLastPage` ###
```kotlin
val scope = rememberCoroutineScope()

val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10,)

            appendPage(
                items = page.items,
                nextPageKey = page.nextPageNumber,
                isLastPage = page.isLastPage // optional, defaults to false
            )
        }
    }
}
```

### Handle errors using `setError` ###
```kotlin
val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            try {
                val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10,)

                appendPage(
                    items = page.items,
                    nextPageKey = page.nextPageNumber,
                    isLastPage = page.isLastPage
                )
            } catch (e: Exception) {
                setError(e)
            }
        }
    }
)
```

## 2- Define your paginated composable ##

It can either be `PaginatedLazyColumn` or `PaginatedLazyRow` or `PaginatedLazyVerticalGrid` or `PaginatedLazyHorizontalGrid`

```kotlin
@Composable
fun Content() {
    val paginationState = ... // either remembered here or in ViewModel
    
    // Or any other paginated composable
    PaginatedLazyColumn(
        paginationState = paginationState,
        firstPageProgressIndicator = { ... },
        newPageProgressIndicator = { ... },
        firstPageErrorIndicator = { e -> ... },
        newPageErrorIndicator = { e -> ... },
        firstPageEmptyIndicator = { ... },
        newPageEmptyIndicator = { ... },
    ) {
        itemsIndexed(
            paginationState.allItems!!, // safe to access here
        ) { _, item ->
            Item(value = item)
        }
    }
}
```

### Retrying your last failed request can be through `retryLastFailedRequest` ###
```kotlin
paginationState.retryLastFailedRequest()
```

### Refreshing can be through `refresh` method ###
```kotlin
paginationState.refresh(
    initialPageKey = 1 // optional, defaults to the value provided when creating the state
)
```

### More complex sample can be found in the [sample module](https://github.com/Ahmad-Hamwi/lazy-pagination-compose/tree/main/sample) ###

# Contributing #
This library is made to help other developers out in their app developments, feel free to contribute by suggesting ideas and creating issues and PRs that would make this repository more helpful.

# Honorable Mentions #
Thank you for the following contributors:
- [Tomislav Mladenov](https://github.com/TomislavMladenov): For adding support to the JS target.
- [Siarhei Luskanau](https://github.com/siarhei-luskanau): For keeping project dependencies up-to-date!

# Support #
You can show support by either contributing to the repository or by buying me a cup of coffee!

<p>
    <a href="https://www.buymeacoffee.com/ahmadhamwi" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" />
</p>

# License

Copyright (C) 2024 Ahmad Hamwi

Licensed under the MIT License
