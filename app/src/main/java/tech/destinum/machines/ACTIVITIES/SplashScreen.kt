package tech.destinum.machines.ACTIVITIES

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import java.util.concurrent.TimeUnit

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.machines.R

class SplashScreen : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        super.onStart()

        disposable.add(Completable.timer(4, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "SplashScreen: ERROR", e) }
                .doOnComplete {
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .subscribe())
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onStop()
    }

    companion object {

        private val TAG = SplashScreen::class.java.simpleName
    }
}
