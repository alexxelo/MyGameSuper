package com.ilyin.tools_android.crud

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlin.reflect.KProperty

open class PreferencesWrapper(protected val prefs: SharedPreferences) : CrudPref {

  override fun contains(key: String) = prefs.contains(key)

  override fun containsAll(vararg keys: String): Boolean {
    return keys.all(::contains)
  }

  override fun getBool(key: String, defValue: Boolean) = prefs.getBoolean(key, defValue)

  override fun getInt(key: String, defValue: Int) = prefs.getInt(key, defValue)

  override fun getLong(key: String, defValue: Long) = prefs.getLong(key, defValue)

  override fun getFloat(key: String, defValue: Float) = prefs.getFloat(key, defValue)

  override fun getString(key: String, defValue: String): String = prefs.getString(key, defValue) ?: defValue

  override fun getStringList(key: String, defValue: List<String>): List<String> {
    return getStringListNullable(key) ?: defValue
  }

  override fun getStringListNullable(key: String): List<String>? {
    return prefs.getStringSet(key, null)?.toList()
  }

  override fun getIntList(key: String, defValue: List<Int>): List<Int> {
    val stringedList = getString(key, "")
    return if (stringedList.isBlank()) {
      listOf()
    } else {
      stringedList.split("-").map(String::toInt)
    }
  }

  override fun save(key: String, value: Any?) {
    if (value == null) {
      delete(key)
    } else {
      val editor = edit()
      when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is Float -> editor.putFloat(key, value)
        is String -> editor.putString(key, value)
        is List<*> -> saveAsList(editor, key, value)
      }
      editor.commit()
    }
  }

  private fun saveAsList(editor: SharedPreferences.Editor, key: String, listOfAny: List<*>) {
    if (listOfAny.isEmpty()) {
      delete(key)
    } else {
      val firstAny = listOfAny[0]!!
      val firstAnyClass = firstAny.javaClass
      val allItemsAreSameClass = listOfAny.size == listOfAny.filterIsInstance(firstAnyClass).size
      if (allItemsAreSameClass) {
        when (firstAny) {
          is String -> saveStringList(editor, key, listOfAny.map { it.toString() })
          is Int -> saveIntList(editor, key, listOfAny.mapNotNull { it as? Int })
        }
      } else {
        val listOfString = listOfAny.map { it.toString() }
        saveStringList(editor, key, listOfString)
      }
    }
  }

  private fun saveStringList(editor: SharedPreferences.Editor, key: String, list: List<String>) {
    editor.putStringSet(key, list.toSet())
  }

  private fun saveIntList(editor: SharedPreferences.Editor, key: String, list: List<Int>) {
    val stringedList = list.joinToString("-") { it.toString() }
    editor.putString(key, stringedList)
  }

  override fun delete(vararg keys: String) {
    val edit = edit()
    keys.forEach { edit.remove(it) }
    edit.commit()
  }

  override fun delete(key: String) {
    edit().remove(key).commit()
  }

  @SuppressLint("CommitPrefEdits")
  private fun edit() = prefs.edit()

  override fun clear() = edit().clear().apply()

  open class PropertyDelegate<T>(
    private val preferencesWrapper: CrudPref,
    val key: String,
    private val getter: (key: String) -> T
  ) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

    fun get(): T = if (isOverridden()) {
      getter(getOverrideKey())
    } else {
      getter(key)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) = set(value)

    fun set(value: T?) = preferencesWrapper.save(key, value)

    fun setOverrideValue(value: T?) {
      if (value != null) {
        preferencesWrapper.save(getOverrideKey(), value)
      } else {
        preferencesWrapper.delete(getOverrideKey())
      }
    }

    private fun isOverridden() = preferencesWrapper.contains(getOverrideKey())

    private fun getOverrideKey() = "override_$key"
  }

  class BoolPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
    private val defaultValue: Boolean = false
  ) : PropertyDelegate<Boolean>(
    getter = { preferencesWrapper.getBool(it, defaultValue) },
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class IntPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
    private val defaultValue: Int = 0
  ) : PropertyDelegate<Int>(
    getter = { preferencesWrapper.getInt(it, defaultValue) },
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class LongPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
    private val defaultValue: Long = 0L
  ) : PropertyDelegate<Long>(
    getter = { preferencesWrapper.getLong(it, defaultValue) },
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class FloatPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
    private val defaultValue: Float = 0f
  ) : PropertyDelegate<Float>(
    getter = { preferencesWrapper.getFloat(it, defaultValue) },
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class StringPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
    private val defaultValue: String = ""
  ) : PropertyDelegate<String>(
    getter = { preferencesWrapper.getString(it, defaultValue) },
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class IntListPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
  ) : PropertyDelegate<List<Int>>(
    getter = preferencesWrapper::getIntList,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class StringListPref constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
  ) : PropertyDelegate<List<String>>(
    getter = preferencesWrapper::getStringList,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class StringListPrefNullable constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
  ) : PropertyDelegate<List<String>?>(
    getter = preferencesWrapper::getStringListNullable,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class BoolPrefNullable constructor(
    private val preferencesWrapper: CrudPref,
    key: String,
  ) : PropertyDelegate<Boolean?>(
    getter = preferencesWrapper::getBoolNullable,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class IntPrefNullable constructor(
    private val preferencesWrapper: CrudPref,
    key: String
  ) : PropertyDelegate<Int?>(
    getter = preferencesWrapper::getIntNullable,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class LongPrefNullable constructor(
    private val preferencesWrapper: CrudPref,
    key: String
  ) : PropertyDelegate<Long?>(
    getter = preferencesWrapper::getLongNullable,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class FloatPrefNullable constructor(
    private val preferencesWrapper: CrudPref,
    key: String
  ) : PropertyDelegate<Float?>(
    getter = preferencesWrapper::getFloatNullable,
    preferencesWrapper = preferencesWrapper,
    key = key
  )

  class StringPrefNullable constructor(
    private val preferencesWrapper: CrudPref,
    key: String
  ) : PropertyDelegate<String?>(
    getter = preferencesWrapper::getStringNullable,
    preferencesWrapper = preferencesWrapper,
    key = key
  )
}
