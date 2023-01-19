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
import com.onitsura12.farmdel.databinding.FragmentCartBinding
import com.onitsura12.farmdel.recyclerView.CartAdapter

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var viewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding
    private val adapter: CartAdapter = CartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        viewModel = CartViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_confirmOrderFragment)
        }
        initRcView()
    }



    private fun initRcView() {
        binding.cartRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRcView.adapter = adapter
        viewModel.cart.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }

}