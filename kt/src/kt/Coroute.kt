package kt

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by guazi on 2016/4/25.
 */
enum class Worker {
    Io, Ui
}

object Diapatcher {
    val pool= Executors.newCachedThreadPool();
    fun<A,B> dispatch(a: A, fn: (A) -> B): Future<B> {
        return pool.submit<B>({fn(a)})
    }

}

class Coroute<T : Any>(val worker: Worker? = null, protected val call: () -> T) {
    infix fun<B : Any> bind(op: (T) -> Coroute<B>): Coroute<B> {
        return Coroute<B> {
            val a = run()
            val b = if (worker != null)
                Diapatcher.dispatch(a, op as (Any) -> Any).get() as Coroute<B>
            else op(a)
            b.run()
        }
    }
    fun run(): T {
        return call()
    }
}

fun coroute() {


    val start = Coroute(Worker.Io) { 1 }
    val co = start bind {
        Coroute { println(it + 2) ;it.toString() }
    } bind {
        Coroute { println(it + 3);it }
    }
    co.run()
    println("end")
}