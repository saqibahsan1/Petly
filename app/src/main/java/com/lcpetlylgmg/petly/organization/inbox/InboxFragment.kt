package com.lcpetlylgmg.petly.organization.inbox

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.common.chat.ui.ChatActivity
import com.lcpetlylgmg.petly.common.chat.data.ChatThread
import com.lcpetlylgmg.petly.common.chat.data.ChatViewModel
import com.lcpetlylgmg.petly.databinding.FragmentInboxBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.GlobalKeys


class InboxFragment : Fragment(), InboxAdapter.OnItemClickListener {

    private var _binding: FragmentInboxBinding? = null
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: InboxAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        val root: View = binding.root


        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]


        viewModel.getChatsByUserId(
            PreferenceHelper.getPref(requireContext()).getCurrentUser()?.userId.toString()
        )

        viewModel.chatThreads.observe(viewLifecycleOwner, Observer { chatThreads ->
            if (chatThreads != null) {
                adapter = InboxAdapter(this)
                adapter.setList(chatThreads)
                binding.recyclerViewInbox.adapter = adapter
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(
        chatThread: ChatThread,
        position: Int,
        relativeLayout: RelativeLayout?
    ) {
        if (chatThread.isUserNewMessage) {
            var newIsUserNewMessage = !chatThread.isUserNewMessage
            chatThread.threadId.let { threadId ->
                val userRef = FirebaseFirestore.getInstance().collection(GlobalKeys.CHAT_TABLE)
                    .document(threadId)
                userRef.update("isUserNewMessage", newIsUserNewMessage)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update the user object in the dataset
                            chatThread.isUserNewMessage = newIsUserNewMessage
                            // Notify the adapter that this item has changed
                            adapter.notifyItemChanged(position)
                        }
                    }
            }
        }
        if (chatThread.isShelterNewMessage) {
            var newIsShelterNewMessage = !chatThread.isShelterNewMessage
            chatThread.threadId.let { threadId ->
                val userRef = FirebaseFirestore.getInstance().collection(GlobalKeys.CHAT_TABLE)
                    .document(threadId)
                userRef.update("isShelterNewMessage", newIsShelterNewMessage)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update the user object in the dataset
                            chatThread.isShelterNewMessage = newIsShelterNewMessage
                            // Notify the adapter that this item has changed
                            adapter.notifyItemChanged(position)
                        }
                    }
            }
        }

        var intent = Intent(requireContext(), ChatActivity::class.java)
        PreferenceHelper.getPref(requireContext()).saveStringValue("chat", "y")
        intent.putExtra(GlobalKeys.CHAT, chatThread)
        activity?.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChatsByUserId(
            PreferenceHelper.getPref(requireContext()).getCurrentUser()?.userId.toString()
        )
    }
}