package com.onitsura12.farmdel.fragments.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.onitsura12.data.storage.firebase.utils.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initUser
import com.onitsura12.data.storage.firebase.utils.UID
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentLoginBinding
import com.onitsura12.farmdel.utils.LoginUtils.Companion.signInWithGoogle


class LoginFragment : Fragment() {


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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (AUTH.currentUser != null){
            UID = AUTH.currentUser?.uid.toString()
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

        initButtons()
        initLauncher()



    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initButtons(){
        binding.tvGoogleAuth.setOnClickListener {
            if (binding.userAgreement.isChecked) {
                signInWithGoogle(launcher = launcher, fragment = this, context = requireContext())
            }
            else {
                Toast.makeText(requireContext(), "Для продолжения надо принять " +
                        "пользовательское соглашение", Toast.LENGTH_SHORT).show()
            }

        }

        binding.tvUserAgreement.movementMethod = LinkMovementMethod.getInstance()
        binding.tvUserAgreement.text = Html.fromHtml("Я прочитал и согласен с <a " +
                "href=http://drive" +
                ".google.com/drive/folders/1upBJFhQLntlTk-mqbT0HxI9AuOoPZ5zJ?usp=sharing>пользовательским соглашением и политикой конфиденциальности</a>", 0)
    }

    private fun initLauncher(){
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    AUTH.signInWithCredential(credential).addOnCompleteListener {auth ->
                        if (auth.isSuccessful){
                            initUser()
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                    }
                }
            }
            catch (e: ApiException){
                Toast.makeText(requireContext(), "smthng goes wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }




}