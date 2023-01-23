package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onitsura12.domain.models.Address
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccAddAddressBinding


class AccAddAddressFragment : Fragment() {


    private lateinit var viewModel: AccAddAddressViewModel
    private lateinit var binding: FragmentAccAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccAddAddressBinding.inflate(layoutInflater)
        viewModel = AccAddAddressViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            accAddAddressBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            saveButton.setOnClickListener {
                val newAddress = Address(
                    city = etCity.text.toString(),
                    street = etStreet.text.toString(),
                    entrance = etEntrance.text.toString(),
                    house = etHouse.text.toString(),
                    floor = etFloor.text.toString(),
                    flat = etFlat.text.toString(),
                    id = viewModel.getId().toString(),
                    primary = true
                )
                viewModel.createNewAddress(newAddress = newAddress)
                findNavController().popBackStack()
            }
        }
    }


}