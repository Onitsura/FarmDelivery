package com.onitsura12.farmdel.fragments.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountBinding
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.USER
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.initUser
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: FragmentAccountBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        viewModel = AccountViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userName.observe(viewLifecycleOwner){
            binding.accountName.text = it
        }
        initUser()

        binding.apply {

            if (UID == "" || UID == "null"){
                accSignOut.visibility = View.GONE
                accSignOutText.visibility = View.GONE
                tvGoogleAuth.visibility = View.VISIBLE
                googleIcon.visibility = View.VISIBLE
            }
            accDetailsText.setOnClickListener {
                if (UID != "" && UID != "null") {
                    findNavController().navigate(R.id.action_accountFragment_to_accDetailsFragment)
                }
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
                accSignOut.visibility = View.GONE
                accSignOutText.visibility = View.GONE
                tvGoogleAuth.visibility = View.VISIBLE
                googleIcon.visibility = View.VISIBLE
                USER.name = "Пользователь"
                USER.phone = ""
                USER.photoUrl = ""
                UID = ""
                viewModel.userName.value = USER.name
            }
            tvGoogleAuth.setOnClickListener {
                signInWithGoogle()
            }

            Picasso.get().load(USER.photoUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .fit()
                .into(accountPhoto)

            launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

                try {
                    val account = task.getResult(ApiException::class.java)

                    if (account != null){
                        firebaseAuth(account.idToken!!)
                        initUser()
                        findNavController().navigate(R.id.shopFragment)
                    }
                }
                catch (e: ApiException){
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }




    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuth(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        AUTH.signInWithCredential(credential)
    }














}