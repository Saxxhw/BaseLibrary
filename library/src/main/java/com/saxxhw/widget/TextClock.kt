package com.saxxhw.widget

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.text.format.DateFormat
import android.util.AttributeSet

import java.util.Calendar
import java.util.TimeZone

import android.view.ViewDebug.ExportedProperty
import com.saxxhw.R
import java.lang.annotation.ElementType
import java.lang.annotation.Target

/**
 * Created by Saxxhw on 2017/12/2.
 * 邮箱：Saxxhw@126.com
 * 功能：
 */

class TextClock : android.support.v7.widget.AppCompatTextView {
    private var mFormat12: CharSequence? = null
    private var mFormat24: CharSequence? = null
    private var mDescFormat12: CharSequence? = null
    private var mDescFormat24: CharSequence? = null
    /**
     * Returns the current format string. Always valid after constructor has
     * finished, and will never be `null`.
     */
    @ExportedProperty
    var format: CharSequence? = null
        private set
    @ExportedProperty
    private var mHasSeconds: Boolean = false
    private var mDescFormat: CharSequence? = null
    private var mAttached: Boolean = false
    private var mTime: Calendar? = null
    private var mTimeZone: String? = null
    private var forceUse = FORMAT_AUTO
    // endregion

    private val mFormatChangeObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            chooseFormat()
            onTimeChanged()
        }

        override fun onChange(selfChange: Boolean, uri: Uri) {
            chooseFormat()
            onTimeChanged()
        }
    }

    private val mIntentReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (mTimeZone == null && Intent.ACTION_TIMEZONE_CHANGED == intent.action) {
                val timeZone = intent.getStringExtra("time-zone")
                createTime(timeZone)
            }
            onTimeChanged()
        }
    }
    private val mTicker = object : Runnable {
        override fun run() {
            onTimeChanged()
            val now = SystemClock.uptimeMillis()
            val next = now + (1000 - now % 1000)
            handler.postAtTime(this, next)
        }
    }

    /**
     * Returns the formatting pattern used to display the date and/or time
     * in 12-hour mode. The formatting pattern syntax is described in
     * [DateFormat].
     *
     * @return A [CharSequence] or null.
     * @see .setFormat12Hour
     * @see .is24HourModeEnabled
     */
    /**
     *
     * Specifies the formatting pattern used to display the date and/or time
     * in 12-hour mode. The formatting pattern syntax is described in
     * [DateFormat].
     *
     *
     *
     * If this pattern is set to null, [.getFormat24Hour] will be used
     * even in 12-hour mode. If both 24-hour and 12-hour formatting patterns
     * are set to null, the default pattern for the current locale will be used
     * instead.
     *
     *
     *
     * **Note:** if styling is not needed, it is highly recommended
     * you supply a format string generated by
     * [DateFormat.getBestDateTimePattern]. This method
     * takes care of generating a format string adapted to the desired locale.
     *
     * @param format A date/time formatting pattern as described in [DateFormat]
     * @see .getFormat12Hour
     * @see .is24HourModeEnabled
     * @see DateFormat.getBestDateTimePattern
     * @see DateFormat
     */
    var format12Hour: CharSequence?
        @ExportedProperty
        get() = mFormat12
        @RemotableViewMethod
        set(format) {
            mFormat12 = format
            chooseFormat()
            onTimeChanged()
        }

    /**
     * Returns the formatting pattern used to display the date and/or time
     * in 24-hour mode. The formatting pattern syntax is described in
     * [DateFormat].
     *
     * @return A [CharSequence] or null.
     * @see .setFormat24Hour
     * @see .is24HourModeEnabled
     */
    /**
     *
     * Specifies the formatting pattern used to display the date and/or time
     * in 24-hour mode. The formatting pattern syntax is described in
     * [DateFormat].
     *
     *
     *
     * If this pattern is set to null, [.getFormat24Hour] will be used
     * even in 12-hour mode. If both 24-hour and 12-hour formatting patterns
     * are set to null, the default pattern for the current locale will be used
     * instead.
     *
     *
     *
     * **Note:** if styling is not needed, it is highly recommended
     * you supply a format string generated by
     * [DateFormat.getBestDateTimePattern]. This method
     * takes care of generating a format string adapted to the desired locale.
     *
     * @param format A date/time formatting pattern as described in [DateFormat]
     * @see .getFormat24Hour
     * @see .is24HourModeEnabled
     * @see DateFormat.getBestDateTimePattern
     * @see DateFormat
     */
    var format24Hour: CharSequence?
        @ExportedProperty
        get() = mFormat24
        @RemotableViewMethod
        set(format) {
            mFormat24 = format
            chooseFormat()
            onTimeChanged()
        }

    /**
     * Indicates whether the system is currently using the 24-hour mode.
     *
     *
     * When the system is in 24-hour mode, this view will use the pattern
     * returned by [.getFormat24Hour]. In 12-hour mode, the pattern
     * returned by [.getFormat12Hour] is used instead.
     *
     *
     * If either one of the formats is null, the other format is used. If
     * both formats are null, the default formats for the current locale are used.
     *
     * @return true if time should be displayed in 24-hour format, false if it
     * should be displayed in 12-hour format.
     * @see .setFormat12Hour
     * @see .getFormat12Hour
     * @see .setFormat24Hour
     * @see .getFormat24Hour
     */
    val is24HourModeEnabled: Boolean
        get() {
            if (forceUse == FORMAT_12) {
                return false
            } else if (forceUse == FORMAT_24) {
                return true
            }

            return DateFormat.is24HourFormat(context)
        }

    /**
     * Indicates which time zone is currently used by this view.
     *
     * @return The ID of the current time zone or null if the default time zone,
     * as set by the user, must be used
     * @see TimeZone
     *
     * @see java.util.TimeZone.getAvailableIDs
     * @see .setTimeZone
     */
    /**
     * Sets the specified time zone to use in this clock. When the time zone
     * is set through this method, system time zone changes (when the user
     * sets the time zone in settings for instance) will be ignored.
     *
     * @param timeZone The desired time zone's ID as specified in [TimeZone]
     * or null to user the time zone specified by the user
     * (system time zone)
     * @see .getTimeZone
     * @see java.util.TimeZone.getAvailableIDs
     * @see TimeZone.getTimeZone
     */
    var timeZone: String?
        get() = mTimeZone
        @RemotableViewMethod
        set(timeZone) {
            mTimeZone = timeZone
            createTime(timeZone)
            onTimeChanged()
        }

    /**
     * Creates a new clock using the default patterns for the current locale.
     *
     * @param context The Context the view is running in, through which it can
     * access the current theme, resources, etc.
     */
    constructor(context: Context) : super(context) {
        init()
    }

    /**
     * Creates a new clock inflated from XML. This object's properties are
     * intialized from the attributes specified in XML.
     *
     *
     * This constructor uses a default style of 0, so the only attribute values
     * applied are those in the Context's Theme and the given AttributeSet.
     *
     * @param context The Context the view is running in, through which it can
     * access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view
     */
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {}

    /**
     * Creates a new clock inflated from XML. This object's properties are
     * intialized from the attributes specified in XML.
     *
     * @param context      The Context the view is running in, through which it can
     * access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view
     * @param defStyleAttr An attribute in the current theme that contains a
     * reference to a style resource that supplies default values for
     * the view. Can be 0 to not look for defaults.
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.TextClock, defStyleAttr, 0)
        try {
            mFormat12 = a.getText(R.styleable.TextClock_format12Hour)
            mFormat24 = a.getText(R.styleable.TextClock_format24Hour)
            mTimeZone = a.getString(R.styleable.TextClock_timeZone)
            forceUse = a.getInt(R.styleable.TextClock_forceUse, FORMAT_AUTO)
        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        if (mFormat12 == null || mFormat24 == null) {
            // LocaleData ld = LocaleData.get(getContext().getResources().getConfiguration().locale);
            if (mFormat12 == null) {
                mFormat12 = LocaleData_timeFormat_hm
            }
            if (mFormat24 == null) {
                mFormat24 = LocaleData_timeFormat_kkm
            }
        }
        createTime(mTimeZone)
        // Wait until onAttachedToWindow() to handle the ticker
        chooseFormat(false)
    }

    private fun createTime(timeZone: String?) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone))
        } else {
            mTime = Calendar.getInstance()
        }
    }

    /**
     * Like setFormat12Hour, but for the content description.
     */
    fun setContentDescriptionFormat12Hour(format: CharSequence) {
        mDescFormat12 = format
        chooseFormat()
        onTimeChanged()
    }

    /**
     * Like setFormat24Hour, but for the content description.
     */
    fun setContentDescriptionFormat24Hour(format: CharSequence) {
        mDescFormat24 = format
        chooseFormat()
        onTimeChanged()
    }

    fun getForceUse(): Int {
        return forceUse
    }

    fun setForceUse(forceUse: Int) {
        this.forceUse = forceUse
        chooseFormat()
        onTimeChanged()
    }

    /**
     * Selects either one of [.getFormat12Hour] or [.getFormat24Hour]
     * depending on whether the user has selected 24-hour format.
     *
     * @param handleTicker true if calling this method should schedule/unschedule the
     * time ticker, false otherwise
     */
    private fun chooseFormat(handleTicker: Boolean = true) {
        val format24Requested = is24HourModeEnabled
        // LocaleData ld = LocaleData.get(getContext().getResources().getConfiguration().locale);
        if (format24Requested) {
            format = abc(mFormat24, mFormat12, LocaleData_timeFormat_kkm)
            mDescFormat = abc(mDescFormat24, mDescFormat12, format)
        } else {
            format = abc(mFormat12, mFormat24, LocaleData_timeFormat_hm)
            mDescFormat = abc(mDescFormat12, mDescFormat24, format)
        }
        val hadSeconds = mHasSeconds
        mHasSeconds = DateFormatCompat.hasSeconds(format)
        if (handleTicker && mAttached && hadSeconds != mHasSeconds) {
            if (hadSeconds)
                handler.removeCallbacks(mTicker)
            else
                mTicker.run()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!mAttached) {
            mAttached = true
            registerReceiver()
            registerObserver()
            createTime(mTimeZone)
            if (mHasSeconds) {
                mTicker.run()
            } else {
                onTimeChanged()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mAttached) {
            unregisterReceiver()
            unregisterObserver()
            handler.removeCallbacks(mTicker)
            mAttached = false
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        context.registerReceiver(mIntentReceiver, filter, null, handler)
    }

    private fun registerObserver() {
        val resolver = context.contentResolver
        resolver.registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver)
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(mIntentReceiver)
    }

    private fun unregisterObserver() {
        val resolver = context.contentResolver
        resolver.unregisterContentObserver(mFormatChangeObserver)
    }

    private fun onTimeChanged() {
        mTime!!.timeInMillis = System.currentTimeMillis()
        text = DateFormat.format(format, mTime)
        contentDescription = DateFormat.format(mDescFormat, mTime)
    }

    companion object {

        /**
         * The default formatting pattern in 12-hour mode. This pattern is used
         * if [.setFormat12Hour] is called with a null pattern
         * or if no pattern was specified when creating an instance of this class.
         *
         *
         * This default pattern shows only the time, hours and minutes, and an am/pm
         * indicator.
         *
         * @see .setFormat12Hour
         * @see .getFormat12Hour
         */
        @Deprecated("Let the system use locale-appropriate defaults instead.")
        val DEFAULT_FORMAT_12_HOUR: CharSequence = "h:mm a"
        /**
         * The default formatting pattern in 24-hour mode. This pattern is used
         * if [.setFormat24Hour] is called with a null pattern
         * or if no pattern was specified when creating an instance of this class.
         *
         *
         * This default pattern shows only the time, hours and minutes.
         *
         * @see .setFormat24Hour
         * @see .getFormat24Hour
         */
        @Deprecated("Let the system use locale-appropriate defaults instead.")
        val DEFAULT_FORMAT_24_HOUR: CharSequence = "H:mm"

        // region [add by imknown]
        val FORMAT_12 = 0
        val FORMAT_24 = 1
        val FORMAT_AUTO = 2

        private val LocaleData_timeFormat_hm = "h:mm"
        private val LocaleData_timeFormat_kkm = "kk:mm"

        /**
         * Returns a if not null, else return b if not null, else return c.
         */
        private fun abc(a: CharSequence?, b: CharSequence?, c: CharSequence?): CharSequence? =
                a ?: (b ?: c)
    }
}

