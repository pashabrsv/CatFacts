package ru.apps.rtrofitfacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.apps.rtrofitfacts.databinding.ActivityMainBinding

const val BASE_URL =  "https://cat-fact.herokuapp.com"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentData()


        binding.layoutGenerateNewFact.setOnClickListener{
            getCurrentData()
        }

    }

    private fun getCurrentData(){

        binding.tvTextView.visibility = View.INVISIBLE
        binding.tvTimeStamp.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCatFacts().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d(TAG, data.text)

                    withContext(Dispatchers.Main) {
                        binding.tvTextView.visibility = View.VISIBLE
                        binding.tvTimeStamp.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        binding.tvTextView.text = data.text
                        binding.tvTimeStamp.text = data.createdAt
                    }
                }
            } catch (e: java.lang.Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, "Интернета, видимо, нет. Попробуй нажать на экран снова", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}