package com.saxxhw.widget.option.multi

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.saxxhw.R
import com.saxxhw.glide.GlideApp
import com.saxxhw.widget.option.Option

/**
 * Created by Saxxhw on 2018/1/12.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */
class MultiAdapter : BaseQuickAdapter<Option, BaseViewHolder>(R.layout.widget_radio_view, null) {

    override fun convert(helper: BaseViewHolder, item: Option) {
        GlideApp.with(mContext).load(item.image.url).into(helper.getView(R.id.image))
        helper.setBackgroundColor(R.id.layout, if (item.isChecked) Color.parseColor("#E5F3FE") else Color.parseColor("#FFFFFF"))
        helper.setTextColor(R.id.title, if (item.isChecked) Color.parseColor("#008EFF") else Color.parseColor("#757575"))
        helper.setText(R.id.title, "${helper.layoutPosition + 1}、${item.name}")
    }
}