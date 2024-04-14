package com.kaktux.dogedex.auth

interface FragmentActions {

    fun onRegisterButtonClick()
    fun onLoginFieldsValidated(email: String, password: String)
}
