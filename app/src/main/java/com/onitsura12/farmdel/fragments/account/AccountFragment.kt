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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initUser
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAccountBinding
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

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
        initUser()
        viewModel.userName.observe(viewLifecycleOwner){
            binding.accountName.text = it
        }

        viewModel.userName.observe(viewLifecycleOwner){
            binding.accountName.text = it
        }

        binding.apply {

            if (UID == "" || UID == "null"){
                accSignOut.visibility = View.GONE
                accSignOutText.visibility = View.GONE
                tvGoogleAuth.visibility = View.VISIBLE
                googleIcon.visibility = View.VISIBLE
            }
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
            accSignOutText.setOnClickListener {
                AUTH.signOut()
                Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                accSignOut.visibility = View.GONE
                accSignOutText.visibility = View.GONE
                tvGoogleAuth.visibility = View.VISIBLE
                googleIcon.visibility = View.VISIBLE
                USER.fullname = "Пользователь"
                USER.phone = ""
                USER.photoUrl = ""
                UID = ""
                viewModel.userName.value = USER.fullname
            }
            tvGoogleAuth.setOnClickListener {
                Log.i("AuthAcc", UID)
                signInWithGoogle()
            }
            if (UID.isNotEmpty()) {
                Picasso.get().load(USER.photoUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fit()
                    .into(accountPhoto)
            }

            launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

                try {
                    val account = task.getResult(ApiException::class.java)

                    if (account != null){
                        firebaseAuth(account.idToken!!)
//                        initUser()
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