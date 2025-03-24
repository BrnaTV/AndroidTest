package com.example.androidtest

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.view.ContextMenu  // Dodano
import android.view.View  // Dodano
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private var counter = 0
    private lateinit var textViewCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // The Toolbar defined in the layout has the id "my_toolbar".
        setSupportActionBar(findViewById(R.id.my_toolbar))

        textViewCounter = findViewById(R.id.textViewCounter)
        val UpButton = findViewById<Button>(R.id.buttonUp)
        val downButton = findViewById<Button>(R.id.buttonDown)

        // Učitavanje spremljene vrijednosti iz SharedPreferences
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        counter = sharedPreferences.getInt("COUNTER_VALUE", 0)
        textViewCounter.text = counter.toString()

        UpButton.setOnClickListener {
            counter++
            if(counter == 10) {
                counter = 0
                // Provjeri da li je ime uneseno
                val nameText = findViewById<TextView>(R.id.plainTextName)?.text.toString()

                // Provjeri da li ime nije prazno prije nego pošaljemo
                if (nameText.isNotEmpty()) {
                    val intent = Intent(this, SuccessActivity::class.java).apply {
                        putExtra("name", nameText) // Poslali smo ime u Intent
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Molimo unesite ime!", Toast.LENGTH_SHORT).show()
                }
            }
            textViewCounter.text = counter.toString()
        }

        // Registracija kontekstualnog izbornika za textViewCounter
        registerForContextMenu(textViewCounter)
    }

    // Spremanje trenutnog brojača prilikom promjene orijentacije
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("COUNTER_VALUE", counter)
    }

    // Vraćanje vrijednosti brojača nakon promjene orijentacije
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        counter = savedInstanceState.getInt("COUNTER_VALUE", 0)
        textViewCounter.text = counter.toString()
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(applicationContext, "onStart", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(applicationContext, "onResume", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(applicationContext, "onPause", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "onPause")
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("COUNTER_VALUE", counter)
            apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "onDestroy", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(applicationContext, "onRestart", Toast.LENGTH_SHORT).show()
        Log.i("MyLog", "onRestart")
    }

    // Inflacija izbornika u akcijskoj traci
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Odabir opcije iz izbornika
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.restore_counter -> {
                counter = 0
                textViewCounter.text = counter.toString()
                return true
            }

            // Dodavanje opcija za promjenu jezika
            R.id.croatian -> {
                changeLanguage(this, "hr")  // Pozivanje funkcije za promjenu jezika na hrvatski
                Toast.makeText(this, "Jezik promijenjen na Hrvatski", Toast.LENGTH_SHORT).show()
                recreate()  // Ponovno učitaj aktivnost kako bi se primijenile promjene
                return true
            }
            R.id.english -> {
                changeLanguage(this, "en")  // Pozivanje funkcije za promjenu jezika na engleski
                Toast.makeText(this, "Language changed to English", Toast.LENGTH_SHORT).show()
                recreate()  // Ponovno učitaj aktivnost kako bi se primijenile promjene
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Funkcija za promjenu jezika
    @Suppress("DEPRECATION")
    fun changeLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        context.createConfigurationContext(config)  // Ovo osigurava da se promjena jezika primijeni
        res.updateConfiguration(config, res.displayMetrics)
    }

    // KONTEKSTUALNI IZBORNIK
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_float, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reset_counter -> {
                counter = 0
                textViewCounter.text = counter.toString()
                Toast.makeText(this, "Brojač resetiran!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
