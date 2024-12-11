package com.example.assignment1


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.text.HtmlCompat
import com.example.assignment1.CategoryModel
import com.example.assignment1.QuizModel
import kotlin.random.Random

object Constants {

    val difficultyList = listOf("Any", "Easy","Medium", "Hard")
    val typeList = listOf("Any","Multiple Choice", "True/false")
    const val user = "USER"
    const val allTimeScore = "allTimeScore"
    const val weeklyScore = "weeklyScore"
    const val monthlyScore = "monthlyScore"
    const val lastGameScore = "lastGameScore"


    fun isNetworkAvailable(context: Context):Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val network = connectivityManager.activeNetwork?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network)?: return false

        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)-> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)-> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> true
            else -> false
        }
    }

    fun getCategoryItemList():ArrayList<CategoryModel>
    {
        val list = ArrayList<CategoryModel>()
        list.add(
            CategoryModel("9", "General Knowledge")
        )
        list.add(
            CategoryModel("10","Books")
        )
        list.add(
            CategoryModel("11","Film")
        )
        list.add(
            CategoryModel("12","Music")
        )
        list.add(
            CategoryModel("13","Musicals & Theatres")
        )
        list.add(
            CategoryModel("14","Television")
        )
        list.add(
            CategoryModel("15","Video Games")
        )
        list.add(
            CategoryModel("16","Board Games")
        )
        list.add(
            CategoryModel("17","Science & Nature")
        )
        list.add(
            CategoryModel("18","Computer")
        )
        list.add(
            CategoryModel("19","Mathematics")
        )
        list.add(
            CategoryModel("20","Mythology")
        )
        list.add(
            CategoryModel("21","Sports")
        )
        list.add(
            CategoryModel("22","Geography")
        )
        list.add(
            CategoryModel("23","History")
        )
        list.add(
            CategoryModel("24","Politics")
        )
        list.add(
            CategoryModel("25","Art")
        )
        list.add(
            CategoryModel("26","Celebrities")
        )
        list.add(
            CategoryModel("27","Animals")
        )
        list.add(
            CategoryModel("28","Vehicles")
        )
        list.add(
            CategoryModel("29","Comics")
        )
        list.add(
            CategoryModel("30","Gadgets")
        )
        list.add(
            CategoryModel("31","Anime & Manga")
        )

        list.add(
            CategoryModel("32","Cartoons & Animations")
        )
        return list
    }

    fun getRandomOptions(correctAnswer:String,incorrectAnswer:List<String>):Pair<String,List<String>>
    {
        val list  = mutableListOf<String>()
        val decodedCorrectAnswer = decodeHtmlString(correctAnswer)
        list.add(decodedCorrectAnswer)
        for (i in incorrectAnswer)
        {
            list.add(decodeHtmlString(i))
        }
        list.shuffle()
        return Pair(decodedCorrectAnswer,list)
    }

    fun decodeHtmlString(htmlEncoded: String): String {
        return HtmlCompat.fromHtml(htmlEncoded, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }

    fun getCategoryStringArray():List<String>
    {
        val list = getCategoryItemList()
        val result = mutableListOf<String>()
        result.add("Any")
        for(i in list)
            result.add(i.name)
        return result
    }
}