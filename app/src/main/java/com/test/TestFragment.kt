package com.test

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.saxxhw.base.BaseFragment
import com.saxxhw.matisse.Matisse
import com.saxxhw.matisse.MimeType
import com.saxxhw.matisse.engine.impl.GlideEngine
import com.saxxhw.matisse.internal.entity.CaptureStrategy
import com.saxxhw.widget.SelectPictureView
import kotlinx.android.synthetic.main.fragment_test.*

/**
 * Created by Saxxhw on 2018/1/13.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */
class TestFragment : BaseFragment(), SelectPictureView.onPictureSelectListener {

    companion object {
        // 图片选择回传值
        private val REQUEST_CODE_IMAGE = 12
    }


    override fun getLayout(): Int = R.layout.fragment_test

    override fun initEventAndData(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {
        addPicture.setOnPictureSelectListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> addPicture.setData(Matisse.obtainPathResult(data))
            }
        }
    }

    override fun onPictureSelect() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .capture(true)
                .captureStrategy(CaptureStrategy(true, "com.test.fileprovider"))
                .maxSelectable(addPicture.missingCount)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_IMAGE)
    }
}