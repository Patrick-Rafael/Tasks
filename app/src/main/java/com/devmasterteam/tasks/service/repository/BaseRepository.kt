package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Connection

open class BaseRepository(val context: Context) {

    fun failResponse(str: String): String {

        return Gson().fromJson(str, String::class.java)
    }

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>) {

        if (response.code() == TaskConstants.HTTP.SUCCESS) {
            response.body()?.let { listener.onSuccess(it) }
        } else {
            listener.onFailure(failResponse(response.errorBody()!!.string()))
        }

    }

    fun <T> executeCall(call: Call<T>, listener: APIListener<T>) {

        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }


        })
    }

    fun isConnectionAvailable(): Boolean {

        var result = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNet = cm.activeNetwork ?: return false
        val netWorkCapabilites = cm.getNetworkCapabilities(activeNet) ?: return false

        result = when {
            netWorkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            netWorkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
        return result
    }

}