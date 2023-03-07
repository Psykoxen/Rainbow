package fr.rainbow.ui.detailed

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.rainbow.R

class DetailedActivity : AppCompatActivity(){
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        val intent = getIntent()
        Log.d("DetailedActivity", intent.extras!!.getDouble("latitude").toString())
        Log.d("DetailedActivity", intent.extras!!.getDouble("longitude").toString())
    }

}