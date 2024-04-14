package com.kaktux.dogedex.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaktux.dogedex.R
import com.kaktux.dogedex.databinding.FragmentLoginBinding
import com.kaktux.dogedex.utils.isValidEmail

class LoginFragment : Fragment() {

    private lateinit var fragmentActions: FragmentActions
    private lateinit var binding: FragmentLoginBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActions = try {
            context as FragmentActions
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException("$context must implement FragmentAction")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.loginRegisterButton.setOnClickListener {
            fragmentActions.onRegisterButtonClick()
        }
        binding.loginButton.setOnClickListener {
            validateFields()
        }
        return binding.root
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""

        val email = binding.emailEdit.text.toString()
        Log.i("GLOB", "EMAIL IS $email")
        if (!isValidEmail(email)) {
            binding.emailInput.error = getString(R.string.email_not_valid)
            return
        }

        val password = binding.passwordEdit.text.toString()
        if (password.isEmpty()) {
            binding.passwordInput.error = getString(R.string.password_not_empty)
            return
        }
        fragmentActions.onLoginFieldsValidated(email, password)
    }
}
