package com.kaktux.dogedex.auth

import com.kaktux.dogedex.api.DogsApi
import com.kaktux.dogedex.api.dto.LoginDTO
import com.kaktux.dogedex.api.dto.SignUpDTO
import com.kaktux.dogedex.api.dto.UserDTOMapper
import com.kaktux.dogedex.api.makeNetworkCall
import com.kaktux.dogedex.api.responses.ApiResponseStatus
import com.kaktux.dogedex.model.User

class AuthRepository {

    suspend fun login(email: String, password: String): ApiResponseStatus<User> = makeNetworkCall {
        val loginDTO = LoginDTO(email, password)
        val loginResponse = DogsApi.retrofitService.login(loginDTO)
        if (!loginResponse.isSuccess) {
            throw Exception(loginResponse.message)
        }
        val userDTO = loginResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }

    suspend fun signUp(email: String, password: String, passwordConfirmation: String): ApiResponseStatus<User> = makeNetworkCall {
        val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)
        if (!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }
        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }
}
