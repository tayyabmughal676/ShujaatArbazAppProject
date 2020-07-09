package com.example.shujaatarbaz.Home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.shujaatarbaz.MainActivity
import com.example.shujaatarbaz.R
import com.example.shujaatarbaz.utils.userDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirebStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        Firebase
        mAuth = FirebaseAuth.getInstance()
        mFirebStore = FirebaseFirestore.getInstance()

        val mUser = mAuth.currentUser

        mFirebStore.collection(userDatabase)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val userName: String = document.data["username"].toString()
                        val phoneNumber: String = document.data["phone"].toString()
                        val userDetail = " $userName \n $phoneNumber "
                        homeUserName.text = userDetail
                        Toast.makeText(
                            applicationContext,
                            "Data: ${document.data["username"]}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error: ${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

//        btn
        homeBtn.setOnClickListener {
//          sign Out
            mAuth.signOut()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}