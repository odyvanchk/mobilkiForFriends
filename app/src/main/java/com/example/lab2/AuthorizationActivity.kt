package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lab2.databinding.ActivityAuthorizationBinding
import com.google.firebase.auth.FirebaseAuth

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

//        tvRedirectSignUp.setOnClickListener {
//            val intent = Intent(this, RegistrationActivity::class.java)
//            startActivity(intent)
//            // using finish() to end the activity
//            finish()
//        }
    }

    private fun login() {
        val email = binding.etEmailAddress.text.toString()
        val pass = binding.etPassword.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}