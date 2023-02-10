package com.onitsura12.farmdel.presentation.fragments.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentConfirmOrderBinding
import com.onitsura12.farmdel.domain.models.Address
import com.onitsura12.farmdel.presentation.recyclerView.AddressAdapter
import com.onitsura12.farmdel.presentation.recyclerView.OrderItemAdapter

class ConfirmOrderFragment : Fragment() {



    private lateinit var viewModel: ConfirmOrderViewModel
    private lateinit var binding: FragmentConfirmOrderBinding
    private lateinit var addressAdapter: AddressAdapter
    private val cartAdapter: OrderItemAdapter = OrderItemAdapter(root = null, click = null,
        clickRemove = null, viewType = null, clickAdditional = null)

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

        viewModel.totalCost.observe(viewLifecycleOwner){
            binding.tvCartTotalCost.text = it.toString()
        }

        initButtons()
        initAddressRcView()
        initCartRcView()

    }


    private fun initAddressRcView() {
        val markAddress: (address: Address) -> Unit = { viewModel.markAddress(address = it) }
        val removeAddress: (address: Address) -> Unit = { viewModel.removeAddress(address = it)}
        val editAddress: (address: Address) -> Unit = { editAddress(address = it)}
        addressAdapter = AddressAdapter(markAddress = markAddress, remove = removeAddress, edit =
        editAddress)
        binding.addressRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRcView.adapter = addressAdapter
        viewModel.addressList.observe(viewLifecycleOwner) {
            addressAdapter.submitList(it)
        }
    }

    private fun initCartRcView() {

        binding.cartRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRcView.adapter = cartAdapter
        viewModel.orderItemsList.observe(viewLifecycleOwner) {
            cartAdapter.submitList(it)
        }
    }

    private fun initButtons() {

        viewModel.isAddressNull.observe(viewLifecycleOwner) {
            if (it) {
                binding.addAddressButton.visibility = View.VISIBLE
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
        binding.confirmButton.setOnClickListener {

                if (!viewModel.isAddressNull.value!!) {
                    if (!viewModel.isOrderListEmpty.value!!) {
                        if (!viewModel.isPhoneNull.value!!) {
                            viewModel.createOrder()
                            viewModel.createNotification()
                            viewModel.cleanCart()
                            findNavController().navigate(R.id.action_confirmOrderFragment_to_cartSuccessFragment)
                        } else {
                            Toast.makeText(
                                requireContext(), "Добавьте номер телефона", Toast
                                    .LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.accDetailsFragment)
                        }
                    } else Toast.makeText(
                        requireContext(), "Добавьте товары в корзину", Toast
                            .LENGTH_SHORT
                    ).show()
                } else Toast.makeText(
                    requireContext(),
                    "Добавьте адрес доставки",
                    Toast.LENGTH_SHORT
                )
                    .show()


            }




        binding.addAddressButton.setOnClickListener {
            it.visibility = View.GONE
            findNavController().navigate(R.id.accAddAddressFragment)

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