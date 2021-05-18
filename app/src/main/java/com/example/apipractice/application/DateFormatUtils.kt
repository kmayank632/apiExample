package com.example.apipractice.application

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateFormatUtils {

    /* Some Date and time Formats */
    interface DATE_TIME_FORMAT {
        companion object {
            const val MONOGO_DB_UTC = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'" /* 1993-12-06T05:15:30.600Z */
            const val MMMM_dd_yyyy = "MMMM dd, yyyy" /* December 06, 1993 */
            const val MMM_dd_yyyy = "MMM dd, yyyy" /* Dec 06, 1993 */
            const val EEEE_MMMM_dd = "EEEE, MMMM dd" /* Friday, December 06 */
            const val hh_mm_aa = "hh:mm aa" /* 12:05 AM */
            const val HH_MM = "HH:mm" /* 13:05 */
            const val dd = "dd" /* 06 */
            const val MMM = "MMM" /* Dec */
            const val MM = "MM" /* 12 */
            const val yyyy = "yyyy" /* 1993 */
            const val MMMM_yyyy = "MMMM yyyy" /* December 1993 */
            const val MMMM_dd_yyyy_HH_mm = "MMMM dd, yyyy HH:mm" /* December 06, 1993 23:30 */
            const val MMMM_dd_yyyy_hh_mm_aa =
                "MMMM dd, yyyy hh:mm aa" /* December 06, 1993 2:30 AM */
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

        /* Enable Following Statement when OldFormat is in UTC */
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

    /**
     * Convert LONG time into FORMAT Date/Time String
     *
     * @param timestamp TimeStamp in LONG
     * @param isUTC TRUE if want to convert result into UTC converted
     * @param newFormat New Date/Time Format
     * */
    fun getDateFormatFromLong(timestamp: Long, isUTC: Boolean, newFormat: String?): String {
        val mDate = Date(timestamp)
        val mSimpleDateFormat = SimpleDateFormat(newFormat, Locale.US)
        mSimpleDateFormat.timeZone = TimeZone.getDefault()
        if (isUTC) {
            mSimpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        }
        return mSimpleDateFormat.format(mDate)
    }

}