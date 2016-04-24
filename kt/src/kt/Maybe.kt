package kt

import kotlin.concurrent.thread

/**
 * Created by guazi on 2016/4/24.
 */
open class AsynaAble<T>(){
    lateinit var fn:()->T
    fun call():T{
        return fn()
    }
}
sealed class Maybe<T>:AsynaAble<Maybe<T>>() {
    class Nothing<T> : Maybe<T>() {

    }

    class Just<T>(val value: T) : Maybe<T>() {

    }
}

infix fun<A, B> Maybe<A>.fmap(func: (A) -> B): Maybe<B> {
    return when (this) {
        is Maybe.Just<A> -> Maybe.Just<B>(func(value))
        is Maybe.Nothing<A> -> Maybe.Nothing<B>()
    }
}

operator infix fun<A, B> Maybe<A>.times(func: (A) -> Maybe<B>): Maybe<B> {
    return when (this) {
        is Maybe.Just<A> -> func(value)
        is Maybe.Nothing<A> -> Maybe.Nothing<B>()
    }
}

operator infix fun<A, B> Maybe<A>.div(func: () -> Maybe<B>): Maybe<B> {
    return func()
}

enum class ThreadMode{
    Ui,Io,Computing
}
inline operator infix fun<R,T:AsynaAble<R>> T.times(mode:ThreadMode):T{

    return this
}




fun craft() {
    val m = Maybe.Just<Int>(0)*ThreadMode.Ui
    var fn=fun()=Maybe.Just(1)
    m fmap { it.toString() }
    m *{
        Maybe.Just(it.toString())
    } * {
        Maybe.Just(it + "a")
    }/ {
        Maybe.Just(1)
    }

}
