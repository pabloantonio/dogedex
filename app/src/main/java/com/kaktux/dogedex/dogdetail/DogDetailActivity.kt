package com.kaktux.dogedex.dogdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.kaktux.dogedex.R
import com.kaktux.dogedex.databinding.ActivityDogDetailBinding
import com.kaktux.dogedex.model.Dog

class DogDetailActivity : AppCompatActivity() {
    companion object {
        const val DOG_KEY = "dog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent?.extras?.getParcelable<Dog>(DOG_KEY).let {
            binding.dogIndex.text = getString(R.string.dog_index_format, it?.index)
            binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, it?.lifeExpectancy)
            binding.dogImage.load(it?.imageUrl)
            binding.closeButton.setOnClickListener {
                finish()
            }
            binding.dog = it
        }
    }
}
