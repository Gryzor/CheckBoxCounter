package com.androidonlinux.checkboxcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
        thing.isSelected = isChecked


        val value = if (isChecked) {
            _checkedCount.value?.plus(1)
        } else {
            _checkedCount.value?.minus(1)
        }

        _checkedCount.postValue(value)
    }
}