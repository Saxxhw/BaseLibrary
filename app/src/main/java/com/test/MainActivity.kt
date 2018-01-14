package com.test

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.saxxhw.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    override fun getLayout(): Int = R.layout.activity_main

    override fun initEventAndData(savedInstanceState: Bundle?) {
    }

    override fun setNavigation(mToolBar: Toolbar?) {
        mToolBar?.setNavigationIcon(R.mipmap.ic_menu_personal_center)
        mToolBar?.setNavigationOnClickListener { toast("21321321") }
    }
}