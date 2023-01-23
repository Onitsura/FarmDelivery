package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.domain.models.Order
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccOrdersBinding
import com.onitsura12.farmdel.recyclerView.OrderAdapter
import com.onitsura12.farmdel.recyclerView.OrderItemAdapter

class AccOrdersFragment : Fragment() {


    private val viewModel: AccOrdersViewModel by activityViewModels()
    private lateinit var binding: FragmentAccOrdersBinding
    private val orderAdapter: OrderAdapter = OrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccOrdersBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accOrdersBackButton.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }

        initOrderRcView()
    }


    private fun initOrderRcView() {
        binding.rcViewOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rcViewOrders.adapter = orderAdapter
        viewModel.ordersList.observe(viewLifecycleOwner) {
            orderAdapter.submitList(it)

        }
    }


}