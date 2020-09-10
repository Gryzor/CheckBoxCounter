package com.androidonlinux.checkboxcounter

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class FakeDataRepository {
    private val listOfData: MutableList<Thing> = mutableListOf()

    private fun createFakeData() {
        for (i in 1..1000) {
            listOfData.add(Thing(i, "This is Item $i", false))
        }
    }

    init {
        createFakeData()
    }

    fun getData(): List<Thing> {
        return ArrayList(listOfData)
    }

    suspend fun simulateLongOperation() = flow {
        delay(500)
        emit(10)
        delay(500)
        emit(30)
        delay(500)
        emit(60)
        delay(300)
        emit(70)
        delay(100)
        emit(80)
        delay(200)
        emit(90)
        delay(300)
        emit(100)
    }
}