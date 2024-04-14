package com.kaktux.dogedex.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaktux.dogedex.auth.LoginActivity
import com.kaktux.dogedex.databinding.ActivitySettingsBinding
import com.kaktux.dogedex.model.User

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpOnclickListeners()
    }

    private fun setUpOnclickListeners() {
        binding.logoutButton.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        User.logOutUser(this)
        val intent = Intent(this, LoginActivity::class.java)
        /**
         * estas banderas son utiles para las cuales se limpian las banderas
         */
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
