package com.example.lab2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lab2.databinding.ActivityPersonDetailBinding
import com.example.lab2.entity.Person
import com.squareup.picasso.Picasso

class PersonDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonDetailBinding
    lateinit var person : Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.personVideo)

        Picasso.get().load(intent.getStringExtra("personImg")).into(binding.personImg)

        person = Person(intent.getStringExtra("personId").toString(),
                        intent.getStringExtra("personName").toString(),
                        intent.getIntExtra("personAge", 0),
                        intent.getStringExtra("personSex").toString(),
                        intent.getStringExtra("personCity").toString(),
                        intent.getStringExtra("personCountry").toString(),
                        intent.getDoubleExtra("personCost", 0.0),
                        intent.getStringExtra("personLangLevel").toString(),
                        intent.getStringExtra("personLang").toString(),
                        intent.getStringExtra("personImg").toString(),
                        intent.getStringExtra("personVideo").toString()
            )

        binding.personName.text = person.name
        binding.personCost.text = "${person.costPerHour} $"
        binding.personAge.text = person.age.toString()
        binding.personCountry.text = person.country
        binding.personCity.text = person.city
        binding.personLangLevel.text = person.languageLevel
        binding.personLang.text = person.language
        binding.personSex.text = person.sex
        binding.personVideo.setMediaController(mediaController)
        binding.personVideo.setVideoURI(Uri.parse(intent.getStringExtra("personVideo")))
        binding.personVideo.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_page_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this@PersonDetailActivity, EditPersonActivity::class.java)

                intent.apply {
                    putExtra("action", Constants.ACTIONS.getValue(Actions.EDIT))
                    putExtra("personName", person.name)
                    putExtra("personImg", person.pathImage)
                    putExtra("personCost", person.costPerHour)
                    putExtra("personSex", person.sex)
                    putExtra("personAge", person.age)
                    putExtra("personCountry", person.country)
                    putExtra("personCity", person.city)
                    putExtra("personLang", person.language)
                    putExtra("personLangLevel", person.languageLevel)
                    putExtra("personId", person.id)
                    putExtra("personVideo", person.pathVideo)
                }
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}