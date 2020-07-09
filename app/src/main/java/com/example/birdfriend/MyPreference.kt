package com.example.birdfriend

import android.content.Context

class MyPreference(context: Context) {

    val PREFERENCE_NAME = "HomeAwayPreference"
    val PREFERENCE_HOME_AWAY = "HOME_AWAY"

    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getHomeAway() : String? {
        return preference.getString(PREFERENCE_HOME_AWAY, "Home")
    }

    fun setHomeAway(status: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_HOME_AWAY,status)
        editor.apply()
    }
}