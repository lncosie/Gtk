package kt

/**
 * Created by guazi on 2016/4/24.
 */
interface Functor{
    infix fun<A, B> fmap(func: (A) -> B): List<B>
}
interface Monad:Functor{
    infix fun<A, B> times(func: (A) -> List<B>): List<B>
}