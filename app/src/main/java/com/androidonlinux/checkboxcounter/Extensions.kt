package com.androidonlinux.checkboxcounter

import androidx.lifecycle.MutableLiveData

/**
 * Force a value change (with its own value) so LiveData observers are notified.
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}