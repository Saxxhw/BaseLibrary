package com.saxxhw.widget.option

/**
 * Created by Saxxhw on 2018/1/12.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */
data class Option(val code: String, val stepCode: String, val name: String, val image: Image, var isChecked: Boolean)

data class Image(val url: String)