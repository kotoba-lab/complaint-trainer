package com.melof.complainttrainer.data

import android.content.Context

/**
 * ユーザーが「この表現もOK」と登録したフレーズを SharedPreferences に保存する。
 * カテゴリごとに改行区切りで保存する。
 */
class UserDictionaryStore(context: Context) {

    private val prefs = context.getSharedPreferences("user_dict", Context.MODE_PRIVATE)

    private fun key(category: ResponseCategory) = category.name

    fun loadPhrases(category: ResponseCategory): List<String> {
        val raw = prefs.getString(key(category), "") ?: ""
        return raw.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
    }

    fun addPhrase(category: ResponseCategory, phrase: String) {
        val current = loadPhrases(category).toMutableList()
        if (phrase.isNotBlank() && !current.contains(phrase.trim())) {
            current.add(phrase.trim())
            prefs.edit().putString(key(category), current.joinToString("\n")).apply()
        }
    }

    fun removePhrase(category: ResponseCategory, phrase: String) {
        val current = loadPhrases(category).toMutableList()
        current.remove(phrase.trim())
        prefs.edit().putString(key(category), current.joinToString("\n")).apply()
    }

    fun loadAll(): Map<ResponseCategory, List<String>> =
        ResponseCategory.values().associateWith { loadPhrases(it) }
}
