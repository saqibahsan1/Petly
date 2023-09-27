package com.lcpetlylgmg.petly.common.service.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.common.requests.data.RequestRepository
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModel
import com.lcpetlylgmg.petly.common.requests.data.RequestViewModelFactory
import com.lcpetlylgmg.petly.common.service.data.ServicesUrls
import com.lcpetlylgmg.petly.common.service.data.WebViewParams
import com.lcpetlylgmg.petly.databinding.ServicesFragmentBinding
import com.lcpetlylgmg.petly.utils.GlobalKeys

class ServicesFragment : Fragment() {

    private var _binding: ServicesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestViewModel: RequestViewModel
    lateinit var servicesUrls: ServicesUrls
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ServicesFragmentBinding.inflate(inflater, container, false)
        val repository = RequestRepository()
        requestViewModel =
            ViewModelProvider(
                this,
                RequestViewModelFactory(repository)
            )[RequestViewModel::class.java]

        requestViewModel.getServiceLinks()
        requestViewModel.servicesLinks.observe(viewLifecycleOwner) {
            servicesUrls = it
        }
        binding.apply {

            blog.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(GlobalKeys.WEB_DATA, WebViewParams(name = getString(R.string.blog), link = servicesUrls.blogLink))
                startActivity(intent)
            }
            petlyButton.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(GlobalKeys.WEB_DATA, WebViewParams(name = getString(R.string.petlyCommunity), link = servicesUrls.communityLink))
                startActivity(intent)
            }
            liabilityButton.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(GlobalKeys.WEB_DATA, WebViewParams(name = getString(R.string.liabilityInsurance), link = servicesUrls.insLinkOne))
                startActivity(intent)
            }
            surgicalButton.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(GlobalKeys.WEB_DATA, WebViewParams(name = getString(R.string.surgicalProtection), link = servicesUrls.insLinkTwo))
                startActivity(intent)
            }
            healthButton.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(GlobalKeys.WEB_DATA, WebViewParams(name = getString(R.string.healthInsurance), link = servicesUrls.insLinkThree))
                startActivity(intent)
            }
            shopButton.setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra(GlobalKeys.WEB_DATA, WebViewParams(name = getString(R.string.shop), link = servicesUrls.shopLink))
                startActivity(intent)
            }

        }
        return binding.root
    }
}