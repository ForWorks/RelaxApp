package com.example.online.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.online.R
import com.example.online.data.model.States.*
import com.example.online.data.model.*
import com.example.online.databinding.StateItemBinding

class StateAdapter(private val listener: (Pair<MutableList<Advice>, String>) -> Unit): RecyclerView.Adapter<StateAdapter.ViewHolder>() {

    private val advices: MutableList<Advice> = mutableListOf(
        Advice(Calm.name, R.string.advice_1_description, R.string.advice_1_text, R.drawable.ic_advice_1),
        Advice(Calm.name, R.string.advice_2_description, R.string.advice_2_text, R.drawable.ic_advice_2),
        Advice(Relaxed.name, R.string.advice_3_description, R.string.advice_3_text, R.drawable.ic_advice_1),
        Advice(Focused.name, R.string.advice_4_description, R.string.advice_4_text, R.drawable.ic_advice_1),
        Advice(Focused.name, R.string.advice_5_description, R.string.advice_5_text, R.drawable.ic_advice_2),
        Advice(Excited.name, R.string.advice_6_description, R.string.advice_6_text, R.drawable.ic_advice_1),
    )

    private val states: MutableList<State> = mutableListOf(
        State(R.drawable.ic_calm, Calm.name),
        State(R.drawable.ic_relax, Relaxed.name),
        State(R.drawable.ic_focus, Focused.name),
        State(R.drawable.ic_anxious, Excited.name)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.state_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = states[position]
            stateIcon.setImageResource(item.icon)
            state.text = item.description

            root.setOnClickListener {
                val list = mutableListOf<Advice>()
                advices.forEach {
                    if(it.state == item.description)
                        list.add(it)
                }
                listener(Pair(list, item.description))
            }
        }
    }

    override fun getItemCount(): Int {
        return states.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = StateItemBinding.bind(itemView)
    }
}