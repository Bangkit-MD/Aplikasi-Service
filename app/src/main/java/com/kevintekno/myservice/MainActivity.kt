package com.kevintekno.myservice

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kevintekno.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        val foregroundServiceIntent = Intent(this, MyForegoundService::class.java)
        binding.btnStartBackgroundService.setOnClickListener { startService(serviceIntent) }
        binding.btnStopBackgroundService.setOnClickListener { stopService(serviceIntent) }

        binding.btnStartForegroundService.setOnClickListener {onForegroundStart(foregroundServiceIntent) }
        binding.btnStopForegroundService.setOnClickListener { onForegroundStop(foregroundServiceIntent) }
    }
    private fun onForegroundStart(foregroundServiceIntent:Intent){

        if(Build.VERSION.SDK_INT >= 26){
            startForegroundService(foregroundServiceIntent)
        }else{
            startService(foregroundServiceIntent)
        }

    }
    private fun onForegroundStop(intent:Intent){
        stopService(intent)
    }


}