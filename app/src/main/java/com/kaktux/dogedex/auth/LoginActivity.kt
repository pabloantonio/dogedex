package com.kaktux.dogedex.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kaktux.dogedex.MainActivity
import com.kaktux.dogedex.R
import com.kaktux.dogedex.api.responses.ApiResponseStatus
import com.kaktux.dogedex.databinding.ActivityLoginBinding
import com.kaktux.dogedex.model.User

class LoginActivity : AppCompatActivity(), FragmentActions, SignUpFragmentActions {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loadingWheel = binding.loadingWheel
        loadingWheel.visibility = View.GONE
        authViewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Success -> loadingWheel.visibility = View.GONE
                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    showErrorDialog(status.message)
                }
            }
        }
        authViewModel.user.observe(this) { user ->
            if (user != null) {
                User.setLoggedInUser(this, user)
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showErrorDialog(messageId: Int) {
        AlertDialog.Builder(this).setTitle(R.string.error_general).setMessage(messageId).setPositiveButton(android.R.string.ok) { _, _ -> }.create()
            .show()
    }

    override fun onRegisterButtonClick() {
        // findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLoginFieldsValidated(email: String, password: String) {
        authViewModel.login(email, password)
    }

    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        authViewModel.signUp(email, password, confirmPassword)
    }
}
