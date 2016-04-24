package kt

/**
 * Created by guazi on 2016/4/24.
 */
sealed class List<T>{
    fun append(next:List<T>):List<T>{
        return next
    }
    class Empty<T> : List<T>() {

    }

    class Node<T>(val value: T, var next: List<T>) : List<T>() {

    }

    fun id(value: T) = Node<T>(value, Empty<T>())
}


infix fun<A, B> List<A>.fmap(func: (A) -> B): List<B> {
    return when (this) {
        is List.Node<A> -> List.Node<B>(func(value), next fmap func)
        is List.Empty<A> -> List.Empty<B>()
    }
}

operator infix fun<A, B> List<A>.times(func: (A) -> List<B>): List<B> {
    return when (this) {
        is List.Empty<A> -> List.Empty<B>()

        is List.Node<A> -> func(value).append(next.times(func))
    }
}

operator infix fun<A, B> List<A>.div(func: () -> List<B>): List<B> {
    return func()
}
