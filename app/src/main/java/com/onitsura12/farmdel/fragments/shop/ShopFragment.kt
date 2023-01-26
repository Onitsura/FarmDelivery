package com.onitsura12.farmdel.fragments.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initUser
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentShopBinding
import com.onitsura12.farmdel.recyclerView.ShopAdapter

class ShopFragment : Fragment() {

    companion object {
        fun newInstance() = ShopFragment()
    }

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
        adapter = ShopAdapter(
            clickIncrement = clickIncrement,
            clickDecrement = clickDecrement)
        viewModel.adapterList.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }

    }


}