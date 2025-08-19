package com.hemnath.datablocking

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
//    lateinit var pref: SharedPreferences
    var navigateBack: MutableLiveData<Boolean> = MutableLiveData(false)

    private val job = SupervisorJob()
    val viewModelScope = CoroutineScope(Dispatchers.Main + job)
    var isLoading = ObservableField<Int>()
    var errorMsg = ObservableField<String>()

    init {
//        pref = PreferenceManager.getDefaultSharedPreferences(application)
    }

    var error: (hasError: Boolean, errorMessage: String) -> Unit = { hasError, errorMessage ->
        if (hasError) {
            //TODO : show toast
            //application.applicationContext.showToast(errorMessage)
        }

    }

    fun navigateToActivity(intent: Intent, context: Context, finish: Boolean = false) {
        context.startActivity(intent)
        (context as Activity).finish()
    }


}

