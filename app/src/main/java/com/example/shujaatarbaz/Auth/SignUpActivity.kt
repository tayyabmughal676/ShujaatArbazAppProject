package com.example.shujaatarbaz.Auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.shujaatarbaz.Home.HomeActivity
import com.example.shujaatarbaz.Home.NavBarActivity
import com.example.shujaatarbaz.R
import com.example.shujaatarbaz.utils.userDatabase
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {
    private lateinit var mLoginBtn: Button
    private lateinit var mSignUpBtn: Button

    //    Edit Text
    private lateinit var mUserSignUpUserName: EditText
    private lateinit var mUserSignUpEmail: EditText
    private lateinit var mUserSignUpPhoneNumber: EditText
    private lateinit var mUserSignUpPassword: EditText

    //    Firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
//        UI Initialized
        mLoginBtn = findViewById(R.id.userBackSignUpToLoginUpBtn)
        mSignUpBtn = findViewById(R.id.userSignUpBtn)

        mUserSignUpUserName = findViewById(R.id.editTextUserName)
        mUserSignUpEmail = findViewById(R.id.editTextUserEmail)
        mUserSignUpPhoneNumber = findViewById(R.id.editTextUserPhone)
        mUserSignUpPassword = findViewById(R.id.editTextUserPassowrd)
//        Firebase
        mAuth = FirebaseAuth.getInstance()
        mFireStore = FirebaseFirestore.getInstance()

        // Animation Button
        bindProgressButton(mSignUpBtn)

//        Button Clicks
        mLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        mSignUpBtn.setOnClickListener {
            userSignUpWithEmailPassword()
        }
    }

    // user Sing up with Email Password
    private fun userSignUpWithEmailPassword() {
        val signUpEmail = mUserSignUpEmail.text.toString().trim()
        val signUpUserName = mUserSignUpUserName.text.toString().trim()
        val signUpPhoneNumber = mUserSignUpPhoneNumber.text.toString().trim()
        val signUpPassword = mUserSignUpPassword.text.toString().trim()
//          Check Validation
        if (signUpUserName.isEmpty()) {
            mUserSignUpUserName.error = "Enter User Name"
            return
        }
        if (!signUpEmail.isValidEmail() || signUpEmail.isEmpty()) {
            mUserSignUpEmail.error = "Enter Valid Email"
            return
        }
        if (signUpPhoneNumber.isEmpty()) {
            mUserSignUpPhoneNumber.error = "Enter Phone Number"
            return
        }
        if (signUpPassword.isEmpty()) {
            mUserSignUpPassword.error = "Enter Password"
            return
        }

        //            Animator Btn
        mSignUpBtn.attachTextChangeAnimator()
        mSignUpBtn.showProgress {
            buttonTextRes = R.string.login
            progressColor = Color.WHITE
        }
//        sign Up User With Firebase Email Password
        signUpWithFirebaseEmailPassword(
            signUpEmail,
            signUpPassword,
            signUpUserName,
            signUpPhoneNumber
        )
    }

    //      create user with firebase email password
    private fun signUpWithFirebaseEmailPassword(
        signUpEmail: String,
        signUpPassword: String,
        signUpUserName: String,
        signUpPhoneNumber: String
    ) {
        mAuth.createUserWithEmailAndPassword(signUpEmail, signUpPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUserID = mAuth.currentUser?.uid.toString()
//                   Mapping the user date
                    val userMapData = HashMap<String, String>()
                    userMapData["userID"] = currentUserID
                    userMapData["username"] = signUpUserName
                    userMapData["email"] = signUpEmail
                    userMapData["phone"] = signUpPhoneNumber

                    // Add a new document with a generated ID
                    mFireStore.collection(userDatabase)
                        .add(userMapData)
                        .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                            println(documentReference)
                        })
                        .addOnFailureListener(OnFailureListener { e ->
                            Toast.makeText(applicationContext, "Errors $e", Toast.LENGTH_SHORT)
                                .show()
                        })

                    // Go to Home Screen
                    mSignUpBtn.hideProgress("Successful...")
                    val intent = Intent(applicationContext, NavBarActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    mSignUpBtn.hideProgress("Failed...")
                    Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show();
                }
            }
    }

    //    Check email is Valid.
    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}