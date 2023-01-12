package com.onitsura12.farmdel.fragments.cart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentConfirmOrderBinding

class ConfirmOrderFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmOrderFragment()
    }

    private lateinit var viewModel: ConfirmOrderViewModel
    private lateinit var binding: FragmentConfirmOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}