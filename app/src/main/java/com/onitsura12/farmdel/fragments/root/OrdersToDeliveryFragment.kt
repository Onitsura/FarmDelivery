package com.onitsura12.farmdel.fragments.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding = FragmentOrdersToDeliveryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
    }


    private fun initRcView() {
        binding.apply {
            ordersToDeliveryRcView.layoutManager = LinearLayoutManager(requireContext())
            ordersToDeliveryRcView.adapter = adapter

            viewModel.ordersList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        }
    }


}