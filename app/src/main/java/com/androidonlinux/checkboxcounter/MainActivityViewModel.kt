package com.androidonlinux.checkboxcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val repository = FakeDataRepository()

    private val _thingsList = MutableLiveData<List<Thing>>()
    private var _checkedCount = MutableLiveData(0)

    fun observeThingsList(): LiveData<List<Thing>> {
        return _thingsList
    }

    fun observeCounter(): LiveData<Int> {
        return _checkedCount
    }

    fun fetchThings() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getData()
            _thingsList.postValue(data)
        }
    }

    fun onThingClicked(thing: Thing, isChecked: Boolean) {

        if (isChecked.not()) {
            // The user unselected this Thing
            thing.isSelected = false
            thing.progress = null
            updateCheckedCount(isChecked)
            return
        }

        viewModelScope.launch {
            repository.simulateLongOperation().collect { progress ->

                val list = _thingsList.value
                val clickedThing = list?.find { thing.id == it.id }
                clickedThing?.progress = progress

                if (progress == 100) {
                    thing.isSelected = isChecked
                    clickedThing?.progress = null
                    updateCheckedCount(isChecked)
                }

                _thingsList.postValue(list ?: emptyList())
            }
        }
    }

    private fun updateCheckedCount(isChecked: Boolean) {
        val value = if (isChecked) {
            _checkedCount.value?.plus(1)
        } else {
            _checkedCount.value?.minus(1)
        }
        _checkedCount.postValue(value)
    }
}