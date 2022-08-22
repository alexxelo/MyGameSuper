package com.ilyin.tools_android.crud

interface CrudPref {

  fun getBool(key: String, defValue: Boolean = false): Boolean

  fun getBoolNullable(key: String): Boolean? = contains(key).takeIf { it }?.let { getBool(key) }

  fun getInt(key: String, defValue: Int = 0): Int

  fun getIntNullable(key: String): Int? = contains(key).takeIf { it }?.let { getInt(key) }

  fun getLong(key: String, defValue: Long = 0L): Long

  fun getLongNullable(key: String): Long? = contains(key).takeIf { it }?.let { getLong(key) }

  fun getFloat(key: String, defValue: Float = 0.0f): Float

  fun getFloatNullable(key: String): Float? = contains(key).takeIf { it }?.let { getFloat(key) }

  fun getString(key: String, defValue: String = ""): String

  fun getStringNullable(key: String): String? = contains(key).takeIf { it }?.let { getString(key) }

  fun getStringListNullable(key: String): List<String>?

  fun getStringList(key: String, defValue: List<String> = ArrayList()): List<String>

  fun getIntList(key: String, defValue: List<Int> = ArrayList()): List<Int>

  fun save(key: String, value: Any?)

  fun contains(key: String): Boolean

  fun containsAll(vararg keys: String): Boolean

  fun delete(vararg keys: String)

  fun delete(key: String)

  fun clear()
}
