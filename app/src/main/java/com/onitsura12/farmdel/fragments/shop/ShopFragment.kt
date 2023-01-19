package com.onitsura12.farmdel.fragments.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.makeramen.roundedimageview.RoundedImageView
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentShopBinding
import com.onitsura12.farmdel.databinding.ShopItemBinding
import com.onitsura12.farmdel.recyclerView.OnListItemClickListener
import com.onitsura12.farmdel.recyclerView.ShopAdapter
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.USER
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.initUser

class ShopFragment : Fragment() {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private lateinit var viewModel: ShopViewModel
    private lateinit var binding: FragmentShopBinding
    private val adapter: ShopAdapter = ShopAdapter()

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
        initRcView()
        initUser()
        viewModel.isInWhiteList(UID)

        viewModel.showAddButton.observe(viewLifecycleOwner) {
            if (it) {
                binding.addShopItem.visibility = View.VISIBLE
            }
        }
        binding.addShopItem.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addShopItemFragment)
        }

        viewModel.shopItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }


    private fun initRcView() {
        initListener()
        binding.shopRcView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.shopRcView.adapter = adapter
    }

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
        val listener = object : OnListItemClickListener {
            //Работает, записывает данные в бд, но клик работает только со второго щелчка
            override fun onClick(view: ViewGroup, position: Int) {
                    view.findViewById<RoundedImageView>(R.id.cartItemIncreaseButton).setOnClickListener {
                        val counter = view.findViewById<TextView>(R.id
                            .cartItemCounter)
                        val title = view.findViewById<TextView>(R.id.itemTitle)
                        val cost = view.findViewById<TextView>(R.id.itemCost)
                        val newValue = counter.text.toString().toInt() + 1
                        counter.text = newValue.toString()
                        val newCartItem = ShopItem(
                            title = title.text.toString(),
                            cost = cost.text.toString(),
                            count = counter.text.toString(),

                        )
                        viewModel.addNewCartItem(newCartItem)
                        Log.i("Counter", view.id.toString())
                    }

                    view.findViewById<RoundedImageView>(R.id.cartItemDecreaseButton).setOnClickListener {
                        val counter = binding.shopRcView.findViewById<TextView>(R.id
                            .cartItemCounter)
                        val title = view.findViewById<TextView>(R.id.itemTitle)
                        val cost = view.findViewById<TextView>(R.id.itemCost)
                        if(counter.text.toString().toInt() > 0) {
                            val newValue = counter.text.toString().toInt() - 1
                            counter.text = newValue.toString()
                            val newCartItem = ShopItem(
                                title = title.text.toString(),
                                cost = cost.text.toString(),
                                count = counter.text.toString()
                            )
                            viewModel.addNewCartItem(newCartItem)
                            Log.i("Counter", view.id.toString())
                        }

                        }

                }
            }




        adapter.setListener(listener = listener)
    }


}