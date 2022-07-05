package com.example.online.presentation.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.online.R
import com.example.online.data.model.Advice
import com.example.online.databinding.AdviceItemBinding

class AdviceAdapter(private val advices: List<Advice>): RecyclerView.Adapter<AdviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.advice_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = advices[position]
            adviceHeader.text = item.state
            adviceDescription.text = root.context.getString(item.description)

            val dialog = AlertDialog.Builder(root.context).setCancelable(true).create()

            Glide.with(root.context)
                .load(item.icon)
                .into(adviceIcon)

            detailedButton.setOnClickListener {
                dialog.setTitle(item.state)
                dialog.setMessage(root.context.getString(item.text))
                dialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return advices.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = AdviceItemBinding.bind(itemView)
    }
}