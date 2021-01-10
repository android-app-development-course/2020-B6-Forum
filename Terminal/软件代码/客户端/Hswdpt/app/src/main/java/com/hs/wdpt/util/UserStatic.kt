package com.hs.wdpt.util

import android.content.Context
import android.util.Log
import android.widget.Toast

class UserStatic(val context: Context) {
    companion object {
        const val userPref = "user"
    }

    fun isLogin(): Boolean {
        val sf = context.getSharedPreferences(userPref, Context.MODE_PRIVATE)
        if (sf.contains("userid")) {
            return true
        }
        return false
    }

    fun login(id: String) {
        val edit = context.getSharedPreferences(userPref, Context.MODE_PRIVATE).edit()
        edit.putString("userid", id)
        edit.apply()
    }

    fun getLoginId(): String? {
        var result = ""
        val pref = context.getSharedPreferences(userPref, Context.MODE_PRIVATE)
        if (pref.contains("userid")) {
            result += pref.all["userid"].toString()
        }
        return result
    }

    fun unLogin() {
        val edit = context.getSharedPreferences(userPref, Context.MODE_PRIVATE).edit()
        edit.remove("userid").apply()
    }
}