package com.example.mycomposeapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CommonUtils {

    fun splitBySeparator(input: String, separator:String): Pair<String, String> {
        if(input.isNotEmpty()){
            val parts = input.split(separator)
            return Pair(parts[0], parts[1])
        }
        return Pair(input, input)
    }

    fun isDouble(value: String): Boolean {
        return (value.toDoubleOrNull() != null) || value.isEmpty()
    }

    fun validateDate(input: String): Pair<Boolean,String> {
        val dateFormat = SimpleDateFormat(AppConstants.DD_MM_YYYY_FORMAT, Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            val date = dateFormat.parse(input) ?: return Pair(false,"Please add date in dd/mm/yyyy format (eg:22/09/2024)")

            val calendar = Calendar.getInstance().apply { time = date }
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // Calendar months are 0-based
            val year = calendar.get(Calendar.YEAR)

            if (month !in 1..12) return Pair(false,"Oops there is not such month, Please add a valid month")
            if (day > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) return Pair(false,"Oops $month don't have $day days ")
            if (year < 2024) return Pair(false,"Please add a year of 2024 or above")

            Pair(true,AppConstants.EMPTY)
        } catch (e: ParseException) {
            Pair(false,"Wrong date, please correct")
        }
    }

    fun formattingDate(inputDate:String):Pair<String,String>{
        val inputFormat = SimpleDateFormat(AppConstants.DD_MM_YYYY_FORMAT, Locale.getDefault()) // Input format
        val outputFormatDayMonth = SimpleDateFormat(AppConstants.DD_MMM_FORMAT, Locale.getDefault()) // Output format for day and month
        val outputFormatYear = SimpleDateFormat(AppConstants.YYYY_FORMAT, Locale.getDefault()) // Output format for year

        try {
            val date = inputFormat.parse(inputDate)

            // Format into "09 Sept" and "2024"
            val dayMonth = date?.let { outputFormatDayMonth.format(it) }?:AppConstants.EMPTY
            val year = date?.let { outputFormatYear.format(it) }?:AppConstants.EMPTY

          return Pair(dayMonth,year)
        } catch (e: Exception) {
            e.printStackTrace() // Handle invalid date format
        }
        return Pair(AppConstants.EMPTY,AppConstants.EMPTY)
    }

    fun getTodayDate():String{
        val dateFormat = SimpleDateFormat(AppConstants.DD_MM_YYYY_FORMAT, Locale.getDefault()) // Date format
        val currentDate = Date() // Get the current date

        return dateFormat.format(currentDate)
    }

    fun getMonthFromDate(input:String):String{
        if(input.isNotEmpty()){
            val monthNames = arrayOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            val parts = input.split("/")
            val monthNumber = parts[0].toInt()
            return monthNames[monthNumber - 1]
        }
        return AppConstants.EMPTY
    }

    fun getMonthYearFromDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat(AppConstants.DD_MM_YYYY_FORMAT, Locale.getDefault())
        val outputFormat = SimpleDateFormat(AppConstants.MM_YYYY_FORMAT, Locale.getDefault())
        val date = inputFormat.parse(inputDate)

        return date?.let { outputFormat.format(it) }?:AppConstants.EMPTY
    }
}

