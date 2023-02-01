package com.onitsura12.farmdel.presentation.fragments.account


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountAddressBinding
import com.onitsura12.farmdel.domain.models.Address
import com.onitsura12.farmdel.presentation.recyclerView.AddressAdapter

class AccAddressFragment : Fragment() {


    private lateinit var viewModel: AccAddressViewModel
    private lateinit var binding: FragmentAccountAddressBinding
    private lateinit var adapter: AddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountAddressBinding.inflate(layoutInflater)
        viewModel = AccAddressViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRcView()

        binding.accAddressBackButton.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }
        binding.accAddAddressButton.setOnClickListener {
            findNavController().navigate(R.id.accAddAddressFragment)
        }
    }


    private fun initListener() {
        val markAddress: (address: Address) -> Unit = { viewModel.markAddress(address = it) }
        val removeAddress: (address: Address) -> Unit = { viewModel.removeAddress(address = it)}
        val editAddress: (address: Address) -> Unit = { editAddress(address = it)}
        adapter = AddressAdapter(markAddress = markAddress, remove = removeAddress, edit = editAddress)
        viewModel.addressList.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }

    }

    private fun initRcView() {
        initListener()
        binding.addressRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRcView.adapter = adapter
        val itemAnimator = binding.addressRcView.itemAnimator
        if (itemAnimator is DefaultItemAnimator){
            itemAnimator.supportsChangeAnimations = false
        }

    }

    //TODO Edit Address
    private fun editAddress(address: Address){
        val selectedAddress = address.id
        val bundle = Bundle()
        bundle.putString("selectedAddress", selectedAddress)
        setFragmentResult("selectedAddress", bundle)
    }


}