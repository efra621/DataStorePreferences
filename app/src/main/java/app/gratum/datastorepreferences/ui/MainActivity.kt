package app.gratum.datastorepreferences.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.gratum.datastorepreferences.R
import app.gratum.datastorepreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el contenedor con el Fragment
        val userFragment = UserFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, userFragment)
            .commit()
    }
}