package com.mpd.pmdm.toppappbar2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.mpd.pmdm.toppappbar2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //La inflo manualmente
        binding.topAppBar.inflateMenu(R.menu.top_menu)
        //Le asocio un appBarConfiguration para que gestione las etiquetas y el botón arriba
        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar
            .setupWithNavController(navController, appBarConfiguration)

        //Le doy comportamienmto a los items en una función creada por mi
        binding.topAppBar.setOnMenuItemClickListener {
            miMenuNoActionOnClickItem(it)
        }
    }

    fun miMenuNoActionOnClickItem(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }






}