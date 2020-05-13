package com.androidonlinux.checkboxcounter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ThingAdapter : ListAdapter<Thing, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    var thingClickListener: ThingClickListener? = null

    private var toggleListener: ToggleListener = object : ToggleListener {
        override fun onItemToggled(isChecked: Boolean, position: Int) {
            Log.d("XXX", "IsChecked: $isChecked at $position")
            val thing = getItem(position)
            thingClickListener?.onThingClicked(thing, isChecked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ColoredViewHolder(
            inflater.inflate(
                R.layout.green_item_layout,
                parent,
                false
            ),
            toggleListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder).bind(getItem(position))
    }

    internal abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: Thing)
    }

    internal class ColoredViewHolder(
        itemView: View,
        private val toggleListener: ToggleListener?
    ) : BaseViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_text)
        private val checkBoxView: CheckBox = itemView.findViewById(R.id.checkBoxView)

        init {
            itemView.setOnClickListener {
                checkBoxView.toggle()
                toggleListener?.onItemToggled(checkBoxView.isChecked, layoutPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(data: Thing) {
            title.text = data.title
            checkBoxView.isChecked = data.isSelected
        }
    }

    internal class DiffUtilCallback : DiffUtil.ItemCallback<Thing>() {
        override fun areItemsTheSame(oldItem: Thing, newItem: Thing): Boolean {
            Log.d("TAG", "areItems The Same ${oldItem.id == newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Thing, newItem: Thing): Boolean {
            val areSame = (oldItem.title == newItem.title
                    && oldItem.title == newItem.title
                    && oldItem.isSelected == newItem.isSelected)

            Log.d("TAG", "areContents The Same: {$areSame}")
            return areSame
        }
    }

    interface ToggleListener {
        fun onItemToggled(isChecked: Boolean, position: Int)
    }
}