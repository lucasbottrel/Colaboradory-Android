package com.colaboradory.frontendandroid

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.colaboradory.APIservice.API
import com.colaboradory.frontendandroid.databinding.ActivityMainBinding
import com.colaboradory.model.Colaborador
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var nome: EditText? = null;
    private var senha: EditText? = null;
    private var tableLayout: TableLayout? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        nome = findViewById(R.id.editNameText)
        senha = findViewById(R.id.editPasswordText)
        tableLayout = findViewById<TableLayout>(R.id.table)

        ListaColaboradores()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun ListaColaboradores(){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.2.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(API::class.java)

            apiService.listaColaboradores().enqueue(object : Callback<List<Colaborador>> {
                override fun onResponse(call: Call<List<Colaborador>>,
                                        response: Response<List<Colaborador>>) {
                    if (response.isSuccessful) {
                        val colaboradores = response.body()
                        if (colaboradores != null) {
                            for (colaborador in colaboradores) {
                                println("ID: ${colaborador.id}, Nome: ${colaborador.nome}")

                                val tableRow = TableRow(this@MainActivity)

                                val idTextView = TextView(this@MainActivity)
                                idTextView.text = colaborador.id.toString()
                                tableRow.addView(idTextView)

                                val nomeTextView = TextView(this@MainActivity)
                                nomeTextView.text = colaborador.nome
                                tableRow.addView(nomeTextView)

                                tableLayout?.addView(tableRow)
                            }

                        }
                    } else {
                        println("Erro na resposta: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Colaborador>>, t: Throwable) {
                    println("Não enviado!");
                    t.printStackTrace();
                }

            })
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun SaveColaborador(nome: String, senha: String){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.2.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(API::class.java)

            val body = mapOf(
                "nome" to nome,
                "senha" to senha
            )

            apiService.cadastroColaborador(body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    println("Enviado!");
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Não enviado!");
                    t.printStackTrace();
                }
            })
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun sendMessage(view: View){
        view.toString()

        val nomeValue = if (nome != null) nome!!.text else "";
        val senhaValue = if (senha != null) senha!!.text else "";
        SaveColaborador(nomeValue.toString(), senhaValue.toString());

    }
}