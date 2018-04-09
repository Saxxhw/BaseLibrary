package com.test

import android.os.Bundle
import com.saxxhw.base.BaseActivity
import com.saxxhw.widget.SelectPictureView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity(), SelectPictureView.onPictureSelectListener {

    override fun getLayout(): Int = R.layout.activity_main

    override fun initEventAndData(savedInstanceState: Bundle?) {
        addPicture.setData(listOf(
                "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=d2b763e914178a82da3177e39e6a19f8/b8014a90f603738df9e69507b91bb051f819ec63.jpg",
                "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=7259fa6881d4b31ce4319cf8efbf4d0a/8601a18b87d6277fa69bd31322381f30e924fc14.jpg",
                "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=c44b1a71094f78f0940692b011586020/f636afc379310a558cbf5ad1bd4543a98226107f.jpg"))
    }

    override fun bindListener() {
        addPicture.setOnPictureSelectListener(this)
    }

    override fun onAddClickListener() {

    }

    override fun onPictureSelect(position: Int, imageList: List<String>) {
        toast("$position")
    }
}
