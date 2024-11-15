package com.galton.utils

import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * to try catch exceptions a block of code and report
 * @return true if run successfully or false if failed
 */
fun tryRun(error: ((t: Throwable) -> Unit)? = null, block: () -> Unit): Boolean {
    try {
        block.invoke()
        return true
    } catch (t: Throwable) {
        error?.invoke(t)
        Timber.e(t, t.message)
    }
    return false
}

/**
 * to try catch exceptions a block of code and return and report
 * @return true if run successfully or false if failed
 */
fun <T> tryRun(error: ((t: Throwable) -> T)? = null, block: () -> T): T? {
    return try {
        block.invoke()
    } catch (t: Throwable) {
        Timber.e(t, t.message)
        error?.invoke(t)
    }
}

fun <R> tryCatch(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (t: Throwable) {
        Timber.e(t, t.message)
        Result.failure(t)
    }
}

/**
 * accept an array of permissions and return only the ones that are not granted yet,
 * if the returned array is empty it means no need to ask for any permission
 */
fun Fragment.checkSelfPermission(permissionArray: Array<String>): Array<String> {
    val permissionList = mutableListOf<String>()
    permissionArray.forEach { p ->
        context?.let {
            if (PermissionChecker.checkSelfPermission(it, p) != PermissionChecker.PERMISSION_GRANTED)
                permissionList.add(p)
        }
    }
    return permissionList.toTypedArray()
}

fun CoroutineScope.launchSafe(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    // error in async caught here
    val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t, t.message)
    }
    launch(context + exceptionHandler, start, block)
}

/**
 * delay for unit testing only!
 * it will allow an actually delay when used inside runBlockingTest (bc runBlockingTest auto-advances virtual time)
 */
fun CoroutineScope.launchDelay(timeMillis: Long) {
    launch { delay(timeMillis) }
}

fun <T> MutableLiveData<T>.notifyChange() {
    postValue(value)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T> MutableList<T>.replaceOrInsertItem(newItem: T, predicate: (T) -> Boolean): MutableList<T> {
    val index = indexOfFirst(predicate)
    if (index != -1) {
        this[index] = newItem
    } else {
        add(newItem)
    }
    return this
}