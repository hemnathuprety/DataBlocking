package com.hemnath.datablocking.ui

import android.os.Bundle
import com.hemnath.datablocking.BaseViewModelActivity
import com.hemnath.datablocking.R
import com.hemnath.datablocking.databinding.LayoutTestBinding

class TestActivity :
    BaseViewModelActivity<LayoutTestBinding, TestViewModel>() {

    override fun getViewModelClass(): Class<TestViewModel> =
        TestViewModel::class.java

    override fun getLayout(): Int = R.layout.layout_test

    override fun onCreateViewModel(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel;

    }


}