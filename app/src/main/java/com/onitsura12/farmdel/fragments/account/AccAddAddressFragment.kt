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

class AccAddAddressFragment : Fragment() {

    companion object {
        fun newInstance() = AccAddAddressFragment()
    }

    private val viewModel: AccAddAddressViewModel by activityViewModels()
    private lateinit var binding: FragmentAccAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccAddAddressBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            accAddAddressBackButton.setOnClickListener {
                findNavController().navigate(R.id.accAddressFragment)
            }
            saveButton.setOnClickListener {
                //TODO add in RV address
                val newAddress = Address(city = etCity.text.toString(), street =
                etStreet.text.toString(), entrance = etEntrance.text.toString(), floor = etFloor
                    .text.toString(), flat = etFlat.text.toString(), id = viewModel.getId().toString())
                viewModel.newAddress.value = newAddress
                if (viewModel.newAddress.value != null) {
                    Log.e("Addr", viewModel.newAddress.value.toString())
                    saveAddress(viewModel.newAddress.value!!)

                    findNavController().navigate(R.id.accAddressFragment)
                }
                else{
                    Log.e("Addr", viewModel.newAddress.value.toString())
                    Toast.makeText(requireContext(), "Something going wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun saveAddress(newAddress: Address){
        val address = viewModel.convertToArrayList(newAddress)
        val bundle = Bundle()
        bundle.putStringArrayList("newAddress", address)
        setFragmentResult("newAddress", bundle)

    }

}