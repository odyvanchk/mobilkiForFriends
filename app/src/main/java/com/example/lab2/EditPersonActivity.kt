package com.example.lab2

import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab2.databinding.ActivityEditPersonBinding
import com.example.lab2.entity.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import com.google.android.gms.tasks.Continuation
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.UploadTask
import java.util.*

class EditPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPersonBinding
    lateinit var person : Person

    lateinit var name : String
    lateinit var costPerHour : String
    lateinit var city : String
    lateinit var country : String
    lateinit var sex : String
    lateinit var language : String
    lateinit var langLevel : String
    lateinit var age : String
    var imageUri: Uri? = null
    var videoUri: Uri? = null

    lateinit var personImageRef: StorageReference
    lateinit var personVideoRef: StorageReference
    lateinit var personRandomKey: String
    lateinit var downloadImageUrl: String
    lateinit var downloadVideoUrl: String
    lateinit var personsDBRef: DatabaseReference
    lateinit var loadingBar: ProgressDialog
    var action: Actions = Actions.ADD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingBar = ProgressDialog(this)

        personImageRef = FirebaseStorage.getInstance().reference.child("Person images")
        personVideoRef = FirebaseStorage.getInstance().reference.child("Person videos")

        personsDBRef = FirebaseDatabase.getInstance().reference.child("persons")

        binding.uploadPhoto.setOnClickListener {
            ImagePicker.with(this)
                .start()
        }

        binding.uploadVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            startActivityForResult(intent, 45)
        }

        binding.addButton.setOnClickListener {
            validateProductData()
        }
    }

    override fun onStart() {
        super.onStart()
        action = if(Constants.ACTIONS.getValue(Actions.EDIT) == intent.getStringExtra("action")){
            Actions.EDIT
        } else {
            Actions.ADD
        }

        initLanguageSpinner()
        initLangLevelSpinner()
        initSexSpinner()
        initCountrySpinner()

        if(action == Actions.EDIT){
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
            binding.nameET.setText(intent.getStringExtra("personName"))
            binding.priceET.setText(intent.getDoubleExtra("personCost", 0.0).toString())
            binding.CityEd.setText(person.city)
            binding.ageEd.setText(person.age.toString())
            Picasso.get().load(intent.getStringExtra("personImg")).into(binding.image)

            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding.personsVideo)
            binding.personsVideo.setMediaController(mediaController)
            binding.personsVideo.setVideoURI(Uri.parse(intent.getStringExtra("personVideo")))
            binding.personsVideo.start()



            downloadImageUrl=intent.getStringExtra("personImg").toString()
            downloadVideoUrl = intent.getStringExtra("personVideo").toString()

            binding.addButton.text = "Edit"
        } else {
            binding.addButton.text = "Добавить"
        }
    }

    private fun validateProductData() {
        name = binding.nameET.text.toString()
        costPerHour = binding.priceET.text.toString()
        city = binding.CityEd.text.toString()
        country = binding.spinnerCountry.selectedItem.toString()
        sex = binding.spinnerSex.selectedItem.toString()
        language = binding.spinnerLang.selectedItem.toString()
        langLevel = binding.spinnerLangLev.selectedItem.toString()
        age = binding.ageEd.text.toString()


        if (imageUri == null && action != Actions.EDIT) {
            Toast.makeText(this, "Add picture", Toast.LENGTH_SHORT).show()
        } else if (videoUri == null && action != Actions.EDIT) {
            Toast.makeText(this, "Add video", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Add name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(costPerHour)) {
            Toast.makeText(this, "Add price", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Add city", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "Add age", Toast.LENGTH_SHORT).show()
        }
        else {
            storeProductInfo()
        }
    }

    private fun storeProductInfo() {
        loadingBar.setTitle("Loading..");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(action == Actions.EDIT) {
            personRandomKey = intent.getStringExtra("personId").toString()
        } else {
            val calendar = Calendar.getInstance()

            val currentDateFormat = SimpleDateFormat("dd.MM.yyyy")
            var currentDate = currentDateFormat.format(calendar.time)

            val currentTimeFormat = SimpleDateFormat("HH.mm.ss")
            var currentTime = currentTimeFormat.format(calendar.time)

            personRandomKey = currentDate + currentTime
        }

        if(imageUri != null){
            val imageFilePath: StorageReference =
                personImageRef.child(imageUri?.lastPathSegment + personRandomKey)
            val uploadImageTask: UploadTask = imageFilePath.putFile(imageUri!!)
            uploadImageTask.addOnFailureListener {
                val message = it.toString()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(this, "Picture is loaded successfully", Toast.LENGTH_SHORT).show()

                uploadImageTask.continueWithTask(Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    downloadImageUrl = imageFilePath.downloadUrl.toString()
                    imageFilePath.downloadUrl
                }).
                addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        downloadImageUrl = task.result.toString()
                        if(action == Actions.EDIT){
                            saveProductInfoToDatabase()
                        }
                    }
                }
            }
        }

        if(videoUri != null) {
            val videoFilePath: StorageReference =
                personImageRef.child(videoUri?.lastPathSegment + personRandomKey)
            val uploadVideoTask: UploadTask = videoFilePath.putFile(videoUri!!)
            uploadVideoTask.addOnFailureListener {
                val message = it.toString()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(this, "Video is loaded successfully", Toast.LENGTH_SHORT).show()
                uploadVideoTask.continueWithTask(Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                   downloadVideoUrl = videoFilePath.downloadUrl.toString()
                    videoFilePath.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        downloadVideoUrl = task.result.toString()
//                        if(imageUri == null || action == Actions.ADD) {
//                            saveProductInfoToDatabase()
//                        }
                        if (action == Actions.EDIT){
                            saveProductInfoToDatabase()
                        }
                    }
                }
            }
        }
        else {
            saveProductInfoToDatabase()
        }
    }

    private fun saveProductInfoToDatabase() {
        val newPerson = Person(
            personRandomKey,
            name,
            age.toInt(),
            sex,
            city,
            country,
            costPerHour.toDouble(),
            langLevel,
            language,
            downloadImageUrl,
            downloadVideoUrl
        )

        if(action == Actions.EDIT) {
            personsDBRef.child(personRandomKey.replace(".", "")).setValue(newPerson.toMap())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        loadingBar.dismiss()
                        Toast.makeText(
                            this,
                            "Item is updated",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(
                            this,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        val message: String = it.exception.toString()
                        Toast.makeText(
                            this,
                            "Error: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingBar.dismiss()
                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 45 && resultCode == Activity.RESULT_OK) {
            videoUri = data?.data!!
            binding.personsVideo.setVideoURI(videoUri)
            binding.personsVideo.start()
        } else {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    imageUri = data?.data!!
                    binding.image.setImageURI(imageUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initLanguageSpinner() {
        val langAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, LANGUAGES.values.toMutableList()
        )
        binding.spinnerLang.adapter = langAdapter

        if(action == Actions.EDIT) {
            binding.spinnerLang.setSelection(
                LANGUAGES.values.indexOf(intent.getStringExtra("personLang").toString()))
        }

    }

    private fun initLangLevelSpinner() {
        val langLevelAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, LanguageLevel.values().toMutableList()
        )
        binding.spinnerLangLev.adapter = langLevelAdapter

        if(action == Actions.EDIT){
            binding.spinnerLangLev.setSelection(LanguageLevel.
                                valueOf(intent.getStringExtra("personLangLevel").toString()
                                    .uppercase(Locale.getDefault())).ordinal)
        }
    }

    private fun initSexSpinner() {
        val sexAdapter = ArrayAdapter(
            this, R.layout.simple_spinner_item,
            SEX.values.toMutableList()
        )
        binding.spinnerSex.adapter = sexAdapter
        if(action == Actions.EDIT) {
            binding.spinnerSex.setSelection(
                SEX.values.indexOf(intent.getStringExtra("personSex").toString()))
        }
        binding.spinnerSex.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { }
            override fun onNothingSelected(parent: AdapterView<*>) { }
        }
    }

    private fun initCountrySpinner() {
        val brandAdapter = ArrayAdapter(
            this, R.layout.simple_spinner_item,
           COUNTRIES.values.toMutableList()
        )
        binding.spinnerCountry.adapter = brandAdapter

        if(action == Actions.EDIT){
            binding.spinnerCountry.setSelection(COUNTRIES.values.indexOf(intent.getStringExtra("personCountry").toString()))
        }

    }
}