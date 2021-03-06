package com.androidonlinux.checkboxcounter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ThingAdapter : ListAdapter<Thing, ThingAdapter.BaseViewHolder>(DiffUtilCallback()) {

    var thingClickListener: ThingClickListener? = null

    companion object ViewHolderType {
        const val Normal = 0
        const val InProgress = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Normal -> {
                ColoredViewHolder(
                    inflater.inflate(
                        R.layout.green_item_layout,
                        parent,
                        false
                    ),
                    thingClickListener
                )
            }
            else -> {
                ProgressViewHolder(
                    inflater.inflate(
                        R.layout.progress_item_layout,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position) as Thing
        return item.id
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) as Thing

        return if (item.progress == null) Normal else {
            InProgress
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: Thing)
    }

    internal class ColoredViewHolder(
        itemView: View,
        private val toggleListener: ThingClickListener?
    ) : BaseViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_text)
        private val checkBoxView: CheckBox = itemView.findViewById(R.id.checkBoxView)

        override fun bind(data: Thing) {
            title.text = data.title
            checkBoxView.isChecked = data.isSelected
            itemView.setOnClickListener {
                toggleListener?.onThingClicked(data, data.isSelected.not())
            }
        }
    }

    internal class ProgressViewHolder(
        itemView: View
    ) : BaseViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_text)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarView)

        @SuppressLint("SetTextI18n")
        override fun bind(data: Thing) {
            title.text = data.title
            progressBar.progress = data.progress ?: 0
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
                    && oldItem.isSelected == newItem.isSelected
                    && oldItem.progress == newItem.progress)

            Log.d("TAG", "areContents The Same: {$areSame}")
            return areSame
        }
    }
}