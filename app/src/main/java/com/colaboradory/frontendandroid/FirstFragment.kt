package com.colaboradory.frontendandroid

import Adapter.ColaboradorAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.colaboradory.APIservice.APIController
import com.colaboradory.frontendandroid.databinding.FragmentFirstBinding
import com.colaboradory.model.Colaborador


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var nome: EditText? = null
    private var senha: EditText? = null
    private var chefe: Spinner? = null
    private var subordinado: Spinner? = null
    private var APIController = APIController();

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = _binding!!.root

        nome = view.findViewById(R.id.editNameText)
        senha = view.findViewById(R.id.editPasswordText)

        var colaboradoresList: List<Colaborador>? = null

        APIController.ListaColaboradores(object : APIController.ColaboradoresCallback {
            override fun onColaboradoresReceived(colaboradores: List<Colaborador>) {
                colaboradoresList = colaboradores

                val colaboradorAdapter = ColaboradorAdapter( requireContext(), android.R.layout.simple_spinner_item,
                    colaboradoresList!!
                )
                colaboradorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                chefe = view.findViewById(R.id.chefe) // Certifique-se de usar o ID correto
                chefe?.adapter = colaboradorAdapter

                subordinado = view.findViewById(R.id.subordinado) // Certifique-se de usar o ID correto
                subordinado?.adapter = colaboradorAdapter
            }

            override fun onErroResponse(code: Int) {
                // Lide com erros na resposta
            }

            override fun onFalha(throwable: Throwable) {
                // Lide com falhas na solicitação
            }
        });

        if (savedInstanceState != null) {
            val nomeValue = savedInstanceState.getString("nome")
            val senhaValue = savedInstanceState.getString("senha")
            nome?.setText(nomeValue)
            senha?.setText(senhaValue)
        }

        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("nome", nome?.text?.toString())
        outState.putString("senha", senha?.text?.toString())
        nome?.text?.clear()
        senha?.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        nome?.text?.clear()
        senha?.text?.clear()
    }
}