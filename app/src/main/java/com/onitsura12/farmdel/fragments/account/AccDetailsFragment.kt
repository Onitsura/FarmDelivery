package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountDetailsBinding
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_PHONE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.USER

class AccDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AccDetailsFragment()
    }


    private lateinit var viewModel: AccDetailsViewModel
    private lateinit var binding: FragmentAccountDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountDetailsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            accAddressBackButton.setOnClickListener {
                findNavController().navigate(R.id.accountFragment)
            }
            setupAccInfo()
            editPhoneNumber()
        }
    }


    private fun setupAccInfo() {
        binding.apply {
            accountName.text = AUTH.currentUser?.displayName
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHONE).get().addOnSuccessListener { data ->
                USER.phone = data.value.toString()
            }
            if (USER.phone.isBlank()) {
                accountPhone.text = USER.phone
//                accountPhone.setBackgroundColor(
//                    ContextCompat.getColor(
//                        requireContext(), R.color
//                            .red_light
//                    )
//                )
            } else {
                accountPhone.text = USER.phone
            }
            accountEmail.text = AUTH.currentUser?.email
        }
    }

    private fun editPhoneNumber() {
        binding.apply {
            editButton.setOnClickListener {
                accountPhone.visibility = View.GONE
                etAccountPhone.visibility = View.VISIBLE
                editButton.visibility = View.GONE
                applyButton.visibility = View.VISIBLE

            }
            applyButton.setOnClickListener {
                val newNumber: String = etAccountPhone.text.toString()

                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(UID)
                    .child(CHILD_PHONE)
                    .setValue(newNumber)
                    .addOnCompleteListener {
                        USER.phone = newNumber
                        Toast.makeText(requireContext(), "Номер сохранён", Toast.LENGTH_SHORT)
                            .show()
                        accountPhone.text = USER.phone
                        accountPhone.visibility = View.VISIBLE
                        etAccountPhone.visibility = View.GONE
                        editButton.visibility = View.VISIBLE
                        applyButton.visibility = View.GONE
                        accountPhone.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color
                                    .white
                            )
                        )
                    }
            }
        }

    }
}