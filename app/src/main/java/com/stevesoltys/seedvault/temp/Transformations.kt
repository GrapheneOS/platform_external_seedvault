package com.stevesoltys.seedvault.temp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

fun <X, Y> switchMap(
    source: LiveData<X>,
    switchMapFunction: androidx.arch.core.util.Function<X, LiveData<Y>>,
): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(source, object : Observer<X> {
        var mSource: LiveData<Y>? = null
        override fun onChanged(x: X) {

            val newLiveData: LiveData<Y> = switchMapFunction.apply(x)
            if (mSource === newLiveData) {
                return
            }
            if (mSource != null) {
                result.removeSource(mSource!!)
            }
            mSource = newLiveData
            result.addSource(newLiveData) { y -> result.setValue(y) }
        }
    })
    return result
}
