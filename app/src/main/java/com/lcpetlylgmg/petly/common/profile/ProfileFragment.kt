package com.lcpetlylgmg.petly.common.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.lcpetlylgmg.petly.databinding.FragmentProfileBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.ui.activities.MainActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var helper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        helper = PreferenceHelper.getPref(requireContext())

        binding.apply {
            helper.getCurrentUser()?.name.let {
                tvName.text = it
            }
            helper.getCurrentUser()?.email.let {
                tvEmail.text = it
            }
            helper.getCurrentUser()?.accountType.let {
                tvAccountType.text = it
            }

            logoutButton.setOnClickListener {
                helper.clearPreferences()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }

            deleteAccountButton.setOnClickListener {
                val user = FirebaseAuth.getInstance().currentUser

                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User account deleted successfully
                        PreferenceHelper.getPref(requireContext()).clearPreferences()
                        Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    } else {
                        // An error occurred while deleting the user account
                        Toast.makeText(
                            requireContext(),
                            "Account deletion failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}