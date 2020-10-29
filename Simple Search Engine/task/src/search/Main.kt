package search

import java.io.File
import java.lang.Exception

var persons = mutableListOf<List<String>>()
var invertedIndex = mutableMapOf<String, MutableList<Int>>()
var isFound = false
var isRun = true
var finds = 0

fun main(args: Array<String>) {
    addToStorage(args)
    calcInvert()
    while(isRun) {
        println("=== Menu ===\n" +
                "1. Search information.\n" +
                "2. Print all data.\n" +
                "0. Exit.\n")
        val options = readLine()!!
        when (options) {
            "1" -> search()
            "2" -> printAll()
            "0" -> isRun = false
        }
    }
}

fun calcInvert() {
    for (line in persons) {
        line.forEach { findIndex(it) }
    }
}

fun findIndex(element: String) {
    if (invertedIndex.containsKey(element)) return
    var indexes = mutableListOf<Int>()
    for (i in persons.indices) {
        if(persons[i].contains(element)) indexes.add(i)
    }
    invertedIndex[element] = indexes
}

fun addToStorage (args: Array<String>) {
    try {
        val lines = File(args[1]).readLines()
        for (line in lines) {
            persons.add(line.split("\\s+".toRegex()))
        }
    } catch (e: Exception) {
        println("File not found.")
    }
}

fun search() {
    println()
    println("Select a matching strategy: ALL, ANY, NONE")
    val strategy = readLine()!!
    println()
    isFound = false
    println("Enter a name or email to search all suitable people.")
    val query = readLine()!!
    var result = ""
    when (strategy) {
        "ALL" -> result += searchAll(query)
        "ANY" -> result += searchAny(query)
        "NONE" -> result += searchNone(query)
    }
    //persons.forEach {result += simpleSearch(it, query)}
    //result += invSearch(query)

    if (isFound) {
        println("$finds persons found:")
        println(result)
    } else {
        println("No matching people found.\n")
    }
}

fun simpleSearch(list: List<String>, query: String): String {
    var res = ""
    for (element in list) {
        if (element.toLowerCase().contains(query.toLowerCase())) {
            res = list.joinToString(" ") + "\n"
            isFound = true
        }
    }
    return res
}

fun invSearch(query: String): String {
    var res = ""
    if (invertedIndex.containsKey(query)) {
        for (index in invertedIndex[query]!!) {
            res += persons[index].joinToString(" ") + "\n"
            isFound = true
        }
    }
    return res
}

fun searchAll(query: String): String {
    finds = 0
    var res = ""
    isFound = false
    for (list in persons) {
        var isAllFind = true
        val line = list.joinToString(" ").toLowerCase()
        for (word in query.split("\\s+".toRegex())) {
            if (!line.contains(word.toLowerCase())) isAllFind = false
        }
        if (isAllFind) {
            isFound = true
            res += list.joinToString(" ") + "\n"
            finds++
        }
    }
    return res
}

fun searchAny(query: String): String {
    var res = ""
    finds = 0
    isFound = false
    for (list in persons) {
        var isAllFind = true
        val line = list.joinToString(" ").toLowerCase()
        for (word in query.split("\\s+".toRegex())) {
            if (line.contains(word.toLowerCase())) {
                isFound = true
                res += list.joinToString(" ") + "\n"
                finds++
                break
            }
        }
    }
    return res
}

fun searchNone(query: String): String {
    var res = ""
    finds = 0
    isFound = false
    for (list in persons) {
        var isNoneFind = true
        val line = list.joinToString(" ").toLowerCase()
        for (word in query.split("\\s+".toRegex())) {
            if (line.contains(word.toLowerCase())) isNoneFind = false
        }
        if (isNoneFind) {
            isFound = true
            res += list.joinToString(" ") + "\n"
            finds++
        }
    }
    return res
}

fun printAll() {
    var res = "\n=== List of people ===\n"
    persons.forEach{ res += it.joinToString(" ") + "\n"}
    println(res)
}
