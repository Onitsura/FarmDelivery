package com.onitsura12.farmdel.fragments.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccOrdersBinding

class AccOrdersFragment : Fragment() {

    companion object {
        fun newInstance() = AccOrdersFragment()
    }

    private lateinit var viewModel: AccOrdersViewModel
    private lateinit var binding: FragmentAccOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccOrdersBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accOrdersBackButton.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }
    }

}