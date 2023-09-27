package com.lcpetlylgmg.petly.adopt.match.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.databinding.ActivityMatchDetailsBinding
import com.lcpetlylgmg.petly.utils.GlobalKeys

class MatchDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = intent.getParcelableExtra<Post>(GlobalKeys.POST)

        if (post != null) {
            binding.apply {
                post.dogName.let {
                    name.text = it
                }
                post.breed.let {
                    breed.text = it
                }
                post.age.let {
                    age.text = it
                }
                post.dogExperience.let {
                    dogExperience.text = it
                }
                post.dogGender.let {
                    dogGender.text = it
                }
                post.dogSize.let {
                    dogSize.text = it
                }
                post.aboutDog.let {
                    dogAbout.text = it
                }
                post.country.let {
                    dogCountry.text = it
                }
                post.ageRange.let {
                    ageRange.text = it
                }
                post.compatibleWithCats.let {
                    catExperience.text = it
                }
                post.compatibleWithKids.let {
                    childExperience.text = it
                }
                post.handicapDog.let {
                    handicapDog.text = it
                }
                post.city.let {
                    dogCity.text = it
                }
                post.state.let {
                    if (it != null) {
                        for (item in it) {
                            dogStates.append(item)
                        }

                    }
                }

                Glide.with(this@MatchDetailsActivity).load(post.imageUrl).into(image)



            }
        }
        binding.buttonBack.setOnClickListener { finish() }
    }
}