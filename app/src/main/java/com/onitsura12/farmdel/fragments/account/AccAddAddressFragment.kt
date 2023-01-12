package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccAddAddressBinding

class AccAddAddressFragment : Fragment() {

    companion object {
        fun newInstance() = AccAddAddressFragment()
    }

    private lateinit var viewModel: AccAddAddressViewModel
    private lateinit var binding: FragmentAccAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccAddAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            accAddAddressBackButton.setOnClickListener {
                findNavController().navigate(R.id.accAddressFragment)
            }
            saveButton.setOnClickListener {
                //TODO add in RV address

                findNavController().navigate(R.id.accAddressFragment)
            }
        }
    }

}