package com.example.application.db

/*
 * Helper for insertOperations
 */
class InsertBuilder {
    private val attributes = mutableListOf<String>()
    fun addAtribute(name : String, type : String, isNull : Boolean, isPrimaryKey : Boolean, autoIncrement: Boolean) {
        val nu : String = if (isNull) "NOT NULL" else ""
        val pk : String = if (isPrimaryKey) "PRIMARY KEY" else ""
        val ai : String = if (autoIncrement) "AUTOINCREMENT" else ""
        attributes.add("$name $type $nu $pk $ai")
    }
    fun getList(): List<String> {
        return attributes
    }
}

