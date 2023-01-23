package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun list(listener: APIListener<List<TaskModel>>) {

        executeCall(remote.list(), listener)

    }


    fun listNext(listener: APIListener<List<TaskModel>>) {
        executeCall(remote.listNext(), listener)

    }


    fun listOverDue(listener: APIListener<List<TaskModel>>) {
        executeCall(remote.listOverdue(), listener)

    }

    fun create(task: TaskModel, listener: APIListener<Boolean>) {

        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)

    }


    fun delete(id: Int, listener: APIListener<Boolean>) {

        val call = remote.delete(id)

        executeCall(call, listener)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                handleResponse(response, listener)

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })

    }

}