package com.onitsura12.farmdel.fragments.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onitsura12.farmdel.R

class AccAddressFragment : Fragment() {

    companion object {
        fun newInstance() = AccAddressFragment()
    }

    private lateinit var viewModel: AccAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccAddressViewModel::class.java)
        // TODO: Use the ViewModel
    }

}