package com.kevintekno.myservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.kevintekno.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        val foregroundServiceIntent = Intent(this, MyForegoundService::class.java)
        val boundServiceIntent = Intent(this, MyBoundService::class.java)
        binding.btnStartBackgroundService.setOnClickListener { startService(serviceIntent) }
        binding.btnStopBackgroundService.setOnClickListener { stopService(serviceIntent) }

        binding.btnStartForegroundService.setOnClickListener {onForegroundStart(foregroundServiceIntent) }
        binding.btnStopForegroundService.setOnClickListener { onForegroundStop(foregroundServiceIntent) }

        binding.btnStartBoundService.setOnClickListener {
            bindService(boundServiceIntent, connection, BIND_AUTO_CREATE)
        }
        binding.btnStopBoundService.setOnClickListener {
            unbindService(connection)
        }

    }
    private var boundStatus = false
    private lateinit var boundService: MyBoundService

    private var connection = object : ServiceConnection {


        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBoundService.MyBinder
            boundService = myBinder.getService
            boundStatus= true
            getNumberDataFromService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundStatus= false
        }
    }
    private fun getNumberDataFromService(){
        boundService.numberLiveData.observe(this){number ->
            binding.tvBoundServiceNumber.text = number.toString()

        }
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

    override fun onStop() {
        super.onStop()
        if(boundStatus){
            unbindService(connection)
            boundStatus = false
        }
    }


}