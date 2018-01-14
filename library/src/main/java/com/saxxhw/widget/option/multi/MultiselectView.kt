package com.saxxhw.widget.option.multi

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
class MultiselectView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle), BaseQuickAdapter.OnItemClickListener {

    private var adapter: MultiAdapter = MultiAdapter()
    private lateinit var mList: List<Option>
    private var mListener: OnCheckedChangeListener? = null

    // 可以点击
    var canChecked = true

    init {
        this.setAdapter(adapter)
        this.layoutManager = LinearLayoutManager(context)
        adapter.onItemClickListener = this
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (canChecked) {
            // 设置当前选中项
            mList[position].isChecked = !mList[position].isChecked
            // 刷新界面
            adapter?.notifyItemChanged(position)
            // 触发自定义监听事件
            mListener?.onMultiCheckedChangeListener(position, mList[position])
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
    fun getCheckedItem(): List<Option>? {
        if (isChecked()) {
            return mList.filter { it.isChecked }
        }
        return null
    }

    /**
     * 绑定监听事件
     */
    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        mListener = listener
    }

    interface OnCheckedChangeListener {
        fun onMultiCheckedChangeListener(position: Int, option: Option)
    }
}