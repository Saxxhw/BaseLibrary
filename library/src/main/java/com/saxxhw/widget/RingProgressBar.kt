package com.saxxhw.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

import com.saxxhw.R

/**
 * Created by Saxxhw on 2017/6/30.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */

class RingProgressBar @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(mContext, attrs, defStyle) {

    companion object {
        //空心样式
        val STROKE = 0
        //实心样式
        val FILL = 1
    }

    //画笔对象
    private val paint: Paint
    //View宽度
    private var mWidth: Int = 0
    //View高度
    private var mHeight: Int = 0
    //默认宽高值
    private var result = 0
    //默认padding值
    private var padding = 0

    // 圆环的颜色
    var ringColor: Int = 0
    // 圆环进度颜色
    var ringProgressColor: Int = 0
    // 文字颜色
    var textColor: Int = 0
    // 文字大小
    var textSize: Float = 0.toFloat()
    // 圆环宽度
    var ringWidth: Float = 0.toFloat()
    //最大值
    private var max: Int = 0
    //是否显示文字
    private val textIsShow: Boolean
    //圆环进度条的样式
    private val style: Int
    //进度回调接口
    private var mOnProgressListener: OnProgressListener? = null
    // 圆环中心
    private var centre: Int = 0
    // 圆环半径
    private var radius: Int = 0

    // 进度值
    @get:Synchronized
    var progress: Int = 0
        @Synchronized set(progress) {
            var progress = progress
            if (progress < 0) {
                throw IllegalArgumentException("The progress of 0")
            }
            if (progress > max) {
                progress = max
            }
            if (progress <= max) {
                field = progress
                postInvalidate()
            }
            if (progress == max) {
                if (mOnProgressListener != null) {
                    mOnProgressListener!!.progressToComplete()
                }
            }
        }

    init {
        //初始化画笔
        paint = Paint()

        //初始化默认宽高值
        result = dp2px(100)

        //初始化属性
        val mTypedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RingProgressBar)

        ringColor = mTypedArray.getColor(R.styleable.RingProgressBar_ringColor, Color.BLACK)
        ringProgressColor = mTypedArray.getColor(R.styleable.RingProgressBar_ringProgressColor,
                Color.WHITE)
        textColor = mTypedArray.getColor(R.styleable.RingProgressBar_textColor, Color.BLACK)
        textSize = mTypedArray.getDimension(R.styleable.RingProgressBar_textSize, 16f)
        ringWidth = mTypedArray.getDimension(R.styleable.RingProgressBar_ringWidth, 5f)
        max = mTypedArray.getInteger(R.styleable.RingProgressBar_max, 100)
        textIsShow = mTypedArray.getBoolean(R.styleable.RingProgressBar_textIsShow, true)
        style = mTypedArray.getInt(R.styleable.RingProgressBar_style, 0)

        mTypedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        centre = width / 2
        radius = (centre - ringWidth / 2).toInt()
        //绘制外层圆
        drawCircle(canvas)
        //绘制文本内容
        drawTextContent(canvas)
        //绘制进度条
        drawProgress(canvas)
        // 绘制分割线
        drawLine(canvas)
    }


    /**
     * 绘制外层圆环
     */
    private fun drawCircle(canvas: Canvas) {
        //设置画笔颜色
        paint.color = ringColor
        //设置画笔样式
        paint.style = Paint.Style.STROKE
        //设置stroke的宽度
        paint.strokeWidth = ringWidth
        //设置抗锯齿
        paint.isAntiAlias = true
        //绘制圆形
        canvas.drawCircle(centre.toFloat(), centre.toFloat(), radius.toFloat(), paint)
    }

    /**
     * 绘制进度文本
     */
    private fun drawTextContent(canvas: Canvas) {
        //设置stroke的宽度
        paint.strokeWidth = 0f
        //设置文字的颜色
        paint.color = textColor
        //设置文字的大小
        paint.textSize = textSize
        //设置文字的style
        paint.typeface = Typeface.DEFAULT
        //设置进度值
        val percent = (this.progress.toFloat() / max.toFloat() * 100).toInt()
        //获取文字的宽度 用于绘制文本内容
        val textWidth = paint.measureText(percent.toString() + "%")
        //绘制文本 会根据设置的是否显示文本的属性&是否是Stroke的样式进行判断
        if (textIsShow && percent != 0 && style == STROKE) {
            canvas.drawText(percent.toString() + "%", centre - textWidth / 2, centre + textSize / 2, paint)
        }
    }

    /**
     * 绘制进度条
     */
    private fun drawProgress(canvas: Canvas) {
        //绘制进度 根据设置的样式进行绘制
        paint.strokeWidth = ringWidth
        paint.color = ringProgressColor

        //Stroke样式
        val strokeOval = RectF((centre - radius).toFloat(), (centre - radius).toFloat(), (centre + radius).toFloat(), (centre + radius).toFloat())
        //FIll样式
        val fillOval = RectF((centre - radius).toFloat() + ringWidth + padding.toFloat(), (centre - radius).toFloat() + ringWidth + padding.toFloat(), (centre + radius).toFloat() - ringWidth - padding.toFloat(), (centre + radius).toFloat() - ringWidth - padding.toFloat())

        when (style) {
            STROKE -> {
                paint.style = Paint.Style.STROKE
                paint.strokeCap = Paint.Cap.ROUND
                canvas.drawArc(strokeOval, -90f, (360 * this.progress / max).toFloat(), false, paint)
            }
            FILL -> {
                // 绘制底色
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = -0xfc132e
                canvas.drawArc(fillOval, -90f, 360f, true, paint)
                // 绘制进度
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = ringProgressColor
                if (this.progress != 0) {
                    canvas.drawArc(fillOval, -90f, (360 * this.progress / max).toFloat(), true, paint)
                }
            }
            else -> {
            }
        }
    }

    /**
     * 绘制分割线
     *
     * @param canvas
     */
    private fun drawLine(canvas: Canvas) {
        val rectF = RectF((centre - radius).toFloat() + ringWidth + padding.toFloat(), (centre - radius).toFloat() + ringWidth + padding.toFloat(), (centre + radius).toFloat() - ringWidth - padding.toFloat(), (centre + radius).toFloat() - ringWidth - padding.toFloat())
        val bitmap = BitmapFactory.decodeResource(mContext.resources, R.mipmap.bg_ring_peogress_bar)
        canvas.drawBitmap(bitmap, null, rectF, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取宽高的mode和size
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        //测量宽度
        if (widthMode == View.MeasureSpec.AT_MOST) {
            mWidth = result
        } else {
            mWidth = widthSize
        }

        //测量高度
        if (heightMode == View.MeasureSpec.AT_MOST) {
            mHeight = result
        } else {
            mHeight = heightSize
        }

        //设置测量的宽高值
        setMeasuredDimension(mWidth, mHeight)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)
        //确定View的宽高
        mWidth = w
        mHeight = h
        //初始化padding值 默认设置为5
        padding = dp2px(5)
    }


    /**
     * 获取当前的最大进度值
     */
    @Synchronized
    fun getMax(): Int {
        return max
    }


    /**
     * 设置最大进度值
     */
    @Synchronized
    fun setMax(max: Int) {
        if (max < 0) {
            throw IllegalArgumentException("The max progress of 0")
        }
        this.max = max
    }


    /**
     * dp转px
     */
    fun dp2px(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }


    /**
     * 进度完成回调接口
     */
    interface OnProgressListener {
        fun progressToComplete()
    }


    fun setOnProgressListener(mOnProgressListener: OnProgressListener) {
        this.mOnProgressListener = mOnProgressListener
    }
}