package com.lcpetlylgmg.petly.adopt.match.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.adopt.match.data.Match
import com.lcpetlylgmg.petly.databinding.ActivitySetCharacteristicBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.TranslationHelper
import java.util.Locale

class SetCharacteristicActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {

    private lateinit var binding: ActivitySetCharacteristicBinding
    private val countries = listOf("Germany", "Europe", "USA", "Mexico", "Marokko")
    private var selectedStates: List<String>? = null
    private var selectedCountry: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetCharacteristicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val match = PreferenceHelper.getPref(this).getCurrentCharacteristics()
        if (match != null) {
            val translationHelper = TranslationHelper()
            val currentLocale = resources.configuration.locale
            val germanLocale = Locale("de", "DE")
            val isGermanSelected = currentLocale == germanLocale

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.humanRadioButton,
                    translationHelper.translateToEnglish(match.dogExperience!!)
                ) else setSelectedRadioOption(binding.humanRadioButton, match.dogExperience!!)

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.childrenRadioButton,
                    translationHelper.translateToEnglish(match.compatibleWithKids!!)
                ) else setSelectedRadioOption(
                binding.childrenRadioButton,
                match.compatibleWithKids!!
            )

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.catRadioButton,
                    translationHelper.translateToEnglish(match.compatibleWithCats!!)
                ) else setSelectedRadioOption(binding.catRadioButton, match.compatibleWithCats!!)

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.dogGenderRadioButton,
                    translationHelper.translateToEnglish(match.dogGender!!)
                ) else setSelectedRadioOption(binding.dogGenderRadioButton, match.dogGender!!)

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.handicapRadioButton,
                    translationHelper.translateToEnglish(match.handicapDog!!)
                ) else setSelectedRadioOption(binding.handicapRadioButton, match.handicapDog!!)

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.dogSizeRadioButton,
                    translationHelper.translateToEnglish(match.dogSize!!)
                ) else setSelectedRadioOption(binding.dogSizeRadioButton, match.dogSize!!)

            if (!isGermanSelected)
                setSelectedRadioOption(
                    binding.ageRangeRadioButton,
                    translationHelper.translateToEnglish(match.ageRange!!)
                ) else setSelectedRadioOption(binding.ageRangeRadioButton, match.ageRange!!)


        }

        val adapterCountries = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapterCountries
        binding.spinnerCountry.onItemSelectedListener = this


        if (match != null) {

            val position = adapterCountries.getPosition(match.country)

            if (position != -1) {
                binding.spinnerCountry.setSelection(position)
            }

        }


        binding.buttonBack.setOnClickListener { finish() }

        binding.apply {
            button.setOnClickListener {
                val translationHelper = TranslationHelper()

                val currentLocale = resources.configuration.locale
                val germanLocale = Locale("de")
                val isGermanSelected = currentLocale == germanLocale

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


                val match = Match(

                    if (isGermanSelected) ageRange else translatedageRange,
                    if (isGermanSelected) catsExperience else translatedCatsExperience,
                    if (isGermanSelected) childExperience else translatedChildExperience,
                    selectedCountry,
                    if (isGermanSelected) humanExperience else translatedHumanExperience,
                    if (isGermanSelected) gender else translatedGender,
                    if (isGermanSelected) dogSize else translatedDogSize,
                    if (isGermanSelected) handicap else translatedHandicap,
                    selectedStates
                )
                returnSelectedModelToFragment(match)
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, newPosition: Int, p3: Long) {

        selectedCountry = countries[newPosition]
        val states = when (countries[newPosition]) {
            "Germany" -> resources.getStringArray(R.array.states_germany).toList()
            "Europe" -> resources.getStringArray(R.array.states_europe).toList()
            "USA" -> resources.getStringArray(R.array.states_usa).toList()
            "Mexico" -> resources.getStringArray(R.array.states_usa).toList()
            "Marokko" -> resources.getStringArray(R.array.states_usa).toList()
            else -> emptyList()
        }
        binding.stateSpinner.setAdapterWithOutImage(this, states, this)
        binding.stateSpinner.initMultiSpinner(this, binding.stateSpinner)


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {

        selectedStates = chosenItems
        for (i in chosenItems!!.indices) {
            Log.e("chosenItems", chosenItems[i])
        }
    }

    private fun returnSelectedModelToFragment(selectedModel: Match) {
        val resultIntent = Intent()
        PreferenceHelper.getPref(this).saveCharacteristics(selectedModel)
        resultIntent.putExtra("selectedModel", selectedModel)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    fun setSelectedRadioOption(radioGroup: RadioGroup, selectedValue: String) {
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            val buttonText = radioButton.text.toString()
            if (buttonText == selectedValue) {
                radioButton.isChecked = true
                return
            }
        }
    }
}