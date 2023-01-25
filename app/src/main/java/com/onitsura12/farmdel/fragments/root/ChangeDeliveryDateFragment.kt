package com.onitsura12.farmdel.fragments.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.databinding.FragmentChangeDeliveryDateBinding
import com.onitsura12.farmdel.recyclerView.OrderItemAdapter

class ChangeDeliveryDateFragment : Fragment() {


    private val viewModel: ChangeDeliveryDateViewModel by activityViewModels()
    private lateinit var binding: FragmentChangeDeliveryDateBinding
    private lateinit var adapter: OrderItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeDeliveryDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeDeliveryDateBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        initRcView()
    }


    private fun initRcView() {
        val click: (shopItem: ShopItem) -> Unit = { viewModel.changeDeliveryDate(it) }
        viewModel.root.observe(viewLifecycleOwner) {
            adapter = OrderItemAdapter(root = it, click = click)
            binding.changeDeliveryDateRcView.layoutManager = LinearLayoutManager(requireContext())
            binding.changeDeliveryDateRcView.adapter = adapter
        }
        viewModel.adapterList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }


}