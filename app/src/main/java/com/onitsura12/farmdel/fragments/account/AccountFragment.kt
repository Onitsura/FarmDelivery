package com.onitsura12.farmdel.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountBinding
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.USER
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: FragmentAccountBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        USER.photoUrl = AUTH.currentUser?.photoUrl.toString()
        USER.name = AUTH.currentUser?.displayName



        binding.apply {
            accDetailsText.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_accDetailsFragment)
            }
            accAddressText.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_accAddressFragment)
            }
            accOrdersText.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_accOrdersFragment)
            }
            accSignOutText.setOnClickListener {
                AUTH.signOut()
                Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

            }


            accountName.text = USER.name


            Picasso.get().load(USER.photoUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .fit()
                .into(accountPhoto)
        }

    }











}