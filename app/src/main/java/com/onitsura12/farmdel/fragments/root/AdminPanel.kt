package com.onitsura12.farmdel.fragments.root

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAdminPanelBinding


class AdminPanel : Fragment() {

    private lateinit var binding: FragmentAdminPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminPanelBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            accAdminPanelBackButton.setOnClickListener {
                findNavController().navigate(R.id.accountFragment)
            }

            ordersToDeliveryText.setOnClickListener {
                findNavController().navigate(R.id.action_adminPanel_to_ordersToDeliveryFragment)
            }

            changeDeliveryDateText.setOnClickListener {
                findNavController().navigate(R.id.action_adminPanel_to_changeDeliveryDateFragment)
            }

        }
    }
}