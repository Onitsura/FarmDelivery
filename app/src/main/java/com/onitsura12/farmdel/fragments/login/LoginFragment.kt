package com.onitsura12.farmdel.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentLoginBinding
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.AUTH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_EMAIL
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ID
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_PHONE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    firebaseAuth(account.idToken!!)
                    initUser()
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
            catch (e: ApiException){
                Toast.makeText(requireContext(), "smthng goes wrong", Toast.LENGTH_SHORT).show()
            }
        }


        checkAuth()
        
        binding.tvGoogleAuth.setOnClickListener {
            signInWithGoogle()
        }
        binding.tvPrivateMode.setOnClickListener {
            Toast.makeText(requireContext(), "Can't go this way now", Toast.LENGTH_SHORT).show()
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

    private fun checkAuth(){
        if (AUTH.currentUser != null){
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    private fun initUser() {
        val uid = AUTH.currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any?>()
        dateMap[CHILD_ID] = uid
        dateMap[CHILD_PHONE] = AUTH.currentUser?.phoneNumber
        dateMap[CHILD_FULLNAME] = AUTH.currentUser?.displayName
        dateMap[CHILD_EMAIL] = AUTH.currentUser?.email
        dateMap[FirebaseHelper.CHILD_PHOTO] = AUTH.currentUser?.photoUrl



        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
            .addOnCompleteListener { task->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Добро пожаловать", Toast.LENGTH_SHORT).show()

                } else Toast.makeText(requireContext(), task.exception?.message.toString(), Toast
                    .LENGTH_SHORT).show()
            }
    }

}