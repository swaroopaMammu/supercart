package com.example.mycomposeapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CommonUtils {

    fun splitNumericAndText(input: String): Pair<String, String> {
        val numberPattern = Regex("\\d+(\\.\\d+)?")
        val textPattern = Regex("[a-zA-Z]+")

        val numberMatch = numberPattern.find(input)?.value ?: ""
        val textMatch = textPattern.find(input)?.value ?: ""

        return Pair(numberMatch, textMatch)
    }

    fun validateDate(input: String): Pair<Boolean,String> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            val date = dateFormat.parse(input) ?: return Pair(false,"Please add date in dd/mm/yyyy format (eg:22/09/2024)")

            val calendar = Calendar.getInstance().apply { time = date }
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // Calendar months are 0-based
            val year = calendar.get(Calendar.YEAR)

            if (month !in 1..12) return Pair(false,"Oops there is not such month, Please add valid month")
            if (day > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) return Pair(false,"Oops $month don't have $day days ")
            if (year < 2024) return Pair(false,"Please add a year of 2024 or above")

            Pair(true,"")
        } catch (e: ParseException) {
            Pair(false,"Wrong date, please correct")
        }
    }

    fun formattingDate(inputDate:String):Pair<String,String>{
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Input format
        val outputFormatDayMonth = SimpleDateFormat("dd MMM", Locale.getDefault()) // Output format for day and month
        val outputFormatYear = SimpleDateFormat("yyyy", Locale.getDefault()) // Output format for year

        try {
            val date = inputFormat.parse(inputDate) // Parse the input date string

            // Format into "09 Sept" and "2024"
            val dayMonth = date?.let { outputFormatDayMonth.format(it) }?:""
            val year = date?.let { outputFormatYear.format(it) }?:""
          return Pair(dayMonth,year)
        } catch (e: Exception) {
            e.printStackTrace() // Handle invalid date format
        }
        return Pair("","")
    }

    fun getTodayDate():String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Date format
        val currentDate = Date() // Get the current date

        return dateFormat.format(currentDate)
    }

    fun getMonthYearFromDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)

        return outputFormat.format(date)
    }
}

