package com.test

import android.os.Bundle
import com.saxxhw.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayout(): Int = R.layout.activity_main

    override fun initEventAndData(savedInstanceState: Bundle?) {
        title = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十"
    }

    override fun bindListener() {

    }
}
