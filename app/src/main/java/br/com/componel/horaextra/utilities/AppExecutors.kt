package br.com.componel.horaextra.utilities

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers


/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun runOnIoThread(f: () -> Unit) {
    Completable.fromAction(f)
        .subscribeOn(Schedulers.io())
        .subscribe()
}

fun runOnComputationalThread(f: () -> Unit) {
    Completable.fromAction(f)
        .subscribeOn(Schedulers.computation())
        .subscribe()
}