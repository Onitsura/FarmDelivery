package com.onitsura12.farmdel.presentation.fragments.account

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        initPopupMenu()
        binding.apply {
            accAddAddressBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            popupCities.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etEntrance.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etHouse.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etStreet.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etFloor.setOnEditorActionListener { _, p1, _ -> p1 == EditorInfo.IME_ACTION_NEXT }
            etFlat.setOnEditorActionListener { _, _, _ ->
                viewModel.addNewAddress(
                    newAddress = viewModel.createNewAddress(
                        city = popupCities.text.toString(),
                        street = etStreet.text.toString(),
                        house = etHouse.text.toString(),
                        entrance = etEntrance.text.toString(),
                        floor = etFloor.text.toString(),
                        flat = etFlat.text.toString()
                    )
                )
                hideKeyboard()
                findNavController().popBackStack()
                true
            }

            saveButton.setOnClickListener {
                viewModel.addNewAddress(
                    newAddress = viewModel.createNewAddress(
                        city = popupCities.text.toString(),
                        street = etStreet.text.toString(),
                        house = etHouse.text.toString(),
                        entrance = etEntrance.text.toString(),
                        floor = etFloor.text.toString(),
                        flat = etFlat.text.toString()
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initPopupMenu() {
        binding.apply {
            val popupMenu = PopupMenu(requireContext(), popupCities, Gravity.CENTER)

            popupMenu.inflate(R.menu.popup_cities_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                popupCities.text = item.toString()
                return@setOnMenuItemClickListener true
            }


            popupCities.setOnClickListener {
                popupMenu.show()
            }
        }
    }

}