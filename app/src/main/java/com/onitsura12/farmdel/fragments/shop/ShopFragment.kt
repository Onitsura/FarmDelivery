package com.onitsura12.farmdel.fragments.shop

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentShopBinding
import com.onitsura12.farmdel.recyclerView.ShopAdapter
import com.onitsura12.farmdel.utils.FirebaseHelper
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
        firstSign()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUser()
        binding.addShopItem.visibility = View.VISIBLE
        binding.addShopItem.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addShopItemFragment)
        }

        viewModel.shopItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        initRcView()
    }


    private fun initRcView() {
        binding.shopRcView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.shopRcView.adapter = adapter
    }

    private fun firstSign(){
        val userNode = FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseHelper.NODE_USERS).child(
            FirebaseHelper.UID
        ).get().addOnSuccessListener {
            Log.i("firstEntry", it.value.toString())
            if (it.value.toString() == "null"){
                FirebaseHelper.initUser()
            }
        }
    }

}