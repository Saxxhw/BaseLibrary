package com.saxxhw.base

/**
 * Created by Saxxhw on 2017/1/12.
 * 邮箱：Saxxhw@126.com
 * 功能：View基类
 */

interface BaseView {

    /**
     * 展示错误信息
     *
     * @param msg
     */
    fun showErrorMsg(msg: String)

    /**
     * show ProgressDialog
     */
    fun showProgress()

    /**
     * dismiss ProgressDialog
     */
    fun dismissProgress()

    /**
     * show loading
     */
    fun showLoading()

    /**
     * hide loading
     */
    fun showContent()

    /**
     * show empty
     */
    fun showEmpty(title: String = "", content: String = "")

    /**
     * show network error
     */
    fun showNetworkError(title: String = "", content: String = "")
}