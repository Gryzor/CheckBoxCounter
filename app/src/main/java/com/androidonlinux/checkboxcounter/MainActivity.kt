package com.androidonlinux.checkboxcounter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var checked: Int = 0
    private var adapter: ThingAdapter = ThingAdapter()
    private var repository: FakeDataRepository = FakeDataRepository()

    private val listener: ThingClickListener = object : ThingClickListener {
        override fun onThingClicked(thing: Thing, isChecked: Boolean) {
            Log.d("XXX", "On Thing Clicked! ${thing.id} isChecked? $isChecked")

            // The "correct" thing to do here is have a view model that talks to the Repository
            // that lets the repository know that this item has been changed. The viewmodel
            // should also keep the counter, not the activity.
            // The activity should _observe_ the viewModel's counter and update the view accordingly.
            thing.isSelected = isChecked

            if (isChecked) {
                checked++
            } else {
                checked--
            }

            updateCounter()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.thingClickListener = listener
        mainRecyclerView.adapter = adapter

        updateCounter()

        adapter.submitList(repository.getData())
    }

    private fun updateCounter() {
        counter.text = "Checked Total: $checked"
    }
}

interface ThingClickListener {
    fun onThingClicked(thing: Thing, isChecked: Boolean)
}
