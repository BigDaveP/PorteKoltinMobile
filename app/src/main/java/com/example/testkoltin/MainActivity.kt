package com.example.testkoltin

import android.nfc.Tag
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testkoltin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()

    var valueJson = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        run()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayAdapter: ArrayAdapter<*>
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //Permet de récupérer la liste des utilisateurs et de les afficher dans la liste "userList"
        val gson = Gson()
        val type: Type = object : TypeToken<List<Topic?>?>() {}.type
        val topics: List<Topic> = gson.fromJson(valueJson, type)
        var usernameList: List<String> = listOf()
        for (topic in topics) {
            usernameList += topic.name
        }
        var mListView = findViewById<ListView>(R.id.userList)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, usernameList)
        mListView.adapter = arrayAdapter
    }

    // Permet de récupérer la liste des utilisateurs dans l'API et de les stocker dans la variable "valueJson"
    fun run() {
        val request = Request.Builder()
            .url("http://167.114.96.59:2223/getUser")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    valueJson = response.body!!.string()
                }
            }
        })
    }
}


