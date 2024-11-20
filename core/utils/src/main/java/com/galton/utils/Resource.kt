package com.galton.utils

import android.os.Looper
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

@Keep
data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String = "",
    var handled: Boolean? = null,
    var pos: Int? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        NETWORK_DISCONNECTED,
        LOADING
    }

    fun isError(): Boolean {
        return status == Status.ERROR
    }

    fun isLoading(): Boolean {
        return status == Status.LOADING
    }

    fun isSuccess(): Boolean {
        return status == Status.SUCCESS
    }

    fun isNotHandled(): Boolean = handled != true

    fun isHandled(): Boolean = handled ?: false

    companion object {
        fun <T> success(data: T? = null, message: String = "", pos: Int? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, message, pos = pos)
        }

        @JvmOverloads
        fun <T> error(data: T? = null, message: String = ""): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> networkError(data: T? = null): Resource<T> {
            return Resource(Status.NETWORK_DISCONNECTED, data)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data)
        }
    }
}

fun <T> Resource<T>?.toLoading(handled: Boolean? = null, pos: Int? = null): Resource<T> {
    return this?.copy(status = Resource.Status.LOADING, handled = handled, pos = pos, data = data)
        ?: Resource.loading()
}

fun <T> Resource<T>?.toError(
    data: T? = null,
    message: String = "",
    handled: Boolean? = null,
    pos: Int? = null
): Resource<T> {
    return this?.copy(
        status = Resource.Status.ERROR,
        message = message,
        handled = handled,
        pos = pos,
        data = data ?: this.data
    )
        ?: Resource.error(data, message)
}

fun <T> Resource<T>?.toNetworkError(data: T? = null, message: String = ""): Resource<T> {
    return this?.copy(status = Resource.Status.NETWORK_DISCONNECTED, message = message, handled = handled, pos = null)
        ?: Resource.networkError(data)
}

fun <T> Resource<T>?.toSuccess(data: T? = null, message: String = "", pos: Int? = null): Resource<T> {
    return this?.copy(data = data ?: this.data, status = Resource.Status.SUCCESS, message = message, pos = pos)
        ?: Resource.success(data)
}

fun <T> Resource<T>?.toSuccess(
    data: T? = null,
    message: String = "",
    pos: Int? = null,
    handled: Boolean?
): Resource<T> {
    return this?.copy(
        data = data ?: this.data,
        status = Resource.Status.SUCCESS,
        message = message,
        pos = pos,
        handled = handled
    )
        ?: Resource.success(data)
}

fun <T> Resource<T>?.toSuccess(message: String = "", pos: Int? = null): Resource<T> {
    return this?.copy(status = Resource.Status.SUCCESS, message = message, pos = pos)
        ?: Resource.success()
}

fun <T> Resource<T>?.updateData(data: T, message: String = "", pos: Int? = null): Resource<T> {
    return this?.copy(data = data, message = message, pos = pos)
        ?: Resource.success(data)
}

fun <T> Resource<T>?.updateData(data: T): Resource<T> {
    return this?.copy(data = data)
        ?: Resource.success(data)
}

fun <T> Resource<T>?.updateData(data: T, pos: Int): Resource<T> {
    return this?.copy(data = data, pos = pos)
        ?: Resource.success(data)
}

fun <T> MutableLiveData<Resource<T>>.toLoading(handled: Boolean? = null, pos: Int? = null) {
    postValue(value.toLoading(handled, pos))
}

fun <T> MutableLiveData<Resource<T>>.toSuccess(message: String = "", pos: Int? = null) {
    postValue(value.toSuccess(message, pos))
}

fun <T> MutableLiveData<Resource<T>>.toSuccess(data: T?, message: String = "", pos: Int? = null) {
    postValue(value.toSuccess(data, message, pos))
}

fun <T> MutableLiveData<Resource<T>>.toSuccess(data: T?, message: String = "", pos: Int? = null, handled: Boolean?) {
    postValue(value.toSuccess(data, message, pos, handled))
}

fun <T> MutableLiveData<Resource<T>?>.toReset() {
    postValue(null)
}

fun <T> MutableLiveData<Resource<T>>.toError(
    data: T? = null,
    message: String = "",
    handled: Boolean? = null,
    pos: Int? = null
) {
    postValue(value.toError(data, message, handled, pos))
}

fun <T> MutableLiveData<Resource<T>>.updateData(
    data: T,
    message: String = ""
) {
    postValue(value.updateData(data, message))
}

fun <T> MutableLiveData<Resource<T>>.toNetworkError(
    data: T? = null,
    message: String = ""
) {
    postValue(value.toNetworkError(data, message))
}

fun <T> MutableLiveData<T>.setLiveDataValue(value: T?) {
    try {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.value = value
        } else {
            this.postValue(value)
        }
    } catch (t: Throwable) {
        Timber.e(t, t.message)
    }
}
