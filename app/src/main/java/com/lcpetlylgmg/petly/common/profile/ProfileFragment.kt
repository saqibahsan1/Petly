package com.lcpetlylgmg.petly.common.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.databinding.FragmentProfileBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.ui.activities.LoginActivity
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

            updatePasswordButton.setOnClickListener {
                showCustomDialog()
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

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val body = dialog.findViewById(R.id.passwordEditText) as AppCompatEditText
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog.window?.setLayout(width,height)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val yesBtn = dialog.findViewById(R.id.updateButton) as AppCompatButton
        yesBtn.setOnClickListener {
            if (!body.text.isNullOrEmpty()) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.updatePassword(body.text.toString())
                    ?.addOnCompleteListener { task ->
                        dialog.dismiss()
                        if (task.isSuccessful) {
                            showAlertDialog(requireContext(), this.getString(R.string.passwordUpdatedSuccessfully), "")
                        } else
                            showAlertDialog(requireContext(), task.exception?.message.toString(), "Error")

                    }
            }
        }

        val noBtn = dialog.findViewById(R.id.cancel_button) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun showAlertDialog(context: Context, message: String, title: String) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}