package kt

/**
 * Created by guazi on 2016/4/24.
 */
class State<S, A>(var run: ((s: S) -> Pair<S, A>)) {
    companion object {
        fun<S, A> start(a: A) = State { s: S -> Pair(s, a) }
    }
    fun<B> bind(fn: (A) -> State<S, B>): State<S, B> {
        return State {
            s: S ->
            val new = run(s)
            val g = fn(new.second)
            g.run(new.first)
        }
    }
}


fun state() {
    val s = State.start<Int, Int>(1)
    s.bind {
        State<Int, String> {
            Pair(it+1, it.toString())
        }
    }.bind {
        i->State<Int,String> {
            it->
            Pair(2,i+"aa")
        }
    }.run(1)
}