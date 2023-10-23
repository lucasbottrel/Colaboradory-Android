package com.colaboradory.frontendandroid

import Adapter.ColaboradorAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.colaboradory.APIservice.API
import com.colaboradory.APIservice.APIController
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
    private var APIController = APIController();
    private var nome: EditText? = null
    private var senha: EditText? = null
    private var chefe: Spinner? = null
    private var subordinado: Spinner? = null
    private var idChefe: Integer? = null
    private var idSubordinado: Integer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val view = binding.root

        nome = view.findViewById(R.id.editNameText)
        senha = view.findViewById(R.id.editPasswordText)
        chefe = view.findViewById(R.id.chefe)
        subordinado = view.findViewById(R.id.subordinado)

        var colaboradoresList: List<Colaborador>? = null

        chefe?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelecionado = parent?.getItemAtPosition(position) as Colaborador

                idChefe = itemSelecionado.id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Lidar com nenhum item selecionado, se necessário
            }
        }

        subordinado?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelecionado = parent?.getItemAtPosition(position) as Colaborador

                idSubordinado = itemSelecionado.id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Lidar com nenhum item selecionado, se necessário
            }
        }

        APIController.ListaColaboradores(object : APIController.ColaboradoresCallback {
            override fun onColaboradoresReceived(colaboradores: List<Colaborador>) {
                colaboradoresList = colaboradores

                val colaboradorAdapter = ColaboradorAdapter(applicationContext, android.R.layout.simple_spinner_item,
                    colaboradoresList!!
                )
                colaboradorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                chefe = findViewById(R.id.chefe) // Certifique-se de usar o ID correto
                chefe?.adapter = colaboradorAdapter

                subordinado = findViewById(R.id.subordinado) // Certifique-se de usar o ID correto
                subordinado?.adapter = colaboradorAdapter
            }

            override fun onErroResponse(code: Int) {
                // Lide com erros na resposta
            }

            override fun onFalha(throwable: Throwable) {
                // Lide com falhas na solicitação
            }
        });

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

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

    fun sendMessage(view: View){
        val nomeValue = if (nome != null) nome!!.text else ""
        val senhaValue = if (senha != null) senha!!.text else ""
        SaveColaborador(nomeValue.toString(), senhaValue.toString())
        nome?.text?.clear()
        senha?.text?.clear()
    }

    fun saveBoss(view: View){
        val chefe = idChefe;
        val subordinado = idSubordinado;

        if (chefe != subordinado){
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.2.105:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(API::class.java)

                val body = mapOf(
                    "idBoss" to chefe,
                    "idSubordinate" to subordinado
                )

                apiService.associaChefe(body).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        println("Enviado!")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println("Não enviado!")
                        t.printStackTrace()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun SaveColaborador(nome: String, senha: String) {
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
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println("Enviado!")
                    nome
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Não enviado!")
                    t.printStackTrace()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}