package fr.rainbow

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.rainbow.databinding.ActivityMainBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.functions.Functions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var favorites: ArrayList<Favorite> = arrayListOf()
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
    }

    fun testInitFavorite(){
        val fav0 = Favorite("Your Position", 0.0 ,0.0, true, true,true,null)
        val fav1 = Favorite("Mont Blanc",45.832622,6.865175,false,false,true,null)
        val fav2 = Favorite("Villeurbanne",45.786,4.883,false,false,true,null)
        val fav3 = Favorite("Montpellier",43.610,3.876,false,false,true,null)
        val fav4 = Favorite("Agde",43.309,3.475,false,false,true,null)
        val fav5 = Favorite("Marseille",43.296,5.369,false,false,true,null)
        val fav6 = Favorite("somewhere",42.0,6.0,false,false,true,null)
        favorites.add(fav0)
        favorites.add(fav1)
        favorites.add(fav2)
        favorites.add(fav3)
        favorites.add(fav4)
        favorites.add(fav5)
        favorites.add(fav6)
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
        }
        val test = Functions.readFile(this)
        Log.d("test",test.size.toString())
    }
    fun openYourActivity(favoriteItem: Favorite) {
        val detailedIntent = Intent(this, DetailedActivity::class.java)
        detailedIntent.putExtra("favorite",favoriteItem)
        resultLauncher.launch(detailedIntent)
    }
}