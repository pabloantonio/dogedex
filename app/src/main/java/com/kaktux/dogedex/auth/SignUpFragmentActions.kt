package com.kaktux.dogedex.auth

interface SignUpFragmentActions {

    fun onSignUpFieldsValidated(email: String, password: String, confirmPassword: String)
}
