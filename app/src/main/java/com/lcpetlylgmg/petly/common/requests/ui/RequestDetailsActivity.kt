package com.lcpetlylgmg.petly.common.requests.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.adopt.AdoptHomeActivity
import com.lcpetlylgmg.petly.common.chat.data.ChatViewModel
import com.lcpetlylgmg.petly.common.chat.ui.ChatActivity
import com.lcpetlylgmg.petly.common.requests.data.Request
import com.lcpetlylgmg.petly.common.requests.data.RequestRepository
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModel
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModelFactory
import com.lcpetlylgmg.petly.databinding.ActivityRequestDetailsBinding
import com.lcpetlylgmg.petly.organization.OrganizationHomeActivity
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.GlobalKeys

class RequestDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestDetailsBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelRequest: RequestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        val repository = RequestRepository()
        viewModelRequest = ViewModelProvider(
            this,
            RequestViewModelFactory(repository)
        )[RequestViewModel::class.java]
        val request = intent.getParcelableExtra<Request>(GlobalKeys.REQUEST)

        if (request != null) {
            binding.apply {
                request.dogName.let {
                    name.text = it
                }
                request.breed.let {
                    breed.text = it
                }
                request.dogAge.let {
                    age.text = it
                }
                request.dogExperience.let {
                    dogExperience.text = it
                }
                request.dogGender.let {
                    dogGender.text = it
                }
                request.dogSize.let {
                    dogSize.text = it
                }
                request.about.let {
                    dogAbout.text = it
                }
                request.country.let {
                    dogCountry.text = it
                }
                request.ageRange.let {
                    ageRange.text = it
                }
                request.compatibleWithCats.let {
                    catExperience.text = it
                }
                request.compatibleWithKids.let {
                    childExperience.text = it
                }
                request.handicapDog.let {
                    handicapDog.text = it
                }
                request.city.let {
                    dogCity.text = it
                }
                request.state.let {
                    if (it != null) {
                        for (item in it) {
                            dogStates.append(item)
                        }
                    }
                }
                Glide.with(this@RequestDetailsActivity).load(request.postImgUrl).into(image)


                cancelButton.setOnClickListener {
                    request.requestId?.let {
                        viewModelRequest.cancelRequest(it) { success, error ->
                            if (success) {
                                showAlertDialog(
                                    this@RequestDetailsActivity,
                                    getString(R.string.request_message_success),
                                    success
                                )
                            }
                        }
                    }
                }

                messageButton.setOnClickListener {

                    viewModel.checkChatThreadExists(
                        request.requestFrom.toString(),
                        request.requestTo.toString(), request.postId.toString()
                    ) { chatThreadExists, chatThread, exception ->
                        if (exception == null) {
                            if (chatThreadExists) {
                                val intent =
                                    Intent(this@RequestDetailsActivity, ChatActivity::class.java)
                                PreferenceHelper.getPref(this@RequestDetailsActivity)
                                    .saveStringValue("chat", "n")
                                intent.putExtra(
                                    GlobalKeys.THREAD_ID, chatThread?.threadId
                                )
                                intent.putExtra(GlobalKeys.REQUEST, request)
                                startActivity(intent)
                            } else {
                                val intent =
                                    Intent(this@RequestDetailsActivity, ChatActivity::class.java)
                                PreferenceHelper.getPref(this@RequestDetailsActivity)
                                    .saveStringValue("chat", "n")
                                intent.putExtra(
                                    GlobalKeys.THREAD_ID,
                                    request.requestFrom + "_" + Global.generateRandomUUID()
                                )
                                intent.putExtra(GlobalKeys.REQUEST, request)
                                startActivity(intent)
                            }
                        } else {

                        }
                    }

                }

                buttonBack.setOnClickListener { finish() }
            }
        }
    }

    fun showAlertDialog(context: Context, message: String, success: Boolean) {
        AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK") { dialog, _ ->
            if (success) {
                dialog.dismiss()
                if (PreferenceHelper.getPref(this)
                        .getCurrentUser()?.accountType.equals(GlobalKeys.ACCOUNT_TYPE_ADOPT) || PreferenceHelper.getPref(
                        this
                    ).getCurrentUser()?.accountType.equals(GlobalKeys.ACCOUNT_TYPE_AGENT)
                ) {
                    startActivity(Intent(this, AdoptHomeActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, OrganizationHomeActivity::class.java))
                    finish()
                }

            } else {
                dialog.dismiss()
            }
        }.show()
    }

}