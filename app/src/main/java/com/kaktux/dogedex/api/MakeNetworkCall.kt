package com.kaktux.dogedex.api

import com.kaktux.dogedex.R
import com.kaktux.dogedex.api.responses.ApiResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

private const val UNAUTHORIZARED_LOGIN_CODE = 401

suspend fun <T> makeNetworkCall(
    call: suspend () -> T,
): ApiResponseStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            ApiResponseStatus.Success(call())
        } catch (e: UnknownHostException) {
            ApiResponseStatus.Error(R.string.error_servidor_desconocido)
        } catch (e: HttpException) {
            val errorMessage = if (e.code() == UNAUTHORIZARED_LOGIN_CODE) {
                R.string.wrong_user_or_password
            } else {
                R.string.unknown_error
            }
            ApiResponseStatus.Error(errorMessage)
        } catch (e: Exception) {
            val errorMessages = when (e.message) {
                "sign_up_error" -> R.string.error_sign_up
                "sign_in_error" -> R.string.error_sign_in
                "user_already_exists" -> R.string.user_already_exists
                "error_adding_dog" -> R.string.error_adding_dog
                else -> R.string.unknown_error
            }
            ApiResponseStatus.Error(errorMessages)
        }
    }
}
