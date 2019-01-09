package br.com.componel.horaextra.dagger

import android.app.Application
import android.os.StrictMode
import android.support.annotation.VisibleForTesting
import android.util.Log
import android.widget.Toast
import br.com.componel.horaextra.BuildConfig
import br.com.componel.horaextra.dagger.components.*
import br.com.componel.horaextra.dagger.modules.AppModule
import br.com.componel.horaextra.dagger.modules.DbModule
import br.com.componel.horaextra.dagger.modules.NetModule
import com.google.firebase.messaging.FirebaseMessaging
import com.pusher.pushnotifications.PushNotifications


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDagger()
        strictMode()


        PushNotifications.start(applicationContext, "7fe570a0-d561-4b3e-ba13-2111e678baa1")
        PushNotifications.subscribe("extra")

        FirebaseMessaging.getInstance().subscribeToTopic("extra")
                .addOnCompleteListener { task ->
                    var msg = "subscribed"
                    if (!task.isSuccessful) {
                        msg = "subscribe_failed"
                    }
                    Log.d("FirebaseMessaging", msg)

                }


//        getInstance().isAutoInitEnabled = true
//         val token = FirebaseInstanceId.getInstance().token
//         Log.d("TOKEN", "This is your Firebase token : " + token!!)
    }


    private fun initDagger() {

        val appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .dbModule(DbModule(this))
                .netModule(NetModule())
                .build()
        setComponent(appComponent)
    }

    private fun strictMode() {
        if (BuildConfig.DEBUG) {
            Log.d("Application", "Strict Mode")
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .penaltyDeath()
                            .build())
        }
    }

    fun setComponent(component: AppComponent) {
        appComponent = component
        viewModelComponent = appComponent.viewModelComponent()
        fragmentComponent = appComponent.fragmentComponent()
        actComponent = appComponent.activityComponent()
    }

    companion object {
        @VisibleForTesting
        lateinit var appComponent: AppComponent
        lateinit var actComponent: ActivityComponent
        lateinit var viewModelComponent: ViewModelComponent
        lateinit var fragmentComponent: FragmentComponent
    }
}