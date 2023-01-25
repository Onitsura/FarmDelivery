package com.onitsura12.farmdel.fragments.root

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentOrdersToDeliveryBinding
import com.onitsura12.farmdel.recyclerView.OrderToDeliveryAdapter

class OrdersToDeliveryFragment : Fragment() {

    private lateinit var binding: FragmentOrdersToDeliveryBinding
    private val adapter: OrderToDeliveryAdapter = OrderToDeliveryAdapter()
    private val viewModel: OrdersToDeliveryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getOrders()
        binding = FragmentOrdersToDeliveryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }


        }

        initRcView()
        setupPopupMenu()
    }


    private fun initRcView() {
        binding.apply {
            ordersToDeliveryRcView.layoutManager = LinearLayoutManager(requireContext())
            ordersToDeliveryRcView.adapter = adapter

            viewModel.adapterList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }




        }


    }

    private fun setupPopupMenu(){
        binding.apply {
            val popupMenu = PopupMenu(requireContext(), sortDateButton, Gravity.CENTER)

            viewModel.allDates.observe(viewLifecycleOwner) { it ->

                for (i in it.indices) {
                    popupMenu.menu.add(0, i, 0, it[i])
                }
                popupMenu.inflate(R.menu.popup_delivery_date_menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    viewModel.findOrderByDate(item.toString())
                    tvDateToDelivery.text = item.toString()
                    return@setOnMenuItemClickListener true
                }
            }

            sortDateButton.setOnClickListener {
                popupMenu.show()
            }
        }
    }
}






