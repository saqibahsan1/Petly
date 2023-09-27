package com.lcpetlylgmg.petly.user.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lcpetlylgmg.petly.admin.ui.AdminLoginActivity
import com.lcpetlylgmg.petly.databinding.ActivityMainBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.GlobalKeys

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var helper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        helper = PreferenceHelper.getPref(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonToAdopt.setOnClickListener {
                selectUserType(GlobalKeys.ACCOUNT_TYPE_ADOPT, GlobalKeys.TYPE_USER)
            }
            buttonOrganization.setOnClickListener {
                selectUserType(GlobalKeys.ACCOUNT_TYPE_ORGANIZATION, GlobalKeys.TYPE_ORGANIZATION)
            }
            buttonShelter.setOnClickListener {
                selectUserType(GlobalKeys.ACCOUNT_TYPE_SHELTER, GlobalKeys.TYPE_SHELTER)
            }
            buttonAgent.setOnClickListener {
                selectUserType(GlobalKeys.ACCOUNT_TYPE_AGENT, GlobalKeys.TYPE_USER)
            }
            buttonAdmin.setOnClickListener {
                startActivity(Intent(this@MainActivity, AdminLoginActivity::class.java))
            }
        }

    }

    private fun selectUserType(accountType: String, type: String) {
        val intent = Intent(this, LoginActivity::class.java)
        helper.saveStringValue(GlobalKeys.ACCOUNT_TYPE, accountType)
        helper.saveStringValue(GlobalKeys.TYPE, type)
        startActivity(intent)
    }
}