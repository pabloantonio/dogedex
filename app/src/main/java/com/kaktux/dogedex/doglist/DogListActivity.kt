package com.kaktux.dogedex.doglist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kaktux.dogedex.api.responses.ApiResponseStatus
import com.kaktux.dogedex.databinding.ActivityDogListBinding
import com.kaktux.dogedex.dogdetail.DogDetailActivity
import com.kaktux.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY

private const val GRID_SPAN_COUNT = 3

class DogListActivity : AppCompatActivity() {

    /**
     * Esto se usa gracias a las dependecnias añadidas sin tener que ionstanciar el viewModel
     */
    private val dogListViewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loadingWheel = binding.loadingWheel
        val recycler = binding.dogRecycler
        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        val adapter = DogAdapter()

        adapter.setOnItemClickListener {
            // pasar el dog a dogdetail activity
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }

        adapter.setLongOnItemClickListener {
            dogListViewModel.addDogToUser(it.id)
        }
        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this) {
            adapter.submitList(it)
        }

        dogListViewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Success -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, "Datos disponibles", Toast.LENGTH_LONG).show()
                }
                is ApiResponseStatus.Loading -> {
                    loadingWheel.visibility = View.VISIBLE
                }
                is ApiResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
