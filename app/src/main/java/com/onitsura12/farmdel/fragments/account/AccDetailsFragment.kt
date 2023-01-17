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
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_EMAIL
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_FULLNAME
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
            editFullName()
        }
    }


    private fun setupAccInfo() {
        setupAccName()
        setupAccPhone()
        setupAccEmail()
    }


    private fun setupAccEmail() {
        binding.apply {
            if (UID != "") {
                accountEmail.text = USER.eMail
            }
        }
    }


    private fun setupAccPhone() {
        if (UID != "") {
            binding.apply {
                if (USER.phone.isBlank() || USER.phone.isEmpty() || USER.phone == "null") {
                    accountPhone.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.red_light)
                    )
                } else {
                    accountPhone.text = USER.phone
                }
            }
        }
    }


    private fun setupAccName() {
        if (UID != "") {
            binding.apply {
                if (USER.name!!.isBlank() || USER.name!!.isEmpty()) {
                    accountName.text = USER.name
                    accountName.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.red_light)
                    )
                } else {
                    accountName.text = USER.name
                }
            }
        }
    }


    private fun editFullName() {
        binding.apply {
            editNameButton.setOnClickListener {
                accountName.visibility = View.GONE
                etAccountName.visibility = View.VISIBLE
                editNameButton.visibility = View.GONE
                applyNameButton.visibility = View.VISIBLE

            }
            applyNameButton.setOnClickListener {
                if (etAccountName.text.isEmpty() || etAccountName.text.isBlank()) {
                    Toast.makeText(requireContext(), "Имя не может быть пустым", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val newName: String = etAccountName.text.toString()

                    REF_DATABASE_ROOT.child(NODE_USERS)
                        .child(UID)
                        .child(CHILD_FULLNAME)
                        .setValue(newName)
                        .addOnCompleteListener {
                            USER.name = newName
                            Toast.makeText(requireContext(), "Имя сохранено", Toast.LENGTH_SHORT)
                                .show()
                            accountName.text = USER.name
                            accountName.visibility = View.VISIBLE
                            etAccountName.visibility = View.GONE
                            editNameButton.visibility = View.VISIBLE
                            applyNameButton.visibility = View.GONE
                            accountName.setBackgroundColor(
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

    private fun editPhoneNumber() {
        binding.apply {
            editPhoneButton.setOnClickListener {
                accountPhone.visibility = View.GONE
                etAccountPhone.visibility = View.VISIBLE
                editPhoneButton.visibility = View.GONE
                applyPhoneButton.visibility = View.VISIBLE

            }
            applyPhoneButton.setOnClickListener {
                if (etAccountPhone.text.isEmpty() || etAccountPhone.text.isBlank()) {
                    Toast.makeText(
                        requireContext(), "Телефон не может быть пустым", Toast
                            .LENGTH_SHORT
                    )
                        .show()
                } else {
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
                            editPhoneButton.visibility = View.VISIBLE
                            applyPhoneButton.visibility = View.GONE
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






}