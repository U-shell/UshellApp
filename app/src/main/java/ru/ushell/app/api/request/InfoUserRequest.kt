package ru.ushell.app.api.request

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ushell.app.api.API.api
import ru.ushell.app.api.response.info.ResponseInfoGroup
import ru.ushell.app.api.response.info.ResponseInfoTeacher
import ru.ushell.app.models.DTO
import java.net.HttpURLConnection

interface InfoUserCallback {
    fun onInfoUserReceived(dto: DTO?)
}

fun userIsStudent(
     token: String?,
     callback: InfoUserCallback?
){
    val response = api.infoGroup( "Bearer $token")

    response.enqueue(object : Callback<ResponseInfoGroup>{
        override fun onResponse(
            call: Call<ResponseInfoGroup?>,
            response: Response<ResponseInfoGroup?>
        ) {
            when{
                response.code() == HttpURLConnection.HTTP_OK && response.body() != null ->{
                    val body: ResponseInfoGroup = response.body()!!

                    callback?.onInfoUserReceived(DTO(
                        nameGroup = body.title,
                        groupId = body.id
                    ))
                }
                else -> {
                    callback?.onInfoUserReceived(DTO())
                }
            }
        }

        override fun onFailure(
            call: Call<ResponseInfoGroup?>,
            t: Throwable
        ) {
            callback?.onInfoUserReceived(DTO())
        }

    })
}

fun userIsTeacher(
    token: String?,
    callback: InfoUserCallback?
){
    val response = api.infoTeacher( "Bearer $token")

    response.enqueue(object : Callback<ResponseInfoTeacher>{
        override fun onResponse(
            call: Call<ResponseInfoTeacher?>,
            response: Response<ResponseInfoTeacher?>
        ) {
            when{
                response.code() == HttpURLConnection.HTTP_OK && response.body() != null ->{
                    val body: ResponseInfoTeacher = response.body()!!

                    callback?.onInfoUserReceived(DTO(
                        academicStatus = body.academicStatus,
                        academicDegree = body.academicDegree
                    ))
                }
                else -> {
                    callback?.onInfoUserReceived(DTO())
                }
            }
        }

        override fun onFailure(
            call: Call<ResponseInfoTeacher?>,
            t: Throwable
        ) {
            TODO("Not yet implemented")
        }

    })
}