package com.onitsura12.farmdel.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.*
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_EMAIL
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_PHOTO
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initUser
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
            AUTH.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful){
                    initUser()
                }
            }
        }

        fun checkAuth(): Boolean {
            return (AUTH.currentUser != null)
        }


        fun setupAccInfo() {
//            setupUser()
            setupAccName()
            setupAccEmail()
            setupAccPhone()
            setupAccImage()

        }

//        private fun setupUser(){
//            UID = AUTH.currentUser?.uid.toString()
//            REF_DATABASE_ROOT.child(NODE_USERS)
//                .addValueEventListener(object: ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        for (userSnapshot in snapshot.children) {
//                            if ()
//                            USER = userSnapshot.getValue(User::class.java)!!
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                })
//
//        }

        private fun setupAccName() {
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(UID).child(CHILD_FULLNAME)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                            USER.fullname = snapshot.value.toString()


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


        }

        private fun setupAccEmail() {
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(UID).child(CHILD_EMAIL).get()
                .addOnCompleteListener {
                    USER.eMail = it.result.value.toString()
                }

        }


        private fun setupAccPhone() {
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(UID).child(FirebaseHelper.CHILD_PHONE).get()
                .addOnCompleteListener {
                    USER.phone = it.result.value.toString()

                }


        }
        private fun setupAccImage() {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(
                CHILD_PHOTO
            )
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        USER.photoUrl = snapshot.getValue(String::class.java).toString()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }
}

