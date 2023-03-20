package fr.rainbow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
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

        val prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        if(prefs.getBoolean("firstrun",true)){
            testInitFavorite()
            prefs.edit().putBoolean("firstrun", false).commit();
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

    fun testInitFavorite(){
        val fav0 = Favorite("Your Position", 0.0 ,0.0, true, true,true,null,null)
        val fav1 = Favorite("Mont Blanc",45.832622,6.865175,false,false,true,null,null)
        val fav2 = Favorite("Villeurbanne",45.786,4.883,false,false,true,null,null)
        val fav3 = Favorite("Montpellier",43.610,3.876,false,false,true,null,null)
        val fav4 = Favorite("Agde",43.309,3.475,false,false,true,null,null)
        val fav5 = Favorite("Marseille",43.296,5.369,false,false,true,null,null)
        favorites.add(fav0)
        favorites.add(fav1)
        favorites.add(fav2)
        favorites.add(fav3)
        favorites.add(fav4)
        favorites.add(fav5)
        Functions.writeFile(this,favorites)
        Log.e("error","erreur je ne devrais pas être là")
    }

    fun initFavorite(){
        favorites = Functions.readFile(this)
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
            Log.d("test",favorite.toString())
        }
    }

    fun openYourActivity(favoriteItem: Favorite) {
        val detailedIntent = Intent(this, DetailedActivity::class.java)
        detailedIntent.putExtra("favorite",favoriteItem)
        resultLauncher.launch(detailedIntent)
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
            Toast.makeText(this, "Impossible d'avoir plusieurs fois sa position", Toast.LENGTH_LONG).show()

        }

    }

    private fun updateHomeGps(){
        val fm: FragmentManager = supportFragmentManager
        val fragment: HomeFragment? = fm.findFragmentByTag("HomeFragment") as HomeFragment?
        if (fragment != null) {
            fragment.initGps()
        };
    }


}