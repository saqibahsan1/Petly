package com.lcpetlylgmg.petly.organization.post.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.databinding.ActivityNewPostBinding
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.organization.post.data.PostRepository
import com.lcpetlylgmg.petly.organization.post.data.PostViewModel
import com.lcpetlylgmg.petly.organization.post.data.PostViewModelFactory
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.Global.getImageUri
import com.lcpetlylgmg.petly.utils.TranslationHelper
import java.io.IOException
import java.util.Locale


class NewPostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    OnMultiSpinnerSelectionListener {

    private lateinit var binding: ActivityNewPostBinding
    private val countries = listOf("Germany", "Europe", "USA", "Mexico", "Marokko")
    private val readImagePermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    var permissions = arrayOf<String>(
        Manifest.permission.CAMERA, readImagePermission
    )
    private val PERMISSIONS_MULTIPLE_REQUEST = 123
    private var selectedCountry: String = ""
    private var imageUrl: String = ""
    private var postId: String = ""
    private var imageUri: Uri? = null
    private var selectedStates: List<String>? = null
    private lateinit var helper: PreferenceHelper
    private lateinit var postViewModel: PostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = PreferenceHelper.getPref(this)

        val repository = PostRepository()
        postViewModel =
            ViewModelProvider(this, PostViewModelFactory(repository))[PostViewModel::class.java]

        postId = Global.generateRandomUUID()


        binding.apply {
            image.setOnClickListener {
                if (checkPermissions()) {
                    showPictureDialog()
                }
            }
            buttonPost.setOnClickListener {
                binding.laoder.visibility = View.VISIBLE
                newPost()
            }
            buttonBack.setOnClickListener {
                finish()
            }
        }


