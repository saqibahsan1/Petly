package com.lcpetlylgmg.petly.common.requests.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lcpetlylgmg.petly.common.requests.data.Request
import com.lcpetlylgmg.petly.common.requests.data.RequestAdapter
import com.lcpetlylgmg.petly.common.requests.data.RequestRepository
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModel
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModelFactory
import com.lcpetlylgmg.petly.databinding.FragmentRequestBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.GlobalKeys

class RequestFragment : Fragment(), RequestAdapter.OnItemClickListener {

    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!


    private lateinit var requestViewModel: RequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = RequestRepository()
        requestViewModel =
            ViewModelProvider(
                this,
                RequestViewModelFactory(repository)
            )[RequestViewModel::class.java]




        binding.laoder.visibility = View.VISIBLE



        context?.let { PreferenceHelper.getPref(it).getCurrentUser()?.userId.toString() }
            ?.let {

                if (PreferenceHelper.getPref(requireContext())
                        .getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT || PreferenceHelper.getPref(
                        requireContext()
                    ).getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT
                ) {
                    requestViewModel.getRequestsByUserID(it, GlobalKeys.REQUEST_TYPE_FROM)
                } else {
                    requestViewModel.getRequestsByUserID(it, GlobalKeys.REQUEST_TYPE_TO)
                }
            }

        initObserver()

    }

    private fun initObserver() {
        requestViewModel.userRequestLiveData.observe(viewLifecycleOwner) {
            val adapter = RequestAdapter(this)
            adapter.setList(it)
            binding.recyclerViewPost.adapter = adapter

            binding.noPostFound.visibility = View.GONE
            binding.laoder.visibility = View.GONE
        }
        requestViewModel.userRequestLiveDataMessage.observe(viewLifecycleOwner) {
            binding.laoder.visibility = View.GONE
            binding.noPostFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let { PreferenceHelper.getPref(it).getCurrentUser()?.userId.toString() }
            ?.let {

                if (PreferenceHelper.getPref(requireContext())
                        .getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT || PreferenceHelper.getPref(
                        requireContext()
                    ).getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT
                ){
                    requestViewModel.getRequestsByUserID(it, GlobalKeys.REQUEST_TYPE_FROM)
                } else {
                    requestViewModel.getRequestsByUserID(it, GlobalKeys.REQUEST_TYPE_TO)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(request: Request) {
        val intent = Intent(requireContext(), RequestDetailsActivity::class.java)
        intent.putExtra(GlobalKeys.REQUEST, request)
        startActivity(intent)
    }
}