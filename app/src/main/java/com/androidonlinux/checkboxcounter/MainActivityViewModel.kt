package com.androidonlinux.checkboxcounter

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _thingsList = MutableLiveData<MutatedThings>()

    private val repository = FakeDataRepository()
    private var _checkedCount: LiveData<Int> = Transformations.switchMap(_thingsList) { source ->
        val count = source.first.count { it.isSelected }
        MutableLiveData(count)
    }

    private val _mutatedPositions = mutableSetOf<Int>()

    fun observeData(): LiveData<MutatedThings> {
        return _thingsList
    }

    fun observeCounter(): LiveData<Int> {
        return _checkedCount
    }

    private fun addThingToDirtyList(thing: Thing) {
        viewModelScope.launch(Dispatchers.IO) {
            val pos = repository.positionOf(thing)
            if (pos == -1) {
                return@launch
            }

            synchronized(this) {
                _mutatedPositions.add(pos)
                val newList = _thingsList.value?.first
                newList?.let {
                    val positions: Positions = _mutatedPositions.toList()
                    val newValue = MutatedThings(it, positions)
                    _thingsList.postValue(newValue)
                }
            }
        }
    }

    private fun removeThingFromDirtyList(thing: Thing) {
        viewModelScope.launch(Dispatchers.IO) {
            val pos = repository.positionOf(thing)
            if (pos == -1) {
                return@launch
            }

            synchronized(this) {
                _mutatedPositions.remove(pos)
                val newList = _thingsList.value?.first
                newList?.let {
                    val positions: Positions = _mutatedPositions.toList()
                    val newValue = MutatedThings(it, positions)
                    _thingsList.postValue(newValue)
                }
            }
        }
    }

    fun fetchThings() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getData()
            _thingsList.postValue(Pair(data, _mutatedPositions.toList()))
        }
    }

    fun onThingClicked(thing: Thing, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            addThingToDirtyList(thing)

            if (isChecked) {

                repository.simulateLongOperation().collect { progress ->
                    if (progress == 100) {
                        repository.toggle(thing, isChecked)
                        _thingsList.notifyObserver()
                        removeThingFromDirtyList(thing)
                    } else {
                        repository.updateThingProgress(thing, progress)
                        _thingsList.notifyObserver()
                    }
                }
            } else {
                repository.toggle(thing, false)
                _thingsList.notifyObserver()
                removeThingFromDirtyList(thing)
            }
        }
    }
}