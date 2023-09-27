package com.lcpetlylgmg.petly.organization.feedback

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.databinding.FeedbackActivityBinding
import com.lcpetlylgmg.petly.organization.data.Feedback
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.organization.post.data.PostRepository
import com.lcpetlylgmg.petly.organization.post.data.PostViewModel
import com.lcpetlylgmg.petly.organization.post.data.PostViewModelFactory
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.GlobalKeys


class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: FeedbackActivityBinding
    private lateinit var postViewModel: PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FeedbackActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = PostRepository()
        postViewModel = ViewModelProvider(this,PostViewModelFactory(repository))[PostViewModel::class.java]

        val post = intent.getParcelableExtra<Post>(GlobalKeys.POST_DELETE)
        clickEvent(post)
    }

    private fun clickEvent(post: Post?){
        binding.buttonBack.setOnClickListener { finish() }
        binding.deleteButton.setOnClickListener {
            binding.laoder.visibility = View.VISIBLE
            if (binding.RGroup.checkedRadioButtonId == -1) {
                showAlertDialog(this, "Please select any reason", "Feedback")
            } else {
                val selectedId: Int = binding.RGroup.checkedRadioButtonId
                val selectedRadioButton = findViewById<View>(selectedId) as RadioButton
                val feedback = Feedback(
                    feedbackId =  Global.generateRandomUUID(),
                    deletedDate = Global.getCurrentUnixTimestamp(),
                    deletedBy = PreferenceHelper.getPref(this).getCurrentUser()?.userId,
                    dogName = post?.dogName,
                    postId = post?.postId,
                    deletedByName = PreferenceHelper.getPref(this).getCurrentUser()?.name,
                    reasonId = FeedbackValue[selectedRadioButton.text.toString()].intValue,
                    reason = selectedRadioButton.text.toString()
                )
                saveFeedBack(feedback,post?.postId.toString())
            }

        }
    }

    private fun saveFeedBack(feedback: Feedback,postId: String){
        postViewModel.sendFeedBack(feedback) { success, errorMessage ->
            binding.laoder.visibility = View.GONE
            if (success) {
                deletePost(postId)
            } else {
                showAlertDialog(
                    this,
                    getString(R.string.post_message_failed) + " " + errorMessage, "Failed"
                )
            }
        }
    }

    private fun deletePost(postId : String) {
        postViewModel.deletePost(postId) {
            if (it) {
                showAlertDialog(
                    this@FeedbackActivity,
                    getString(R.string.post_message_delete), "Deleted"
                )
            }
        }

    }

    private fun showAlertDialog(context: Context, message: String, title: String) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }.show()

    }
}

enum class FeedbackValue(val intValue: Int, val slug: String) {
    NONE(100, ""),
    ADOPTED(0, "Adopted"),
    SICKNESS(1, "Sickness"),
    DIED(2, "Died"),
    Other(2, "Other reason");

    companion object {
        operator fun get(reason: String): FeedbackValue =
            values().find { it.slug == reason } ?: NONE
    }
}