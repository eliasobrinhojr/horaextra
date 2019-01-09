package br.com.componel.horaextra.extensions

import android.databinding.Observable
import android.databinding.ObservableField

fun <T> ObservableField<T>.toRx(): io.reactivex.Observable<T> {
    return io.reactivex.Observable.create { emitter ->
        val callback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (sender == this@toRx) {
                    emitter.onNext(this@toRx.get()!!)
                }
            }
        }
        this.addOnPropertyChangedCallback(callback)
        emitter.setCancellable { this.removeOnPropertyChangedCallback(callback) }
    }
}