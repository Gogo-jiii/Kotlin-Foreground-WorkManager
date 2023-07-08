package com.example.foregroundworkmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.foregroundworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var foregroundWorkRequest: WorkRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStartwork.setOnClickListener {
            Toast.makeText(this, "Starting foreground work!", Toast.LENGTH_SHORT).show()

            foregroundWorkRequest = OneTimeWorkRequest.Builder(MyForegroundWork::class.java)
                .addTag("foregroundwork" + System.currentTimeMillis())
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.SECONDS
                )
                .build()
            WorkManager.getInstance(this).enqueue(foregroundWorkRequest!!)
        }

        binding.btnStopWork.setOnClickListener {
            MyForegroundWork.isStopped = true

            val operation: Operation = WorkManager.getInstance(this).cancelWorkById(
                foregroundWorkRequest!!.id
            )

            Toast.makeText(
                this, "Stopping foreground work!" + operation.state,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}