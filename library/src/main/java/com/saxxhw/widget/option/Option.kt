package com.saxxhw.widget.option

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Saxxhw on 2018/1/12.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */
data class Option(val code: String, val stepCode: String, val name: String, val image: Image, val hiddenDangerStatus: Int, var isChecked: Boolean) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Image>(Image::class.java.classLoader),
            source.readInt(), 1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(code)
        writeString(stepCode)
        writeString(name)
        writeParcelable(image, 0)
        writeInt(hiddenDangerStatus)
        writeInt((if (isChecked) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Option> = object : Parcelable.Creator<Option> {
            override fun createFromParcel(source: Parcel): Option = Option(source)
            override fun newArray(size: Int): Array<Option?> = arrayOfNulls(size)
        }
    }
}

data class Image(val url: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Image> = object : Parcelable.Creator<Image> {
            override fun createFromParcel(source: Parcel): Image = Image(source)
            override fun newArray(size: Int): Array<Image?> = arrayOfNulls(size)
        }
    }
}