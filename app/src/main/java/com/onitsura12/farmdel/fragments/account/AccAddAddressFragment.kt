package com.onitsura12.farmdel.fragments.account

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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
            etCity.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etEntrance.setOnEditorActionListener{ _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etHouse.setOnEditorActionListener{ _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etStreet.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etFloor.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etFlat.setOnEditorActionListener { _, _, _ ->
                val newAddress = Address(
                    city = etCity.text.toString(),
                    street = etStreet.text.toString(),
                    entrance = etEntrance.text.toString(),
                    house = etHouse.text.toString(),
                    floor = etFloor.text.toString(),
                    flat = etFlat.text.toString(),
                    id = viewModel.getId().toString(),
                    primary = false
                )
                viewModel.createNewAddress(newAddress = newAddress)
                hideKeyboard()
                findNavController().popBackStack()
                true
            }
//            parentFragmentManager.setFragmentResultListener(
//                "selectedAddress", viewLifecycleOwner
//            ) { _, bundle ->
//                val addressId = (bundle.getString
//                    ("selectedAddress")!!)
//                viewModel.selectedAddressId.value = addressId
//            }

            //TODO Edit address
//            if (viewModel.selectedAddressId.value != null) {
//                if (viewModel.selectedAddressId.value!!.isNotEmpty() && viewModel
//                        .selectedAddressId.value!!.isNotBlank()){
//                    viewModel.selectedAddress.observe(viewLifecycleOwner){
//
//                    }
//                }
//            }

            saveButton.setOnClickListener {
                val newAddress = Address(
                    city = etCity.text.toString(),
                    street = etStreet.text.toString(),
                    entrance = etEntrance.text.toString(),
                    house = etHouse.text.toString(),
                    floor = etFloor.text.toString(),
                    flat = etFlat.text.toString(),
                    id = viewModel.getId().toString(),
                    primary = false
                )
                viewModel.createNewAddress(newAddress = newAddress)
                findNavController().popBackStack()
            }
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }




//    private fun setupEdit(address: Address){
//        binding.apply {
//
//        }
//    }
//
//    private fun resolveAddress(address: Address?){
//
//    }


}