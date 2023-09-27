package com.lcpetlylgmg.petly.organization

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.databinding.ActivityOrganizationHomeBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.data.UserRepository
import com.lcpetlylgmg.petly.user.data.UserViewModel
import com.lcpetlylgmg.petly.user.data.UserViewModelFactory

class OrganizationHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationHomeBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrganizationHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = UserRepository()
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        saveToken()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_organization_home)
        navView.setupWithNavController(navController)
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
}