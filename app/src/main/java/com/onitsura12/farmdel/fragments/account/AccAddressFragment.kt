package com.onitsura12.farmdel.fragments.account


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountAddressBinding
import com.onitsura12.farmdel.recyclerView.AddressAdapter

class AccAddressFragment : Fragment() {

    companion object {
        fun newInstance() = AccAddressFragment()
    }

    private val viewModel: AccAddressViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountAddressBinding
    private val adapter: AddressAdapter = AddressAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountAddressBinding.inflate(layoutInflater)

        parentFragmentManager.setFragmentResultListener(
            "newAddress", viewLifecycleOwner
        ) { _, bundle ->
            val newAddress = viewModel.convertToAddress(bundle.getStringArrayList("newAddress")!!)
            viewModel.addressList.value!!.add(newAddress)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addressList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        initRcView()

        binding.accAddressBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.accAddAddressButton.setOnClickListener {
            findNavController().navigate(R.id.accAddAddressFragment)
        }
    }


    private fun initRcView(){
        binding.addressRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRcView.adapter = adapter
    }










}