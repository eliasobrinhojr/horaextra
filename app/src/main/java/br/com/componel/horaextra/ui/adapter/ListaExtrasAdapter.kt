package br.com.componel.horaextra.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.componel.horaextra.databinding.ItemListaExtrasBinding
import br.com.componel.horaextra.interfaces.listenerItens
import br.com.componel.horaextra.model.HoraExtra
import kotlinx.android.synthetic.main.item_lista_extras.view.*

class ListaExtrasAdapter(
        override var items: MutableList<HoraExtra>,
        private val context: Context)
    : RecyclerView.Adapter<ListaExtrasAdapter.ViewHolder>(), listenerItens {
    var onItemClick: (HoraExtra?) -> Unit = {}

    inner class ViewHolder(private val binding: ItemListaExtrasBinding) :
            RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.item_lista_extra_cardview.setOnClickListener {
                onItemClick(binding.extra)
            }
        }

        fun bind(extra: HoraExtra) {
            binding.extra = extra
            binding.executePendingBindings()
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListaExtrasBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}