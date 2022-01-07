package com.android.adevinta.util


fun updateDataList(dataList: MutableList<Int>) : List<Int> {
    kotlin.repeat(30) {
        dataList.add(dataList.size + 1)
    }
    return dataList
}