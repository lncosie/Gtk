package kt

import rx.Observable
import rx.exceptions.Exceptions
import rx.functions.Action0
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2016/4/23.
 */
fun main(arg: Array<String>) {

    state();
    return
    val o = Observable.just(1,2,3,4)

    val s=o.flatMap {
        counter->Observable.create<String> {
            val worker = Schedulers.newThread().createWorker()
            it.add(worker)
            worker.schedule(object : Action0 {
                override fun call() {
                    try {
                        Thread.sleep(1000L)
                        it.onNext(counter.toString())
                    } catch (e: Throwable) {
                        try {
                            worker.unsubscribe()
                        } finally {
                            Exceptions.throwOrReport(e, it)
                        }
                    }

                }

            })
        }.observeOn(Schedulers.newThread())
    }.take(4).reduce{
        a,b->a+b
        println(a+b)
        a+b
    }.subscribeOn(Schedulers.newThread())
            .asObservable().toBlocking().subscribe()

    println("end")
    Thread.sleep(2000L)
}