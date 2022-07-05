package com.example.online.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.online.R
import com.example.online.data.model.Horoscope
import com.example.online.databinding.HoroscopeItemBinding

class HoroscopeAdapter(private val entities: List<Horoscope>): RecyclerView.Adapter<HoroscopeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.horoscope_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val entity = entities[position]
            entityName.text = entity.name.substring(0..0).uppercase()
                                         .plus(entity.name.substring(1))
            date.text = entity.date_range
            description.text = entity.description
        }
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = HoroscopeItemBinding.bind(itemView)
    }
}