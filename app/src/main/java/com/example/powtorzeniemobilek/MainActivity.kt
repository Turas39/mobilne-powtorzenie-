package com.example.powtorzeniemobilek

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.logging.Handler


class MainActivity : AppCompatActivity() {
    private var data = ""
    private var godzina = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val podsumowanie = findViewById<TextView>(R.id.podsumowanie)

        val spinner = findViewById<Spinner>(R.id.rasa_spinner)
        val rasy = arrayOf("Hobbit", "Człowiek", "Elf", "Krasnolud")
        val images = listOf(R.drawable.hobbit, R.drawable.czlek, R.drawable.elf, R.drawable.krasnolud)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, rasy)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val fotaPozdro: ImageView = findViewById(R.id.rasa_fota)
        fotaPozdro.setImageResource(images[0])

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fotaPozdro.setImageResource(images[position])
                aktualizujPodsumowanie()  // <- aktualizacja po wyborze rasy
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val dateButton = findViewById<Button>(R.id.date_button)
        val timeButton = findViewById<Button>(R.id.time_button)
        val textview_data = findViewById<TextView>(R.id.data_textview)



        dateButton.setOnClickListener {
            val picker = DatePickerDialog(this)
            picker.setOnDateSetListener { _, year, month, day ->
                data = String.format("%02d.%02d.%d", day, month + 1, year)
                if (godzina.isNotEmpty()) {
                    textview_data.text = "Wyruszasz: $data o $godzina"
                }
                aktualizujPodsumowanie()  // <- aktualizacja po wyborze daty
            }
            picker.show()
        }

        timeButton.setOnClickListener {
            val picker = TimePickerDialog(this, { _, hour, minute ->
                godzina = String.format("%02d:%02d", hour, minute)
                if (data.isNotEmpty()) {
                    textview_data.text = "Wyruszasz: $data o $godzina"
                }
                aktualizujPodsumowanie()  // <- aktualizacja po wyborze godziny
            }, 7, 30, true)
            picker.show()
        }

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val seekBarInfo = findViewById<TextView>(R.id.seekbarinfo)
        seekBarInfo.text = "Czas marszu: ${seekBar.progress} min"

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarInfo.text = "Czas marszu: $progress min"
                aktualizujPodsumowanie()  // <- aktualizacja po zmianie SeekBar
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val startButton = findViewById<Button>(R.id.start_button)
        val stopButton = findViewById<Button>(R.id.stop_button)
        val chronometer = findViewById<Chronometer>(R.id.myChronometr)
        var isRunning = false

        startButton.setOnClickListener {
            if (!isRunning) {
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                isRunning = true
            }
        }
        stopButton.setOnClickListener {
            if (isRunning) {
                chronometer.stop()
                isRunning = false
            }
        }

        val wymrarsz_button = findViewById<Button>(R.id.button_wymarsz)
        val progressBar = findViewById<ProgressBar>(R.id.myProgressBar)
        val timerText = findViewById<TextView>(R.id.timerCounter)

        progressBar.max = 30
        progressBar.progress = 0

        wymrarsz_button.setOnClickListener {
            progressBar.progress = 0
            object : CountDownTimer(30_000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val elapsed = 30 - (millisUntilFinished / 1000).toInt()
                    val secondsLeft = millisUntilFinished / 1000
                    progressBar.progress = elapsed
                    timerText.text = "Pozostało $secondsLeft sekund"
                }
                override fun onFinish() {
                    progressBar.progress = 30
                    timerText.text = "Czas wyruszyć z Rivendell!"
                    Toast.makeText(applicationContext, "Czas wyruszyć z Rivendell!", Toast.LENGTH_LONG).show()
                }
            }.start()
        }

        val ratingBar = findViewById<RatingBar>(R.id.my_ratingbar)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                podsumowanie.text = "Ocena morale drużyny: $rating / ${ratingBar.numStars}"
                aktualizujPodsumowanie()  // <- aktualizacja po zmianie ratingu
            }
        }

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)
        radioGroup.setOnCheckedChangeListener { _, _ ->
            aktualizujPodsumowanie()  // <- aktualizacja po zmianie priorytetu
        }

        val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
        val checkBox3 = findViewById<CheckBox>(R.id.checkBox3)

        val checkBoxes = listOf(checkBox1, checkBox2, checkBox3)
        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                aktualizujPodsumowanie()  // <- aktualizacja po zmianie checkboxów
            }
        }

        val imieEditText = findViewById<EditText>(R.id.imie)
        imieEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) { // gdy tracimy focus (np. po wpisaniu)
                aktualizujPodsumowanie()
            }
        }
    }

    private fun aktualizujPodsumowanie() {
        val imie =
            findViewById<EditText>(R.id.imie).text.toString().ifEmpty { "Nie podano imienia" }

        val spinner = findViewById<Spinner>(R.id.rasa_spinner)
        val rasa = spinner.selectedItem.toString()

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)
        val priorytetId = radioGroup.checkedRadioButtonId
        val priorytet = when (priorytetId) {
            R.id.radiobutton_ukryty -> "Ukryty"
            R.id.radiobutton_zbalansowany -> "Zbalansowany"
            R.id.radiobutton_forsowny -> "Forsowny"
            else -> "Brak"
        }

        val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
        val checkBox3 = findViewById<CheckBox>(R.id.checkBox3)
        val wyposazenieList = mutableListOf<String>()
        if (checkBox1.isChecked) wyposazenieList.add(checkBox1.text.toString().capitalize())
        if (checkBox2.isChecked) wyposazenieList.add(checkBox2.text.toString().capitalize())
        if (checkBox3.isChecked) wyposazenieList.add(checkBox3.text.toString().capitalize())
        val wyposazenie =
            if (wyposazenieList.isEmpty()) "Brak" else wyposazenieList.joinToString(", ")

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val czasMarszu = seekBar.progress

        val ratingBar = findViewById<RatingBar>(R.id.my_ratingbar)
        val morale = ratingBar.rating



        val podsumowanie = findViewById<TextView>(R.id.podsumowanie)

        podsumowanie.text = "Bohater: $imie ($rasa)\n" +
                "Priorytet: $priorytet\n" +
                "Wyposażenie: $wyposazenie\n" +
                "Czas marszu: $czasMarszu min • Morale: ${morale.toInt()}/${ratingBar.numStars.toInt()}\n" +
                "Termin: $data o $godzina"
    }
}


