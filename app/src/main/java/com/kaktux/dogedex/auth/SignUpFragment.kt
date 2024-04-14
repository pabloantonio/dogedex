package com.kaktux.dogedex.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaktux.dogedex.R
import com.kaktux.dogedex.databinding.FragmentSignUpBinding
import com.kaktux.dogedex.utils.isValidEmail

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private lateinit var fragmentActions: SignUpFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActions = try {
            context as SignUpFragmentActions
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
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        setUpSignUpButton()
        return binding.root
    }

    private fun setUpSignUpButton() {
        binding.signUpButton.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""

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

        val passwordConfirmation = binding.confirmPasswordEdit.text.toString()
        if (passwordConfirmation.isEmpty()) {
            binding.confirmPasswordEdit.error = getString(R.string.password_not_empty)
            return
        }

        if (password != passwordConfirmation) {
            binding.passwordInput.error = getString(R.string.password_not_matches)
            return
        }
        fragmentActions.onSignUpFieldsValidated(email, password, passwordConfirmation)
    }
}
