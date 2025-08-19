package com.hemnath.datablocking

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

abstract class BaseViewModelActivity<B : ViewDataBinding, VM : BaseViewModel> :
    CoreBaseActivity<B>() {
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(getViewModelClass())
        onCreateViewModel(savedInstanceState)

        viewModel.navigateBack.observe(this) {
            if (it) {
                super.onBackPressed()
            }
        }


    }

    abstract fun getViewModelClass(): Class<VM>

}
