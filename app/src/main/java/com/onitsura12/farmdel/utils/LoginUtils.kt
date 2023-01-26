package com.onitsura12.farmdel.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.farmdel.R

class LoginUtils {


    companion object {

        private fun getClient(fragment: Fragment, context: Context): GoogleSignInClient {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(fragment.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(context, gso)
        }

        fun signInWithGoogle(
            launcher: ActivityResultLauncher<Intent>,
            fragment: Fragment,
            context: Context
        ) {
            val signInClient = getClient(fragment = fragment, context = context)
            launcher.launch(signInClient.signInIntent)
        }

        fun firebaseAuth(idToken: String) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseHelper.AUTH.signInWithCredential(credential)
        }

        fun checkAuth(): Boolean {
            return (FirebaseHelper.AUTH.currentUser != null)
        }
    }
}
