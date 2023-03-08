package fr.rainbow

import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import favorite
import fr.rainbow.databinding.ActivityMainBinding
import fr.rainbow.functions.file

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var favorites: ArrayList<favorite> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //testInitFavorite()
        initFavorite()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    fun testInitFavorite(){
        val fav1 = favorite("Villeurbane",45.786,4.883)
        val fav2 = favorite("somewhere",42.0,6.0)
        favorites.add(fav1)
        favorites.add(fav2)
        file.writeFile(this,favorites)
    }

    fun initFavorite(){
        file.readFile(this)
    }
}