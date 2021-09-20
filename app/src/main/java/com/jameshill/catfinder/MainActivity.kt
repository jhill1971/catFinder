package com.jameshill.catfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.jameshill.catfinder.BuildConfig
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var pussyView = ImageView

    //the server URL endpoint
    private val serverUrl = "https://api.thecatapi.com/v1/"

    //api key
    private val apiKey = "15f9da01-5372-4552-a0da-ed3caf1bd7e0"

    private val compositeDisposableOnPause = CompositeDisposable()

    private var latestCatCall: Disposable? = null

    // OnCreate Function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.pussyGetter)
        pussyView = findViewById(R.id.pussyView)
        //OnClickListener
         button.setOnClickListener{
            getSomePussy()
        }
    }

    //The API Call
    private fun getSomePussy() {
        //Initialize the repository class with the necessary information.
        val catsRepository = CatsRepository(serverUrl, BuildConfig.DEBUG, apiKey)

        //Stop the last call if its already running.
        latestCatCall?.dispose()

        //Perform API call
        //asking for 10 cats. Passing null for category, category type unimportant.
        latestCatCall =
            catsRepository.getNumberOfRandomCats(10, null).subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    compositeDisposableOnPause.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when {
                        result.hasError() -> result.errorMessage?.let {
                            Toast.makeText(
                                this@MainActivity,
                                "Error getting cats$it",
                                Toast.LENGTH_SHORT).show()
                        }
                            ?: run {
                                Toast.makeText(this@MainActivity, "Null error", Toast.LENGTH_SHORT).show()
                            }
                        result.hasCats() -> result.catData?.let {
                            Toast.makeText(this@MainActivity, "Cats received!", Toast.LENGTH_SHORT).show()
                            withContext(Dispatchers.Main){
                                Glide.with (applicationContext).load(result.url).into(pussyView)
                                pussyView.visibility= View.VISIBLE
                        }
                            ?: run {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Null list of cats",
                                    Toast.LENGTH_SHORT).show()
                            }
                        else -> Toast.makeText(
                            this@MainActivity,
                            "No cats available :(",
                            Toast.LENGTH_SHORT).show()
                    }
                }
    }

    // Killing all background threads (if any exist) cause they don't deserve to live when the activity is not running
    private fun clearAllJobsOnPause() {
        compositeDisposableOnPause.clear()
    }

    // onPause! Stop everything, the user is probably checking memes elsewhere
    override fun onPause() {
        clearAllJobsOnPause()
        super.onPause()
    }
}
