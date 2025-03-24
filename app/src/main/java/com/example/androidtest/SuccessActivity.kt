package com.example.androidtest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SuccessActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var editTextMessage: EditText
    lateinit var sendSMSButton: Button
    lateinit var radioGroup: RadioGroup
    lateinit var radioButtonPhone1: RadioButton
    lateinit var radioButtonPhone2: RadioButton
    lateinit var radioButtonPhone3: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        textView = findViewById(R.id.textView)
        editTextMessage = findViewById(R.id.textView) // Koristi EditText umjesto TextView
        sendSMSButton = findViewById(R.id.buttonSendSMS)
        radioGroup = findViewById(R.id.radioGroup)
        radioButtonPhone1 = findViewById(R.id.radioButtonPhone1)
        radioButtonPhone2 = findViewById(R.id.radioButtonPhone2)
        radioButtonPhone3 = findViewById(R.id.radioButtonPhone3)

        val name = intent.getStringExtra("name")
        textView.text = "$name, uspješno ste došli do 10 koraka."

        sendSMSButton.setOnClickListener {
            sendSMS()
        }
    }

    private fun sendSMS() {
        val selectedPhoneNumber = when (radioGroup.checkedRadioButtonId) {
            R.id.radioButtonPhone1 -> radioButtonPhone1.text.toString()
            R.id.radioButtonPhone2 -> radioButtonPhone2.text.toString()
            R.id.radioButtonPhone3 -> radioButtonPhone3.text.toString()
            else -> {
                Toast.makeText(this, "Molimo odaberite broj telefona!", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val message = editTextMessage.text.toString()

        if (message.isEmpty()) {
            Toast.makeText(this, "Unesite tekst poruke!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$selectedPhoneNumber")
            putExtra("sms_body", message)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Ne mogu pokrenuti SMS aplikaciju!", Toast.LENGTH_SHORT).show()
        }
    }
}
