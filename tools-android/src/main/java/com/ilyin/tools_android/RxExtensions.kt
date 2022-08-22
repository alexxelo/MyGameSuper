package com.ilyin.tools_android

import android.util.SparseArray
import androidx.core.util.forEach
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.track(trackable: RxTrackable): Disposable {
  return trackable.track(this)
}

fun Disposable.trackReplace(subscriptionTag: String, trackable: RxTrackable): Disposable {
  return trackable.track(subscriptionTag, this)
}

interface RxTrackable {

  val compositeDisposable: CompositeDisposable
  val taggedDisposables: SparseArray<Disposable>

  fun track(disposable: Disposable): Disposable {
    compositeDisposable.add(disposable)
    return disposable
  }

  fun track(subscriptionTag: String, disposable: Disposable): Disposable {
    val tagInt = subscriptionTag.hashCode()
    val prev = taggedDisposables.get(tagInt)
    taggedDisposables.put(tagInt, disposable)
    prev?.let(compositeDisposable::remove)
    compositeDisposable.add(disposable)
    return disposable
  }

  fun dispose(subscriptionTag: String) {
    val tagInt = subscriptionTag.hashCode()
    val prev = taggedDisposables.get(tagInt)
    prev?.let(compositeDisposable::remove)
    taggedDisposables.remove(tagInt)
  }

  fun Disposable.connect() {
    track(this)
  }

  fun Disposable.connect(subscriptionTag: String) {
    track(subscriptionTag, this)
  }

  fun rxRelease() {
    compositeDisposable.clear()
    taggedDisposables.forEach { _, value -> value.dispose() }
    taggedDisposables.clear()
  }
}

class RxTrackableDelegate : RxTrackable {
  override val compositeDisposable by lazy(::CompositeDisposable)
  override val taggedDisposables by lazy { SparseArray<Disposable>() }

}