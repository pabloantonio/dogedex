package com.kaktux.dogedex.api.dto

import com.kaktux.dogedex.model.User

class UserDTOMapper {

    fun fromUserDTOToUserDomain(userDTO: UserDTO) = User(userDTO.id, userDTO.email, userDTO.autheticationToken)
}
