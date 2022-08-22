package ru.netology.mydiploma.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.mydiploma.api.AuthApiService
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "id"
    private val tokenKey = "token"
    private val _authLiveData: MutableLiveData<AuthState> = MutableLiveData()
    val authLiveData: LiveData<AuthState>
        get() = _authLiveData

    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)

        if (id == 0L || token == null) {
            _authLiveData.value = AuthState()
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authLiveData.value = AuthState(id, token)
        }
    }


    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authLiveData.value = AuthState(id, token)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authLiveData.value = AuthState()
        with(prefs.edit()) {
            clear()
            commit()
        }
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface AuthEntryPoint {
        fun authApiService(): AuthApiService
    }


}

data class AuthState(
    val id: Long = 0,
    val token: String? = null
)