package com.lcpetlylgmg.petly.adopt.match.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.adopt.AdoptHomeActivity
import com.lcpetlylgmg.petly.adopt.match.data.Match
import com.lcpetlylgmg.petly.adopt.match.data.MatchAdapter
import com.lcpetlylgmg.petly.adopt.match.data.StackLayoutManager
import com.lcpetlylgmg.petly.common.requests.data.Request
import com.lcpetlylgmg.petly.common.requests.data.RequestRepository
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModel
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModelFactory
import com.lcpetlylgmg.petly.databinding.FragmentMatchBinding
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.user.data.User
import com.lcpetlylgmg.petly.user.data.UserRepository
import com.lcpetlylgmg.petly.user.data.UserViewModel
import com.lcpetlylgmg.petly.user.data.UserViewModelFactory
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.GlobalKeys


class MatchFragment : Fragment(), MatchAdapter.OnItemClickListener {
    private var _binding: FragmentMatchBinding? = null
    private lateinit var requestViewModel: RequestViewModel
    private lateinit var adapter: MatchAdapter
    private lateinit var user: User
    private lateinit var selectedModel: Match
    private lateinit var userViewModel: UserViewModel

    private var cachedPostList = mutableListOf<Post>()// Cached data


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val SELECTION_REQUEST_CODE = 123
    private var postMatched: Post? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val repository = RequestRepository()
        requestViewModel = ViewModelProvider(
            this,
            RequestViewModelFactory(repository)
        )[RequestViewModel::class.java]

        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userRepository = UserRepository()
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]

        user = PreferenceHelper.getPref(requireContext()).getCurrentUser()!!




        binding.apply {
            changeCharacter.setOnClickListener {
                openActivityToSelectCharacteristics()
            }
            buttonCharacter.setOnClickListener {
                openActivityToSelectCharacteristics()
            }

            binding.reloadData.setOnClickListener {
                if (::selectedModel.isInitialized) {
                    selectedModel.let { it1 ->
                        performSearch(
                            it1,
                            "Egal"
                        )
                    }
                }
            }

        }
        // Data has not been fetched, fetch it and observe LiveData


        return root
    }


    private fun sendRequestForDog(postMatched: Post) {
        val request = Request(
            postMatched.aboutDog,
            postMatched.ageRange,
            postMatched.breed,
            postMatched.city,
            postMatched.compatibleWithCats,
            postMatched.compatibleWithKids,
            postMatched.country,
            postMatched.age,
            postMatched.dogExperience,
            postMatched.dogGender,
            postMatched.dogName,
            postMatched.dogSize,
            postMatched.handicapDog,
            postMatched.postId,
            postMatched.imageUrl,
            PreferenceHelper.getPref(requireContext()).getCurrentUser()?.userId,
            PreferenceHelper.getPref(requireContext()).getCurrentUser()?.name,
            PreferenceHelper.getPref(requireContext()).getCurrentUser()?.accountType,
            Global.generateRandomUUID(),
            Global.getCurrentUnixTimestamp(),
            postMatched.postedBy,
            postMatched.postedByName,
            postMatched.state
        )

        requestViewModel.sendRequest(request) { success1, errorMessage1 ->
            if (success1) {
                requestViewModel.updateRequestedList(
                    request.requestFrom!!,
                    request.postId!!,
                    user
                )

            } else {
                binding.laoder.visibility = View.GONE
                showAlertDialog(
                    requireContext(),
                    getString(R.string.request_message_failed) + " " + errorMessage1,
                    success1
                )
            }
        }

    }

    fun showAlertDialog(context: Context, message: String, success: Boolean) {
        AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK") { dialog, _ ->
            if (success) {
                dialog.dismiss()
                startActivity(Intent(requireContext(), AdoptHomeActivity::class.java))
                activity?.finish()
            } else {
                dialog.dismiss()
            }
        }.show()
    }

    private fun openActivityToSelectCharacteristics() {
        val intent = Intent(activity, SetCharacteristicActivity::class.java)
        startActivityForResult(intent, SELECTION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedModel = data?.getParcelableExtra<Match>("selectedModel")!!
            performSearch(selectedModel, "Egal")
        }
    }

    private fun initRecyclerView(postList: List<Post>) {
        adapter = MatchAdapter(this)
        adapter.setList(postList)
        binding.cardRecyclerView.layoutManager = StackLayoutManager(requireContext())
        binding.cardRecyclerView.adapter = adapter
        binding.cardRecyclerView.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun performSearch(match: Match, string: String) {
        userViewModel.getUserData(user.userId!!) { userData, errorMessage ->
            if (userData != null) {
                binding.laoder.visibility = View.GONE
                PreferenceHelper.getPref(requireContext()).saveCurrentUser(userData)
                user = PreferenceHelper.getPref(requireContext()).getCurrentUser()!!
                searchSingleDog(match, string) { postList ->
                    if (postList.isNotEmpty()) {
                        cachedPostList.clear()
                        val requestedPostIds = userData.requestedPostIds
                        // Cache the data
                        for (post in postList) {
                            if (requestedPostIds != null) {
                                // Check if the post's postId is not in requestedPostIds and is not already in cachedPostList
                                if (!requestedPostIds.contains(post.postId!!) && !cachedPostList.any { it.postId == post.postId }) {
                                    cachedPostList.add(post)
                                }
                            } else
                                cachedPostList.add(post)
                        }
                        // Populate the RecyclerView
                        initRecyclerView(cachedPostList)
                    } else {
                        binding.noDataLayout.visibility = View.VISIBLE
                        binding.cardRecyclerView.visibility = View.GONE
                    }
                }
            }

        }
    }

    private fun searchSingleDog(match: Match, isMatter: String, callback: (List<Post>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection(GlobalKeys.POST_TABLE)
        var query: Query = collectionRef

        // Apply filtering conditions based on match and isMatter
        if (match.state?.isNotEmpty() == true) {
            query = query.whereArrayContainsAny("state", match.state!!)
        }
        if (match.compatibleWithCats != isMatter) {
            query = query.whereEqualTo("compatibleWithCats", match.compatibleWithCats)
        }
        if (match.compatibleWithKids != isMatter) {
            query = query.whereEqualTo("compatibleWithKids", match.compatibleWithKids)
        }
        if (match.dogExperience != isMatter) {
            query = query.whereEqualTo("dogExperience", match.dogExperience)
        }
        if (match.dogSize != isMatter) {
            query = query.whereEqualTo("dogSize", match.dogSize)
        }
        if (match.compatibleWithCats != isMatter) {
            query = query.whereEqualTo("compatibleWithCats", match.compatibleWithCats)
        }
        if (match.handicapDog != isMatter) {
            query = query.whereEqualTo("handicapDog", match.handicapDog)
        }

        // Execute the query
        val postList = mutableListOf<Post>()
        query.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val post = document.toObject(Post::class.java)
                post?.let { postList.add(it) }
            }
            // Call the callback with the result
            callback(postList)
        }.addOnFailureListener { exception ->
            // Handle any query failure here
        }
    }


    override fun onPause() {
        super.onPause()
    }


    override fun onImageClickListener(post: Post) {
        Toast.makeText(context, "post" + post.postId, Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MatchDetailsActivity::class.java)
        intent.putExtra(GlobalKeys.POST, post)
        startActivity(intent)
    }

    private var postCount = 0
    private var products = 0

    private suspend fun getMarketingProducts(){
        userViewModel.getProductsData { success, errorMessage1 ->
            if (success?.size!! > 0) {
                initRecyclerView(success)
            } else {
                binding.laoder.visibility = View.GONE
                showAlertDialog(
                    requireContext(),
                    getString(R.string.request_message_failed) + " " + errorMessage1,
                    false
                )
            }
        }

    }

    override fun onSentRequestClickListener(post: Post, position: Int) {
        postMatched = post
        postCount = postCount++
        sendRequestForDog(postMatched!!)

    }

    override fun onCancelClickListener() {

    }
}