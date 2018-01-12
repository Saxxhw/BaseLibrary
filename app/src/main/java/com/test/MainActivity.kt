package com.test

import android.os.Bundle
import com.saxxhw.base.BaseActivity
import com.saxxhw.widget.option.Option
import com.saxxhw.widget.option.multi.MultiselectView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity(), MultiselectView.OnCheckedChangeListener {

    override fun getLayout(): Int = R.layout.activity_main

    override fun initEventAndData(savedInstanceState: Bundle?) {
        radio.addData(listOf(
                Option("选项1", false),
                Option("选项2", false),
                Option("选项3", false),
                Option("选项4", false),
                Option("选项5", false)))

    }

    override fun bindListener() {
        radio.setOnCheckedChangeListener(this)
    }

    override fun onMultiCheckedChangeListener(position: Int, option: Option) {
        toast("position=${position}，title=${option.title}")
    }
}
