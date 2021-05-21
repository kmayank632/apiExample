package com.example.apipractice.utills

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateFormatUtils {

    /** Some Date and time Formats */
    interface DATE_TIME_FORMAT {
        companion object {
            const val MONOGO_DB_UTC = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'" /* 1993-12-06T05:15:30.600Z */
            const val dd_MM_yyyy = "dd-MM-yyyy"
        }
    }

    /**
     * Convert TimeStamp to any format
     *
     * @param inputData Which you want to convert
     * @param isOldInUTC if old Time in UTC
     * @param mOldFormat Format of the inputData
     * @param shouldNewInUTC if want to convert output in UTC
     * @param mNewFormat Output data Format
     *
     * */
    fun getDateByFormatCustomUTC(
        inputData: String,
        isOldInUTC: Boolean,
        mOldFormat: String,
        shouldNewInUTC: Boolean,
        mNewFormat: String
    ): String? {
        var mSimpleDateFormat = SimpleDateFormat(mOldFormat, Locale.US)

        /** Enable Following Statement when OldFormat is in UTC */
        if (isOldInUTC) {
            mSimpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        }
        var mDate: Date? = null
        try {
            mDate = mSimpleDateFormat.parse(inputData)
        } catch (e: ParseException) {

        }
        if (mDate != null) {
            mSimpleDateFormat = SimpleDateFormat(mNewFormat, Locale.US)
            if (shouldNewInUTC) {
                mSimpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            } else {
                mSimpleDateFormat.timeZone = TimeZone.getDefault()
            }
            return mSimpleDateFormat.format(mDate)
        }
        return inputData
    }

}