package Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.colaboradory.model.Colaborador

class ColaboradorAdapter(context: Context, resource: Int, private val colaboradores: List<Colaborador>) :
    ArrayAdapter<Colaborador>(context, resource, colaboradores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val colaborador = colaboradores[position]
        (view as TextView).text = colaborador.nome // Substitua "nome" pela propriedade desejada
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val colaborador = colaboradores[position]
        (view as TextView).text = colaborador.nome // Substitua "nome" pela propriedade desejada
        return view
    }
}