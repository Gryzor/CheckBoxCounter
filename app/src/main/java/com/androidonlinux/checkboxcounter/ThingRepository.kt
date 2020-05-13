package com.androidonlinux.checkboxcounter

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
}