package com.example.tugasfirebase.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasfirebase.database.Report
import com.example.tugasfirebase.databinding.ActivitySecondBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var executorService: ExecutorService
    private var updateId: String = ""
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()

        // nampilin ui
        setupUI()

        // fungsi membawa data dari main activity ke second activity
        val selectedNote = intent.getParcelableExtra<Report>("SELECTED_NOTE")
        selectedNote?.let {
            updateUIWithSelectedNoteData(selectedNote)
            updateId = selectedNote.id
        }

    }

    private fun setupUI() {
        with(binding) {
            btnnn.setOnClickListener {
                val nama = txtName.text.toString()
                val judul = txtTitle.text.toString()
                val isi = txtDesc.text.toString()
                val newBudget = Report(nama = nama, judul = judul, isi = isi)
                addReport(newBudget)
                setEmptyField()
            }

            btnUpdate.setOnClickListener {
                val nama = txtName.text.toString()
                val judul = txtTitle.text.toString()
                val isi = txtDesc.text.toString()
                val reportToUpdate = Report(nama = nama, judul = judul, isi = isi)
                updateReport(reportToUpdate)
                updateId = ""
                setEmptyField()
            }
        }
    }

    private fun updateUIWithSelectedNoteData(selectedNote: Report) {
        with(binding) {
            txtName.setText(selectedNote.nama)
            txtTitle.setText(selectedNote.judul)
            txtDesc.setText(selectedNote.isi)
        }
    }

    private fun setEmptyField() {
        with(binding) {
            txtTitle.setText("")
            txtName.setText("")
            txtDesc.setText("")
        }
    }
    private fun addReport(report: Report) {
        db.collection("report")
            .add(report)
            .addOnSuccessListener { documentReference->
                val createdBudgetId = documentReference.id
                report.id = createdBudgetId
                documentReference.set(report)
                    .addOnFailureListener {
                        Log.d("MainActivity", "Error updating budget ID: ", it)
                    }
                val intent = Intent(this@SecondActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
    private fun updateReport(report: Report) {
        if (updateId.isNotEmpty()) {
            // Get the document reference for the report to update
            val reportCollectionRef = db.collection("report")
            val reportRef = reportCollectionRef.document(updateId)

            // Use a map to update specific fields, in this case, 'nama', 'judul', and 'isi'
            val updates = mapOf(
                "nama" to report.nama,
                "judul" to report.judul,
                "isi" to report.isi
                // Add more fields if needed
            )

            // Update the document with the provided data
            reportRef.update(updates)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Report updated successfully!")
                    val intent = Intent(this@SecondActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w("MainActivity", "Error updating report", e)
                }
        } else {
            Log.e("MainActivity", "Error updating report: updateId is empty!")
        }
    }
}