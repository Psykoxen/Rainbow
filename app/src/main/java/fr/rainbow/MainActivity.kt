package fr.rainbow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.rainbow.databinding.ActivityMainBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.functions.file

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var favorites: ArrayList<Favorite> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testInitFavorite()
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
        val fav2 = Favorite("somewhere",42.0,6.0,false,false,null)
        favorites.add(fav0)
        favorites.add(fav1)
        favorites.add(fav2)
        file.writeFile(this,favorites)
    }

    fun initFavorite(){
        favorites = file.readFile(this)
    }

}