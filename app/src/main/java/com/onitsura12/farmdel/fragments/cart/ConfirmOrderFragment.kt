package com.onitsura12.farmdel.fragments.cart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentConfirmOrderBinding
import com.onitsura12.farmdel.recyclerView.AddressAdapter

class ConfirmOrderFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmOrderFragment()
    }


    private lateinit var viewModel: ConfirmOrderViewModel
    private lateinit var binding: FragmentConfirmOrderBinding
    private val adapter: AddressAdapter = AddressAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmOrderBinding.inflate(layoutInflater)
        viewModel = ConfirmOrderViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initRcView()

    }


    private fun initRcView() {
        binding.addressRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRcView.adapter = adapter
        viewModel.addressList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initButtons(){
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
    }
}