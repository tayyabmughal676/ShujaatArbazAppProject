package com.example.shujaatarbaz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shujaatarbaz.Auth.LoginActivity
import com.example.shujaatarbaz.Auth.SignUpActivity
import com.example.shujaatarbaz.Home.HomeActivity
import com.example.shujaatarbaz.Home.NavBarActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
//        UI Clicks
        BtnGoWithLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        BtnGoWithEmailPassword.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
//       check user already logged In
        mAuth.currentUser?.let {
            val intent = Intent(applicationContext, NavBarActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}