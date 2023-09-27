package com.colaboradory.frontendandroid

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.navigation.fragment.findNavController
import com.colaboradory.APIservice.API
import com.colaboradory.frontendandroid.databinding.FragmentSecondBinding
import com.colaboradory.model.Colaborador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var tableLayout: TableLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = _binding!!.root

        // Encontre o TableLayout dentro do layout do fragmento
        tableLayout = view.findViewById(R.id.table)

        return view

    }

    fun ListaColaboradores(view: View){
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.2.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(API::class.java)

            apiService.listaColaboradores().enqueue(object : Callback<List<Colaborador>> {
                override fun onResponse(call: Call<List<Colaborador>>,
                                        response: Response<List<Colaborador>>
                ) {
                    if (response.isSuccessful) {
                        val colaboradores = response.body()

                        val headerRow = TableRow(requireContext())

                        val idHeader = TextView(ContextThemeWrapper(requireContext(), R.style.TableHeader))
                        idHeader.text = "ID"
                        idHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        headerRow.addView(idHeader)

                        val nomeHeader = TextView(ContextThemeWrapper(requireContext(), R.style.TableHeader))
                        nomeHeader.text = "Nome"
                        nomeHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        headerRow.addView(nomeHeader)

                        val complexidadeHeader = TextView(ContextThemeWrapper(requireContext(), R.style.TableHeader))
                        complexidadeHeader.text = "Complexidade"
                        complexidadeHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        headerRow.addView(complexidadeHeader)

                        val scoreHeader = TextView(ContextThemeWrapper(requireContext(), R.style.TableHeader))
                        scoreHeader.text = "Score"
                        scoreHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        headerRow.addView(scoreHeader)

                        tableLayout?.addView(headerRow)

                        if (colaboradores != null) {
                            for (colaborador in colaboradores) {
                                println("ID: ${colaborador.id}, Nome: ${colaborador.nome}, Score: ${colaborador.score}, Complexidade: ${colaborador.complexidade}")

                                val tableRow = TableRow(requireContext())

                                val idTextView = TextView(ContextThemeWrapper(requireContext(), R.style.TableCell))
                                idTextView.text = colaborador.id.toString()
                                tableRow.addView(idTextView)

                                val nomeTextView = TextView(ContextThemeWrapper(requireContext(), R.style.TableCell))
                                nomeTextView.text = colaborador.nome
                                tableRow.addView(nomeTextView)

                                val complexidadeTextView = TextView(ContextThemeWrapper(requireContext(), R.style.TableCell))
                                complexidadeTextView.text = colaborador.complexidade
                                tableRow.addView(complexidadeTextView)

                                val scoreTextView = TextView(ContextThemeWrapper(requireContext(), R.style.TableCell))
                                scoreTextView.text = colaborador.score
                                tableRow.addView(scoreTextView)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Chame a função para preencher a tabela após a visualização ser criada
        ListaColaboradores(view)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}