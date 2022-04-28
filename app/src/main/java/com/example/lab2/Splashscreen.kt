package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lab2.databinding.ActivitySplasnScreenBinding

class Splashscreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplasnScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplasnScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val icon: ImageView = findViewById(R.id.splashIcon)
        icon.alpha = 0f
        icon.animate().setDuration(3000).alpha(1f).withEndAction {
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}