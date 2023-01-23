package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_PHONE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountDetailsBinding

class AccDetailsFragment : Fragment() {


    private val viewModel: AccDetailsViewModel by activityViewModels()
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
            accDetailsBackButton.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.apply {
                viewModel.user.observe(viewLifecycleOwner) {
                    //setup e-mail
                    accountEmail.text = it.eMail
                    //setup phone
                    if (it.phone != "") {
                        accountPhone.text = it.phone
                    } else accountPhone.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red_light
                        )
                    )
                    //setup name
                    accountName.text = it.fullname
                }
            }
            editPhoneNumber()
            editFullName()
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

                    viewModel.saveNewName(newName = newName, context = requireContext())
                    accountName.text = USER.fullname
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
                        requireContext(),
                        "Телефон не может быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    viewModel.checkPhone(phone = etAccountPhone.text.toString())

                    if (viewModel.isPhoneCorrect.value!!) {

                        val newNumber: String = etAccountPhone.text.toString()

                        viewModel.saveNewPhone(newPhone = newNumber, context = requireContext())

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
                    } else Toast.makeText(
                        requireContext(),
                        "Номер телефона должен содержать 10-12 " + "цифр",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


}