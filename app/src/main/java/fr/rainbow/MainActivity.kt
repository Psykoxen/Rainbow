package fr.rainbow

import android.content.Context
import android.os.Bundle
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
        if (getPreferences(Context.MODE_PRIVATE).getBoolean("firstTime", true)) {
            testInitFavorite()
        }
        initFavorite()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    fun testInitFavorite(){
        val fav0 = Favorite("Your Position", 0.0 ,0.0, true, true,null)
        val fav1 = Favorite("Villeurbanne",45.786,4.883,false,false,null)
        val fav2 = Favorite("Montpellier",43.610,3.876,false,false,null)
        val fav3 = Favorite("Agde",43.309,3.475,false,false,null)
        val fav4 = Favorite("Marseille",43.296,5.369,false,false,null)
        val fav5 = Favorite("somewhere",42.0,6.0,false,false,null)
        favorites.add(fav0)
        favorites.add(fav1)
        favorites.add(fav2)
        favorites.add(fav3)
        favorites.add(fav4)
        favorites.add(fav5)
        Functions.writeFile(this,favorites)
    }

    fun initFavorite(){
        favorites = Functions.readFile(this)
    }

}