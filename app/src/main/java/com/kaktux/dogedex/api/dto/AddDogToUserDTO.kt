package com.kaktux.dogedex.api.dto

import com.squareup.moshi.Json

class AddDogToUserDTO(@field:Json(name = "dog_id") val dogId: Long)
