package com.kaktux.dogedex.doglist

import com.kaktux.dogedex.R
import com.kaktux.dogedex.api.DogsApi.retrofitService
import com.kaktux.dogedex.api.dto.AddDogToUserDTO
import com.kaktux.dogedex.api.dto.DogDTOMapper
import com.kaktux.dogedex.api.makeNetworkCall
import com.kaktux.dogedex.api.responses.ApiResponseStatus
import com.kaktux.dogedex.model.Dog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DogRepository {

    /**
     *  este metodo reaqlizara la descarga del listado de los perros
     *  Se coloca suspend porque esta siendo invocado por uns couroutine
     *  Cuando se emplea para el descargar de internet esta CR , se emplea
     *  el withContext empleando el dispatcher IO
     *
     */
    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getAllDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserDTO(dogId)
        val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)
        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }

    suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getUserDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(Dispatchers.IO) {
            /*
               para que se ejecuten al mismo tiempo
             */
            val allDogsListDeferred = async { downloadDogs() }
            val userDogsListDeferred = async { getUserDogs() }

            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogsListDeferred.await()

            if (allDogsListResponse is ApiResponseStatus.Error) {
                allDogsListResponse
            } else if (userDogsListResponse is ApiResponseStatus.Error) {
                userDogsListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Success && userDogsListResponse is ApiResponseStatus.Success) {
                ApiResponseStatus.Success(getCollectionList(allDogsListResponse.data, userDogsListResponse.data))
            } else {
                ApiResponseStatus.Error(R.string.error_general)
            }
        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>) = allDogList.map {
        if (userDogList.contains(it)) {
            it
        } else {
            Dog(0, it.index, "", "", "", "", "", "", "", "", "", false)
        }
    }.sorted()
}
