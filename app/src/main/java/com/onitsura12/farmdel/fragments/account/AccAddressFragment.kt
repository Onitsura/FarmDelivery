package com.onitsura12.farmdel.fragments.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountAddressBinding

class AccAddressFragment : Fragment() {

    companion object {
        fun newInstance() = AccAddressFragment()
    }

    private lateinit var viewModel: AccAddressViewModel
    private lateinit var binding: FragmentAccountAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accAddressBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.accAddAddressButton.setOnClickListener {
            findNavController().navigate(R.id.accAddAddressFragment)
        }
    }

}