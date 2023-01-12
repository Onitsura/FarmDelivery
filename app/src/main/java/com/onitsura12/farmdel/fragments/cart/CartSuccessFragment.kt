package com.onitsura12.farmdel.fragments.cart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onitsura12.farmdel.R

class CartSuccessFragment : Fragment() {

    companion object {
        fun newInstance() = CartSuccessFragment()
    }

    private lateinit var viewModel: CartSuccessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_success, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartSuccessViewModel::class.java)
        // TODO: Use the ViewModel
    }

}