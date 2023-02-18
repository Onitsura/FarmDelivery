package com.onitsura12.farmdel.presentation.fragments.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountBinding
import com.onitsura12.farmdel.utils.AUTH
import com.onitsura12.farmdel.utils.LoginUtils.Companion.checkAuth
import com.onitsura12.farmdel.utils.LoginUtils.Companion.firebaseAuth
import com.onitsura12.farmdel.utils.LoginUtils.Companion.signInWithGoogle
import com.onitsura12.farmdel.utils.UID
import com.onitsura12.farmdel.utils.USER
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {



    private val viewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSignIn()
        initLauncher()
        initViews()



        viewModel.root.observe(viewLifecycleOwner) {
            if (it) {
                initAdminPanel()
            }
        }



        binding.apply {
            accDetailsText.setOnClickListener {
                if (UID.isNotEmpty()) {
                    findNavController().navigate(R.id.action_accountFragment_to_accDetailsFragment)
                }
            }
            accAddressText.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_accAddressFragment)
            }
            accOrdersText.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_accOrdersFragment)
            }
            feedbackText.setOnClickListener {
                try {
                    val mobile = "79888976162"
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone=$mobile")
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Возможно, у вас не установлен WhatsApp",
                        Toast.LENGTH_SHORT).show()
                }
            }
            accSignOutText.setOnClickListener {
                AUTH.signOut()
                Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                checkSignIn()
                USER.fullname = "Пользователь"
                USER.phone = ""
                USER.photoUrl = ""
                UID = ""
                viewModel.user.value?.fullname = USER.fullname
                findNavController().navigate(R.id.shopFragment)
            }
            tvGoogleAuth.setOnClickListener {
                signInWithGoogle(launcher = launcher, fragment = this@AccountFragment, context = requireContext())

            }



        }

    }

    private fun checkSignIn(){
        binding.apply {
            if (!checkAuth()) {
                accSignOut.visibility = View.GONE
                accSignOutText.visibility = View.GONE
//                tvGoogleAuth.visibility = View.VISIBLE
//                googleIcon.visibility = View.VISIBLE
            }
        }
    }



    private fun initLauncher(){
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    firebaseAuth(account.idToken!!)
//                    initUser()
                    findNavController().navigate(R.id.shopFragment)
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun initAdminPanel() {
        binding.accAdminPanel.visibility = View.VISIBLE
        binding.accAdminPanelButton.visibility = View.VISIBLE
        binding.accAdminPanel.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_adminPanel)
        }
    }

    private fun initViews(){
        binding.apply {
            if (UID.isNotEmpty()) {
                Picasso.get().load(AUTH.currentUser?.photoUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fit()
                    .into(accountPhoto)
            }

            viewModel.user.observe(viewLifecycleOwner) {
                binding.accountName.text = it.fullname
            }


        }
    }
}


