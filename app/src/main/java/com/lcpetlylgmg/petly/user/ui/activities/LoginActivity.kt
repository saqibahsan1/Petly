package com.lcpetlylgmg.petly.user.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.adopt.AdoptHomeActivity
import com.lcpetlylgmg.petly.databinding.ActivityLoginBinding
import com.lcpetlylgmg.petly.organization.OrganizationHomeActivity
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.data.UserRepository
import com.lcpetlylgmg.petly.user.data.UserViewModel
import com.lcpetlylgmg.petly.user.data.UserViewModelFactory
import com.lcpetlylgmg.petly.utils.GlobalKeys

class LoginActivity : AppCompatActivity() {
    private lateinit var helper: PreferenceHelper
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private var accountType: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        helper = PreferenceHelper.getPref(this)
        val userRepository = UserRepository()
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]
        accountType = helper.getStringValue(GlobalKeys.ACCOUNT_TYPE)
        type = helper.getStringValue(GlobalKeys.TYPE)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {


            buttonLogin.setOnClickListener {


                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()

                if (email.isEmpty()) {
                    editTextEmail.error = getString(R.string.enter_email)
                } else if (password.isEmpty()) {
                    editTextPassword.error = getString(R.string.enter_password)
                } else {
                    loginUser(email, password)
                }
            }

            registerButton.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
            buttonBack.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
        initObserver()
    }

    private fun initObserver() {
        userViewModel.loginResult.observe(this) {
            val (success, errorMessage) = it
            if (success) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                userViewModel.getUserData(userId) { userData, errorMessage ->
                    if (userData != null) {
                        binding.laoder.visibility = View.GONE
                        helper.saveCurrentUser(userData)
                        helper.setUserLogin(true)
                        saveToken()
                        if (userData.type == type) {
                            if (userData.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT) {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        AdoptHomeActivity::class.java
                                    )
                                )
                                finish()
                            } else if (userData.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT) {
                                if (userData.isApproved!!) {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            AdoptHomeActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    showAlertDialog(
                                        this@LoginActivity,
                                        getString(R.string.message_not_approved), "Congratulations!"
                                    )
                                }
                            } else {
                                if (userData.isApproved!!) {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            OrganizationHomeActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    showAlertDialog(
                                        this@LoginActivity,
                                        getString(R.string.message_not_approved), "Congratulations!"
                                    )
                                }
                            }
                        } else {
                            binding.laoder.visibility = View.GONE
                            Toast.makeText(
                                this,
                                getString(R.string.no_user_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.laoder.visibility = View.GONE
                        Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                binding.laoder.visibility = View.GONE
                Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginUser(
        email: String,
        password: String,
    ) {
        binding.laoder.visibility = View.VISIBLE
        userViewModel.loginUser(email, password)
    }


    private fun saveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            PreferenceHelper.getPref(applicationContext).getCurrentUser()?.userId?.let {
                userViewModel.saveFCMTokenForUser(it, token) { success, errorMessage ->
                    if (success) {
                        Toast.makeText(
                            applicationContext, "" + success.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
            // Log and toast
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

    fun showAlertDialog(context: Context, message: String, title: String) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }.show()

    }

}