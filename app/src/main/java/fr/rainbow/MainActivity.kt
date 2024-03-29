package fr.rainbow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.rainbow.databinding.ActivityMainBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.functions.Functions
import fr.rainbow.ui.home.HomeFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object{
        var favorites: ArrayList<Favorite> = arrayListOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE)
        if(prefs.getBoolean("firstrun",true)){
            testInitFavorite()
            prefs.edit().putBoolean("firstrun", false).apply()
        }
        initFavorite()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        checkInternet()
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(this)
        thread {
            Thread.sleep(5000)
            while (true){
                if(networkManager.isNetworkAvailable.value){
                    //internet connection ok
                }else{
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this, "Vous n'avez pas de connexion internet", Toast.LENGTH_LONG).show()

                    }
                }
                Thread.sleep(10000)
            }
        }

    }

    private fun testInitFavorite(){
        favorites.add(Favorite("Your Position", 0.0 ,0.0, true, true,true,null,null))
        Functions.writeFile(this,favorites)
        Log.e("error","erreur je ne devrais pas être là")
    }

    private fun initFavorite(){
        favorites = Functions.readFile(this)
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val favorite = (data?.getSerializableExtra("favorite") as? Favorite)!!
            if(favorite.isFavorite && !favorites.contains(favorite)){
                favorites.add(favorite)
                Functions.writeFile(this,favorites)
            }else if(!favorite.isFavorite && favorites.contains(favorite)){
                favorites.remove(favorite)
                Functions.writeFile(this,favorites)
            }
            if(favorite.isGPS)
                updateHomeGps()
        }
    }

    fun openYourActivity(favoriteItem: Favorite) {
        val detailedIntent = Intent(this, DetailedActivity::class.java)
        detailedIntent.putExtra("favorite",favoriteItem)
        if (favoriteItem.weatherData != null) {
            resultLauncher.launch(detailedIntent)
        }
    }

    fun addGps(){
        var j = 0
        for (i in favorites){
            if (i.isGPS){
                j++
            }
        }
        if (j==0){
            val gps = Favorite("Your Position", 0.0 ,0.0, true, false,true,null,null)
            favorites.add(gps)
            Functions.writeFile(this,favorites)
            updateHomeGps()
        }else{
            Toast.makeText(this, R.string.samePos, Toast.LENGTH_LONG).show()
        }

    }

    private fun updateHomeGps() {
        val fm: FragmentManager = supportFragmentManager
        (fm.findFragmentByTag("HomeFragment") as HomeFragment?)?.initGps()
    }


}