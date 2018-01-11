package com.saxxhw.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Saxxhw on 2016/10/28.
 * 邮箱：Saxxhw@126.com
 * 功能：不可以滑动，但是可以setCurrentItem的ViewPager。
 */

class NoScrollViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTouchEvent(arg0: MotionEvent): Boolean = false

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean = false
}
