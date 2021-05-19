package com.example.apipractice.utills


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//TODO Move Utils Classes to Similar package
//TODO Take care of Spellings
//TODO Try make common methods to perform similar type of Jobs
//TODO Key values via CONSTANT variables
class StorePreferencesss(context: Context) {



    private val dataStore = context.createDataStore(name = "apiexample")
    /** Preference Keys For Global Settings */
    companion object StoreConfigPreferenceKeys {
        var Token = stringPreferencesKey("LOGIN_DATA_Token")
        var Tokenn = stringPreferencesKey("LOGIN_DATA_Tokenn")
        var User = stringPreferencesKey("USER_TYPE")
    }

    /** Store Any Any with Key */
    suspend  fun <T : Any> storeValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    /** Get Stored Any with Key */
     fun <T : Any> readValue(
        key: Preferences.Key<T>
    ): Flow<T?> {
        return dataStore.getFromLocalStorageAny(key)
    }

     fun <T : Any> DataStore<Preferences>.getFromLocalStorageAny(
        PreferencesKey: Preferences.Key<T>
    ) =
        data.catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[PreferencesKey]
        }

}
