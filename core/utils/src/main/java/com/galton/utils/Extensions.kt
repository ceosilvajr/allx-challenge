package com.galton.utils

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.UnknownHostException
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun String?.removeWhiteSpaces(): String {
    return this?.replace("\\s".toRegex(), "") ?: kotlin.run { "" }
}

fun String?.isValidEmail(): Boolean {
    this?.let {
        val emailAddressPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailAddressPattern.matcher(this).matches()
    }
    return false
}

fun String?.isValidName(): Boolean {
    this?.let {
        val pattern = Pattern.compile("^[a-zA-Z]+[\\s]*([a-zA-Z]+[\\s]*)*\$")
        return pattern.matcher(this).matches()
    }
    return false
}

fun String?.isValidPassportNumber(): Boolean {
    this?.let {
        val passportPattern = Pattern.compile("^[a-zA-Z0-9]+([a-zA-Z0-9]+)*\$")
        return passportPattern.matcher(this).matches()
    }
    return false
}

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
        Log.d("tryRun", t.message, t)
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
        Log.d("tryRun", t.message, t)
        error?.invoke(t)
    }
}

fun <R> tryCatch(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (t: Throwable) {
        Log.d("tryRun", t.message, t)
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
        Log.d("tryRun", t.message, t)
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

fun AndroidViewModel.getApplicationContext(): Application {
    return getApplication<Application>()
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

fun Throwable.isNetworkError(): Boolean {
    return (this is NetWorkDisconnectedException) || (this is UnknownHostException)
}

fun Uri.replaceOrAddUriParameter(key: String, newValue: String, addNewValue: Boolean = false): Uri {
    val params = queryParameterNames
    val newUri = buildUpon().clearQuery()
    var addNewVal = addNewValue
    params.forEach {
        newUri.appendQueryParameter(
            it,
            if (it == key) newValue else getQueryParameter(it)
        )
        if (it == key) addNewVal = false
    }
    if (addNewVal)
        newUri.appendQueryParameter(key, newValue)
    return newUri.build()
}

fun List<String>.takeUnique(
    count: Int = -1,
    fromFirst: Boolean = true,
    ignoreCase: Boolean = false
): List<String> {
    val take = if (count > 0) count else size
    val isTakeAll = take == size
    val results = mutableListOf<String>().apply {
        addAll(this@takeUnique.filter { it.isNotBlank() })
    }

    return if (ignoreCase) {
        if (isTakeAll) {
            results.distinctBy { it.uppercase() }
        } else if (fromFirst) {
            results.distinctBy { it.uppercase() }.take(take)
        } else {
            results.distinctBy { it.uppercase() }.takeLast(take)
        }
    } else {
        if (isTakeAll) {
            results.distinct()
        } else if (fromFirst) {
            results.distinct().take(take)
        } else {
            results.distinct().takeLast(take)
        }
    }
}

fun <T> JSONArray.asList(): List<T>? {
    return try {
        (0 until length()).asSequence().map { get(it) as T }.toList()
    } catch (e: Exception) {
        null
    }
}