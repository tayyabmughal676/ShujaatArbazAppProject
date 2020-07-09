package com.example.shujaatarbaz.Auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shujaatarbaz.Home.HomeActivity
import com.example.shujaatarbaz.Home.NavBarActivity
import com.example.shujaatarbaz.R
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var mLoginBtn: Button
    private lateinit var mSignUpBtn: Button

    //    Edit Text
    private lateinit var mUserLoginEmail: EditText
    private lateinit var mUserLoginPassword: EditText

    //    firebase
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        UI Initialized
        mLoginBtn = findViewById(R.id.userLoginBtn)
        mSignUpBtn = findViewById(R.id.userBackLoginToSignUpBtn)

        mUserLoginEmail = findViewById(R.id.editTextUserLoginEmail)
        mUserLoginPassword = findViewById(R.id.editTextUserLoginPassowrd)
        // Animation Button
        bindProgressButton(mLoginBtn)
//        Firebase
        mAuth = FirebaseAuth.getInstance()
//      Button clicks
        mLoginBtn.setOnClickListener {
//           Login With Email Password
            userLoginWithEmailPass()
        }
        mSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //    User login with Email Password
    private fun userLoginWithEmailPass() {
        val email = mUserLoginEmail.text.toString().trim()
        val password = mUserLoginPassword.text.toString().trim()
//          Check Validation
        if (!email.isValidEmail() || email.isEmpty()) {
            mUserLoginEmail.error = "Enter Valid Email"
            return
        }
        if (password.isEmpty()) {
            mUserLoginPassword.error = "Enter Password"
            return
        }
        //            Animator Btn
        mLoginBtn.attachTextChangeAnimator()
        mLoginBtn.showProgress {
            buttonTextRes = R.string.login_text
            progressColor = Color.WHITE
        }
//      Firebase Login
        loginWithFirebaseEmailPassword(email, password)
    }

    //  Firebase User Login
    private fun loginWithFirebaseEmailPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Go to Home Screen
                    mLoginBtn.hideProgress("Successful...")

                    val intent = Intent(this, NavBarActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()

                } else {
                    mLoginBtn.hideProgress("Authentication Failed...")
                    Toast.makeText(
                        applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
    }

    //    Check email is Valid.
    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}