        val adapterCountries = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapterCountries
        binding.spinnerCountry.onItemSelectedListener = this


    }

    private fun newPost() {
        binding.apply {
            val translationHelper = TranslationHelper()
            val currentLocale = resources.configuration.locale
            val germanLocale = Locale("de")
            val isGermanSelected = currentLocale == germanLocale


            val name = editTextName.text.toString()
            val breed = editTextBreed.text.toString()
            val age = editTextAge.text.toString()
            val city = editTextDogCity.text.toString()
            val about = editTextAbout.text.toString()


            val selectedIdForHuman = binding.humanRadioButton.checkedRadioButtonId
            val radioButtonHuman = findViewById<RadioButton>(selectedIdForHuman)
            val humanExperience = radioButtonHuman.text.toString()
            val translatedHumanExperience = translationHelper.translateToGerman(humanExperience)


            val selectedIdForChildren = binding.childrenRadioButton.checkedRadioButtonId
            val radioButtonChildren = findViewById<RadioButton>(selectedIdForChildren)
            val childExperience = radioButtonChildren.text.toString()
            val translatedChildExperience = translationHelper.translateToGerman(childExperience)


            val selectedIdForCats = binding.catRadioButton.checkedRadioButtonId
            val radioButtonCats = findViewById<RadioButton>(selectedIdForCats)
            val catsExperience = radioButtonCats.text.toString()
            val translatedCatsExperience = translationHelper.translateToGerman(catsExperience)


            val selectedIdForGender = binding.dogGenderRadioButton.checkedRadioButtonId
            val radioButtonGender = findViewById<RadioButton>(selectedIdForGender)
            val gender = radioButtonGender.text.toString()
            val translatedGender = translationHelper.translateToGerman(gender)


            val selectedIdForHandicap = binding.handicapRadioButton.checkedRadioButtonId
            val radioButtonHandicap = findViewById<RadioButton>(selectedIdForHandicap)
            val handicap = radioButtonHandicap.text.toString()
            val translatedHandicap = translationHelper.translateToGerman(handicap)


            val selectedIdForSize = binding.dogSizeRadioButton.checkedRadioButtonId
            val radioButtonSize = findViewById<RadioButton>(selectedIdForSize)
            val dogSize = radioButtonSize.text.toString()
            val translatedDogSize = translationHelper.translateToGerman(dogSize)

            val selectedIdForAgeRange = binding.ageRangeRadioButton.checkedRadioButtonId
            val radioButtonAgeRange = findViewById<RadioButton>(selectedIdForAgeRange)
            val ageRange = radioButtonAgeRange.text.toString()
            val translatedageRange = translationHelper.translateToGerman(ageRange)


            val postedBy = helper.getCurrentUser()?.userId
            val postedByName = helper.getCurrentUser()?.name
            val postedDate = Global.getCurrentUnixTimestamp()

            if (name.isEmpty()) {
                editTextName.error = getString(R.string.enter_name)
                return
            } else if (breed.isEmpty()) {
                editTextBreed.error = getString(R.string.enter_breed)
                return
            } else if (age.isEmpty()) {
                editTextAge.error = getString(R.string.enter_age)
                return
            } else if (city.isEmpty()) {
                editTextDogCity.error = getString(R.string.enter_city)
                return
            } else if (breed.isEmpty()) {
                editTextAbout.error = getString(R.string.enter_about)
                return
            } else {
                savePost(
                    name,
                    breed,
                    age,
                    city,
                    about,
                    if (isGermanSelected) humanExperience else translatedHumanExperience,
                    if (isGermanSelected) catsExperience else translatedCatsExperience,
                    if (isGermanSelected) childExperience else translatedChildExperience,
                    if (isGermanSelected) ageRange else translatedageRange,
                    if (isGermanSelected) gender else translatedGender,
                    if (isGermanSelected) handicap else translatedHandicap,
                    if (isGermanSelected) dogSize else translatedDogSize,
                    selectedCountry,
                    selectedStates,
                    postedBy,
                    postedByName,
                    postedDate,
                    postId
                )
            }

        }
    }

    private fun savePost(
        name: String,
        breed: String,
        age: String,
        city: String,
        about: String,
        humanExperience: String,
        catsExperience: String,
        childExperience: String,
        ageRange: String,
        gender: String,
        handicap: String,
        dogSize: String,
        selectedCountry: String,
        selectedStates: List<String>?,
        postedBy: String?,
        postedByName: String?,
        postedDate: Long,
        postId: String
    ) {


        if (imageUrl != "") {
            val post = Post(
                age,
                ageRange,
                breed,
                city,
                catsExperience,
                childExperience,
                selectedCountry,
                humanExperience,
                gender,
                name,
                dogSize,
                handicap,
                imageUrl,
                postId,
                postedBy,
                postedByName,
                about,
                postedDate,
                selectedStates
            )
            postViewModel.savePost(post) { success, errorMessage ->
                if (success) {
                    binding.laoder.visibility = View.GONE
                    showAlertDialog(this, getString(R.string.post_message_success), success)
                } else {
                    binding.laoder.visibility = View.GONE
                    showAlertDialog(
                        this,
                        getString(R.string.post_message_failed) + " " + errorMessage, success
                    )
                }
            }

        }

    }

    fun showAlertDialog(context: Context, message: String, success: Boolean) {
        AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK") { dialog, _ ->
            if (success) {
                dialog.dismiss()
                finish()
            } else {
                dialog.dismiss()
            }
        }.show()
    }

    private fun uploadImage(imageUri: Uri?, postId: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("Dogs/") // Change "images" to your desired storage path

        val imageRef =
            imagesRef.child("$postId.jpg") // Change "image.jpg" to the desired image name

        val uploadTask = imageUri?.let { imageRef.putFile(it) }
        uploadTask?.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    imageUrl = it.result.toString()
                } else {
                    binding.laoder.visibility = View.GONE
                    Log.d("TAG", "Failed to Download image ${it.result.toString()}")
                }
            }
            // Now you can use the imageUrl
        }?.addOnFailureListener {
            binding.laoder.visibility = View.GONE
            Log.d("TAG", "Failed to upload image ${it.message}")
        }


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, newPosition: Int, p3: Long) {

        selectedCountry = countries[newPosition]
        val states = when (countries[newPosition]) {
            "Germany" -> resources.getStringArray(R.array.states_germany).toList()
            "Europe" -> resources.getStringArray(R.array.states_europe).toList()
            "USA" -> resources.getStringArray(R.array.states_usa).toList()
            "Mexico" -> resources.getStringArray(R.array.states_mexico).toList()
            "Marokko" -> resources.getStringArray(R.array.states_morroko).toList()
            else -> emptyList()
        }

        binding.stateSpinner.setAdapterWithOutImage(this, states, this)
        binding.stateSpinner.initMultiSpinner(this, binding.stateSpinner)

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {

        selectedStates = chosenItems
        //This is where you get all your items selected from the Multi Selection Spinner :)
        for (i in chosenItems!!.indices) {
            Log.e("chosenItems", chosenItems[i])
        }
    }

    //camera work


    private fun showPictureDialog() {
        val options = arrayOf("Select photo from gallery", "Capture photo from camera")

        MaterialAlertDialogBuilder(this)
            .setTitle("Select Action")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        choosePhotoFromGallery()
                    }

                    1 -> {
                        takePhotoFromCamera()
                    }
                }
            }
            .setCancelable(true) // Set the dialog to be cancelable
            .show()
    }

    fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, UpdatePostActivity.GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, UpdatePostActivity.CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                imageUri = contentURI
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    binding.image.setImageBitmap(bitmap)
                    binding.image.imageTintMode = null
                    uploadImage(imageUri, postId)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@NewPostActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data?.extras!!.get("data") as Bitmap
            imageUri = getImageUri(applicationContext, thumbnail)
            binding.image.setImageBitmap(thumbnail)
            binding.image.imageTintMode = null
            uploadImage(imageUri, postId)
        }
    }


    private fun checkPermissions(): Boolean {
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            val result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, listPermissionsNeeded.toTypedArray(), PERMISSIONS_MULTIPLE_REQUEST
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_MULTIPLE_REQUEST -> {
                var allPermissionsGranted = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    }
                }

                if (allPermissionsGranted) {
                    // Both permissions granted.
                    showPictureDialog()
                } else {
                    // Handle the case where one or both permissions were not granted.
                    var perStr = ""
                    for (per in permissions) {
                        perStr += """
                    
                    $per
                    """.trimIndent()
                    } // permissions list of don't granted permission
                }
                return
            }
        }
    }

    companion object {
        const val GALLERY = 1
        const val CAMERA = 2
    }

}