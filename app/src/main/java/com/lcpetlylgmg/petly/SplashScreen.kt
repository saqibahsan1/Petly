package com.lcpetlylgmg.petly

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.lcpetlylgmg.petly.adopt.AdoptHomeActivity
import com.lcpetlylgmg.petly.databinding.ActivitySplashScreenBinding
import com.lcpetlylgmg.petly.organization.OrganizationHomeActivity
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.ui.activities.MainActivity
import com.lcpetlylgmg.petly.utils.GlobalKeys

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 2000 // 2 seconds
    private lateinit var preferenceHelper: PreferenceHelper
    private var isLoggedIn: Boolean = false
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceHelper = PreferenceHelper.getPref(this)
        isLoggedIn = preferenceHelper.isUserLogin()
        // Delay to simulate a loading period
        Handler(Looper.myLooper()!!).postDelayed({
            // Start the main activity after the delay
            if (isLoggedIn) {
                if (PreferenceHelper.getPref(this)
                        .getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT || PreferenceHelper.getPref(
                        this
                    ).getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT
                ) {
                    startActivity(Intent(this@SplashScreen, AdoptHomeActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashScreen, OrganizationHomeActivity::class.java))
                    finish()
                }
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_DELAY)
    }
}