@Target(ElementType.METHOD)
@Retention
annotation class RemotableViewMethod

object DateFormatCompat {

    @Deprecated("Use a literal {@code '} instead.")
    private val QUOTE = '\''

    @Deprecated("Use a literal {@code 's'} instead.")
    private val SECONDS = 's'

    /**
     * Indicates whether the specified format string contains seconds.
     * Always returns false if the input format is null.
     *
     * @param inFormat the format string, as described in [android.text.format.DateFormat]
     *
     * @return true if the format string contains [.SECONDS], false otherwise
     */
    fun hasSeconds(inFormat: CharSequence?): Boolean = hasDesignator(inFormat, SECONDS)

    /**
     * Test if a format string contains the given designator. Always returns
     * `false` if the input format is `null`.
     */
    private fun hasDesignator(inFormat: CharSequence?, designator: Char): Boolean {
        if (inFormat == null)
            return false

        val length = inFormat.length

        var c: Int
        var count: Int

        var i = 0
        while (i < length) {
            count = 1
            c = inFormat[i].toInt()
            if (c == QUOTE.toInt()) {
                count = skipQuotedText(inFormat, i, length)
            } else if (c == designator.toInt()) {
                return true
            }
            i += count
        }

        return false
    }

    private fun skipQuotedText(s: CharSequence?, i: Int, len: Int): Int {
        var i = i
        if (i + 1 < len && s!![i + 1] == QUOTE) {
            return 2
        }

        var count = 1
        // skip leading quote
        i++

        while (i < len) {
            val c = s!![i]

            if (c == QUOTE) {
                count++
                // QUOTEQUOTE -> QUOTE
                if (i + 1 < len && s[i + 1] == QUOTE) {
                    i++
                } else {
                    break
                }
            } else {
                i++
                count++
            }
        }
        return count
    }
}