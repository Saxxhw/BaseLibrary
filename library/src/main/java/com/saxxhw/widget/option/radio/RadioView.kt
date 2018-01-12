package com.saxxhw.widget.option.radio

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.saxxhw.widget.option.Option

/**
 * Created by Saxxhw on 2018/1/12.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */
class RadioView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle), BaseQuickAdapter.OnItemClickListener {

    private var adapter: RadioAdapter = RadioAdapter()
    private lateinit var mList: List<Option>
    private var mListener: OnCheckedListener? = null

    init {
        this.setAdapter(adapter)
        this.layoutManager = LinearLayoutManager(context)
        adapter.onItemClickListener = this
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val option = adapter?.getItem(position) as Option
        if (!option.isChecked) {
            // 清空已选中项
            mList.filter { it.isChecked }.forEach { it.isChecked = false }
            // 设置当前选中项
            mList[position].isChecked = true
            // 刷新界面
            adapter.notifyDataSetChanged()
            // 触发自定义监听事件
            mListener?.onRadioCheckedListener(position, option)
        }
    }

    /**
     * 填充数据
     */
    fun addData(mList: List<Option>) {
        this.mList = mList
        adapter.setNewData(mList)
    }

    /**
     * 是否有选项被选中
     */
    fun isChecked(): Boolean {
        if (mList.isNotEmpty()) {
            return mList.any { it.isChecked }
        }
        return false
    }

    /**
     * 获取选中项
     */
    fun getCheckedItem(): Option? {
        if (isChecked()) {
            return mList.first { it.isChecked }
        }
        return null
    }

    /**
     * 绑定监听事件
     */
    fun setOnCheckedListener(listener: OnCheckedListener) {
        mListener = listener
    }

    interface OnCheckedListener {
        fun onRadioCheckedListener(position: Int, option: Option)
    }
}