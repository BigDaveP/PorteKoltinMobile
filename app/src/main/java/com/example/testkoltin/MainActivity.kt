package com.example.testkoltin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testkoltin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
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
        // Permet de rafraichir la liste toutes les 5 secondes
        Thread {
            while (true) {
                valueJson = ""
                run()
                runOnUiThread {
                    parseValueToView()
                }
                Thread.sleep(10000)
            }
        }.start()
    }

    //Permet de récupérer la liste des utilisateurs et de les afficher dans la liste "userList"
    fun parseValueToView (){
        val arrayAdapter: ArrayAdapter<*>
        val gson = Gson()
        val log: Type = object : TypeToken<List<Logs?>?>() {}.type
        Thread.sleep(1000)
        if (valueJson != ""){
            val logs: List<Logs> = gson.fromJson(valueJson, log)
            var usernameList: List<String> = listOf()
            // Filtre les logs pour avoir l'utilisateur et la date convertie en YYYY-MM-DD-HH-mm-SS
            for (log in logs){
                usernameList += log.name + " - " + log.date.substring(0, 10) + " " + log.date.substring(11, 19)
            }
            var mListView = findViewById<ListView>(R.id.userList)
            arrayAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, usernameList)
            mListView.adapter = arrayAdapter
            // Permet d'afficher les logs dans un TextView lorsqu'on clique sur un utilisateur
            mListView.setOnItemClickListener { parent, view, position, id ->
                val textView = findViewById<TextView>(R.id.textView)
                textView.text = logs[position].name + " - " + logs[position].date.substring(0, 10) + logs[position].date.substring(11, 19) + " - " + logs[position].serrure
            }



        }
        else{
            var mListView = findViewById<ListView>(R.id.userList)
            arrayAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listOf("Loading..."))
            mListView.adapter = arrayAdapter
        }
    }
    // Permet de récupérer la liste des utilisateurs dans l'API et de les stocker dans la variable "valueJson"
    fun run() {
        val request = Request.Builder()
            .url("http://167.114.96.59:2223/getLogs")
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


