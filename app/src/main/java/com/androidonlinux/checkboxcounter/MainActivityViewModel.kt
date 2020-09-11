package com.androidonlinux.checkboxcounter

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _listSource = MutableLiveData<MutatedThings>()

    private val repository = FakeDataRepository()
    private val _thingsList = MutableLiveData<List<Thing>>()
    private var _checkedCount: LiveData<Int> = Transformations.switchMap(_thingsList) { list ->
        val count = list.count { it.isSelected }
        MutableLiveData(count)
    }

    fun observeData(): LiveData<MutatedThings> {
        return _listSource
    }

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
        viewModelScope.launch(Dispatchers.IO) {

            if (isChecked) {
                repository.simulateLongOperation().collect { progress ->
                    if (progress == 100) {
                        repository.toggle(thing, isChecked)
                    } else {
                        repository.updateThingProgress(thing, progress)
                    }
                    _thingsList.notifyObserver()
                }
            } else {
                repository.toggle(thing, false)
                _thingsList.notifyObserver()
            }
        }
    }
}