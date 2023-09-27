package com.lcpetlylgmg.petly.organization.post.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.databinding.FragmentPostBinding
import com.lcpetlylgmg.petly.organization.post.data.PostAdapter
import com.lcpetlylgmg.petly.organization.post.data.PostRepository
import com.lcpetlylgmg.petly.organization.post.data.PostViewModel
import com.lcpetlylgmg.petly.organization.post.data.PostViewModelFactory
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.GlobalKeys

class PostFragment : Fragment(), PostAdapter.OnItemClickListener {

    private var _binding: FragmentPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = PostRepository()
        postViewModel =
            ViewModelProvider(this, PostViewModelFactory(repository))[PostViewModel::class.java]


        binding.apply {
            addButton.setOnClickListener {
                val intent = Intent(context, NewPostActivity::class.java)
                intent.putExtra(GlobalKeys.ACCOUNT_TYPE, GlobalKeys.ACTION_NEW)
                startActivity(intent)
            }
        }

        binding.laoder.visibility = View.VISIBLE

        context?.let { PreferenceHelper.getPref(it).getCurrentUser()?.userId.toString() }
            ?.let { postViewModel.getPostsByUserID(it) }

        initObserver()

    }

    private fun initObserver() {
        postViewModel.userPostsLiveData.observe(viewLifecycleOwner) {
            val adapter = PostAdapter(this)
            adapter.setList(it)
            binding.recyclerViewPost.adapter = adapter

            binding.noPostFound.visibility = View.GONE
            binding.laoder.visibility = View.GONE
        }
        postViewModel.userPostsLiveDataMessage.observe(viewLifecycleOwner) {
            binding.noPostFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let { PreferenceHelper.getPref(it).getCurrentUser()?.userId.toString() }
            ?.let { postViewModel.getPostsByUserID(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(post: Post) {
        val intent = Intent(requireContext(), UpdatePostActivity::class.java)
        intent.putExtra(GlobalKeys.POST, post)
        startActivity(intent)

    }
}