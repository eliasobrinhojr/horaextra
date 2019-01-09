package br.com.componel.horaextra.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.componel.horaextra.helpers.enums.RunMode
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {
    val disposer = CompositeDisposable()

    @Inject
    lateinit var runMode: RunMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (runMode == RunMode.NORMAL) sincronizaDados()
    }

    open fun sincronizaDados() {}

    override fun onDestroy() {
        disposer.dispose()
        super.onDestroy()
    }
}