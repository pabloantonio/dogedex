package com.kaktux.dogedex.api

import com.kaktux.dogedex.api.dto.AddDogToUserDTO
import com.kaktux.dogedex.api.dto.LoginDTO
import com.kaktux.dogedex.api.dto.SignUpDTO
import com.kaktux.dogedex.api.responses.AuthApiResponse
import com.kaktux.dogedex.api.responses.DefaultResponse
import com.kaktux.dogedex.api.responses.DogListApiResponse
import com.kaktux.dogedex.utils.ADD_DOG_TO_USER_URL
import com.kaktux.dogedex.utils.BASE_URL
import com.kaktux.dogedex.utils.GET_ALL_DOGS
import com.kaktux.dogedex.utils.GET_USER_DOGS_URL
import com.kaktux.dogedex.utils.SIGN_IN_URL
import com.kaktux.dogedex.utils.SIGN_UP_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private val okHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(ApiServiceInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @POST(ADD_DOG_TO_USER_URL)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserDogs(): DogListApiResponse
}

object DogsApi {
    // ese lazy indica que no invoques el contenido hasta que sea invocado
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
