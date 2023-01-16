package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccAddAddressBinding
import com.onitsura12.domain.models.Address
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID

class AccAddAddressFragment : Fragment() {

    companion object {
        fun newInstance() = AccAddAddressFragment()
    }

    private val viewModel: AccAddAddressViewModel by activityViewModels()
    private lateinit var binding: FragmentAccAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccAddAddressBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            accAddAddressBackButton.setOnClickListener {
                findNavController().navigate(R.id.accAddressFragment)
            }
            saveButton.setOnClickListener {
                val newAddress = Address(
                    city = etCity.text.toString(),
                    street = etStreet.text.toString(),
                    entrance = etEntrance.text.toString(),
                    house = etHouse.text.toString(),
                    floor = etFloor.text.toString(),
                    flat = etFlat.text.toString(),
                    id = viewModel.getId().toString()
                )
                REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_ADDRESS)
                    .child(newAddress.id).setValue(newAddress)
                    findNavController().navigate(R.id.accAddressFragment)
            }
        }
    }




}