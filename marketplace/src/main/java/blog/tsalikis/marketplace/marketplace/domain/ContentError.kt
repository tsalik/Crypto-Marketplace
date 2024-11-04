package blog.tsalikis.marketplace.marketplace.domain

sealed class ContentError<T> {
    data class Success<T>(val result: T) : ContentError<T>()
    data class Error<T>(val errorCase: ErrorCase): ContentError<T>()
}

enum class ErrorCase {
    Timeout,
    Connectivity,
    Generic,
    Limit
}