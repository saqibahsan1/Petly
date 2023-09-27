package com.lcpetlylgmg.petly.admin.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.databinding.ActivityAdminLoginBinding

class AdminLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            buttonLogin.setOnClickListener {
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                if (email.isEmpty()) {
                    editTextEmail.error = getString(R.string.enter_email)
                    return@setOnClickListener
                }
                if (password.isEmpty()) {
                    editTextPassword.error = getString(R.string.enter_password)
                    return@setOnClickListener
                }
                if (email != "admin@petly.com" && password != "admin123") {
                    editTextEmail.error = getString(R.string.no_user_found)
                    return@setOnClickListener
                } else if (email == "admin@petly.com" && password == "admin123") {
                    startActivity(Intent(this@AdminLoginActivity, AdminActivity::class.java))
                    finish()
                }
            }

            buttonBack.setOnClickListener { finish() }
        }
    }
}