package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val databasse = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {

        private val cache = mutableMapOf<Int, String>()

        fun getDescription(id: Int): String {

            return cache[id] ?: ""
        }

        fun setDescription(id: Int, string: String) {

            cache[id] = string

        }

    }

    fun list(listener: APIListener<List<PriorityModel>>) {

        val call = remote.list()
        executeCall(remote.list(), listener)

    }

    fun list(): List<PriorityModel> {
        return databasse.list()
    }

    fun getDescription(id: Int): String {

        val cached = PriorityRepository.getDescription(id)

        return if (cached == "") {
            val description = databasse.getDescription(id)
            PriorityRepository.setDescription(id, description)
            description
        } else {

            cached

        }


    }


    fun save(list: List<PriorityModel>) {

        databasse.clear()
        databasse.save(list)

    }

}