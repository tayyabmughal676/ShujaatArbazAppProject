package com.example.shujaatarbaz.Home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.*
import com.example.shujaatarbaz.MainActivity
import com.example.shujaatarbaz.R
import com.example.shujaatarbaz.R.id
import com.example.shujaatarbaz.utils.userDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.w3c.dom.Text

class NavBarActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bar)
//        toolbar
        val toolbar: Toolbar = findViewById(id.toolbar)
        setSupportActionBar(toolbar)
//        Firebase
        mAuth = FirebaseAuth.getInstance()

//      Floating Button
        val fab: FloatingActionButton = findViewById(id.fab)
        fab.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val drawerLayout: DrawerLayout = findViewById(id.drawer_layout)
        val navView: NavigationView = findViewById(id.nav_view)
        val navController = findNavController(id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each

        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                id.nav_home, id.nav_gallery, id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_bar, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}