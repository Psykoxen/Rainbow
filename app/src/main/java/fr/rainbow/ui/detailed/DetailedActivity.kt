package fr.rainbow.ui.detailed


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.rainbow.databinding.ActivityDetailedBinding
import fr.rainbow.databinding.ActivityMainBinding


class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}