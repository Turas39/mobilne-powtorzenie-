package com.example.powtorzeniemobilek

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.rasa_spinner)
        val rasy = arrayOf("Hobbit",
            "Człowiek",
            "Elf",
            "Krasnolud")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            rasy
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDecyzje = rasy[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val fotaPozdro: ImageView = findViewById(R.id.rasa_fota)

        val images = listOf(
            R.drawable.hobbit,
            R.drawable.czlek,
            R.drawable.elf,
            R.drawable.krasnolud,
        )

        fotaPozdro.setImageResource(R.drawable.hobbit)

        when (spinner) {
            "Hobbit" -> {

            }
            }
        }

    }
}