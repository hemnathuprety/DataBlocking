package com.hemnath.datablocking

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.appbar.MaterialToolbar


abstract class CoreBaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayout())


    }

    fun initToolbar(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
        if (hasBackArrow()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }

    fun initAppCompatToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        if (hasBackArrow())
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    abstract fun getLayout(): Int

    open fun hasBackArrow(): Boolean {
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun onCreateViewModel(savedInstanceState: Bundle?)

}