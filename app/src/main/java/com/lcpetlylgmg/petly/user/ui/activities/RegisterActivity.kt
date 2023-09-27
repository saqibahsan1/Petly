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
import com.lcpetlylgmg.petly.databinding.ActivityRegisterBinding
import com.lcpetlylgmg.petly.organization.OrganizationHomeActivity
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.data.User
import com.lcpetlylgmg.petly.user.data.UserRepository
import com.lcpetlylgmg.petly.user.data.UserViewModel
import com.lcpetlylgmg.petly.user.data.UserViewModelFactory
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.GlobalKeys

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var helper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        helper = PreferenceHelper.getPref(this)
        val userRepository = UserRepository()
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accountType = helper.getStringValue(GlobalKeys.ACCOUNT_TYPE)
        val type = helper.getStringValue(GlobalKeys.TYPE)



        binding.apply {

            editTextAccountType.setText(accountType)

            registerButton.setOnClickListener {

                val createDate = Global.getCurrentUnixTimestamp()

                val name = editTextName.text.toString()
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                val confirmPass = editTextConfirmPassword.text.toString()

                if (name.isEmpty()) {
                    editTextName.error = getString(R.string.enter_name)
                    return@setOnClickListener
                } else if (email.isEmpty()) {
                    editTextEmail.error = getString(R.string.enter_email)
                } else if (password.isEmpty()) {
                    editTextPassword.error = getString(R.string.enter_password)
                } else if (password.isEmpty()) {
                    editTextConfirmPassword.error = getString(R.string.enter_confirm_password)
                } else if (password != confirmPass) {
                    editTextConfirmPassword.error = getString(R.string.password_not_match)
                } else {
                    registerUser(name, email, password, type, accountType, createDate)
                }
            }

            loginButton.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            buttonBack.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }

        initObserver()

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

    private fun initObserver() {
        userViewModel.registrationResult.observe(this) {
            val (success, errorMessage) = it
            if (success) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                userViewModel.getUserData(userId) { userData, errorMessage ->
                    if (userData != null) {

                        binding.laoder.visibility = View.GONE
                        helper.setUserLogin(true)
                        helper.saveCurrentUser(userData)
                        saveToken()
                        if (userData.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT) {
                            startActivity(
                                Intent(
                                    this@RegisterActivity,
                                    AdoptHomeActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            if (userData.isApproved!!) {
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        OrganizationHomeActivity::class.java
                                    )
                                )
                                finish()
                            } else {
                                showAlertDialog(
                                    this@RegisterActivity,
                                    getString(R.string.message_not_approved), "Congratulations!"
                                )
                            }
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

    private fun registerUser(
        name: String,
        email: String,
        password: String,
        type: String?,
        accountType: String?,
        createDate: Long
    ) {
        val user = User(
            accountType!!, createDate, email, "", false, name, emptyList(), "", type!!
        )
        binding.laoder.visibility = View.VISIBLE
        userViewModel.registerUser(email, password, user)
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