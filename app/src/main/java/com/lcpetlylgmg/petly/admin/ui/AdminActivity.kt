package com.lcpetlylgmg.petly.admin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lcpetlylgmg.petly.admin.data.UserAdminListAdapter
import com.lcpetlylgmg.petly.databinding.ActivityAdminBinding
import com.lcpetlylgmg.petly.user.data.UserRepository
import com.lcpetlylgmg.petly.user.data.UserViewModel
import com.lcpetlylgmg.petly.user.data.UserViewModelFactory

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdminListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = UserRepository()
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(repository))[UserViewModel::class.java]

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.laoder.visibility = View.VISIBLE
        userViewModel.getUsers()

        initObserver()
    }

    private fun initObserver() {
        userViewModel.getUsers().observe(this) { userList ->
            if (userList != null) {
                adapter = UserAdminListAdapter(this@AdminActivity)
                adapter.setList(userList)
                binding.recyclerViewAdmin.adapter = adapter
                binding.laoder.visibility = View.GONE
            } else {
                binding.laoder.visibility = View.GONE
            }
        }

    }
}