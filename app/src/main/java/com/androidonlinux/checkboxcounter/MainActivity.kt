package com.androidonlinux.checkboxcounter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter: ThingAdapter = ThingAdapter()
    private val viewModel: MainActivityViewModel by viewModels()

    private val listener: ThingClickListener = object : ThingClickListener {
        override fun onThingClicked(thing: Thing, isChecked: Boolean) {
            Log.d("XXX", "On Thing Clicked! ${thing.id} isChecked? $isChecked")
            viewModel.onThingClicked(thing, isChecked)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.thingClickListener = listener
        mainRecyclerView.adapter = adapter

        observeCounter()
        observeThings()

        viewModel.fetchThings()
    }

    private fun observeCounter() = viewModel.observeCounter().observe(this) {
        updateCounter(it)
    }

    private fun observeThings() = viewModel.observeThingsList().observe(this) {
        adapter.submitList(it)

        // TODO: don't use this, instead keep track of the position and update just that location
        // via notifyItemChanged(...)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCounter(total: Int) {
        counter.text = "Checked Total: $total"
    }
}

interface ThingClickListener {
    fun onThingClicked(thing: Thing, isChecked: Boolean)
}
