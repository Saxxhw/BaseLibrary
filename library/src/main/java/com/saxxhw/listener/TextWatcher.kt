package com.saxxhw.listener

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Saxxhw on 2018/4/9.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */
interface TextWatcher : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {

    }
}