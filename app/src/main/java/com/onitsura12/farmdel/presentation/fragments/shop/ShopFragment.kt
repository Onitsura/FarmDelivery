package com.onitsura12.farmdel.presentation.fragments.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentShopBinding
import com.onitsura12.farmdel.domain.models.ShopItem
import com.onitsura12.farmdel.presentation.recyclerView.ShopAdapter
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.initUser
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID

class ShopFragment : Fragment() {


    private lateinit var viewModel: ShopViewModel
    private lateinit var binding: FragmentShopBinding
    private lateinit var adapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ShopViewModel()

        binding = FragmentShopBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstSign()
        initRcView()

        viewModel.isInWhiteList(UID)

        viewModel.showAddButton.observe(viewLifecycleOwner) {
            if (it) {
                binding.addShopItem.visibility = View.VISIBLE
            }
        }
        binding.addShopItem.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addShopItemFragment)
        }

        viewModel.isCartEmpty.observe(viewLifecycleOwner) {
            if (!it) {
                binding.toCartButton.visibility = View.VISIBLE
                binding.toCartButton.setOnClickListener {
                    findNavController().navigate(R.id.cartFragment)
                }
            } else binding.toCartButton.visibility = View.GONE
        }


    }


    private fun initRcView() {
        initListener()
        binding.shopRcView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.shopRcView.adapter = adapter
        val itemAnimator = binding.shopRcView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

    }

    //TODO Перенести во VM
    private fun firstSign() {
        val userNode = REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(UID)
            .get()
            .addOnSuccessListener {
                if (it.value.toString() == "null") {
                    initUser()
                }
            }
    }

    private fun initListener() {
        val clickIncrement: (cartItem: ShopItem) -> Unit = { viewModel.incrementItemCount(it) }
        val clickDecrement: (cartItem: ShopItem) -> Unit = { viewModel.decrementItemCount(it) }
        val chooseClick: (shopItem: ShopItem) -> Unit = {
            findNavController().navigate(
                R.id.action_shopFragment_to_shopItemDetailsFragment,
                bundleOf("title" to it.title)
            )
        }
        adapter = ShopAdapter(
            clickIncrement = clickIncrement,
            clickDecrement = clickDecrement,
            chooseClick = chooseClick
        )
        viewModel.adapterList.observe(viewLifecycleOwner) {

            adapter.submitList(it.toMutableList())
        }

    }


}