package com.mpd.pmdm.toppappbar2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import com.mpd.pmdm.toppappbar2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //La inflo manualmente
        binding.topAppBar.inflateMenu(R.menu.top_menu)
        //Le doy comportamienmto a los items en una función creada por mi
        binding.topAppBar.setOnMenuItemClickListener {
            miMenuNoActionOnClickItem(it)
        }

        //Tampoco la conecto al gráfico de  navegación para manejar el título o el botón arriba
    }

    fun miMenuNoActionOnClickItem(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.profileFragment -> {
                findNavController(R.id.nav_host).navigate(R.id.profileFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }






}