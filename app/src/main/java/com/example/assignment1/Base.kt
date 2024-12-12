package com.example.assignment1

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.example.assignment1.UserModel

object Base {

    fun showProgressBar(context:Context):Dialog
    {
        val dialogue = Dialog(context)
        dialogue.setContentView(R.layout.scrore_dialog)
        dialogue.show()

        return dialogue
    }

    fun showToast(context: Context, msg:String)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

//    fun desiredScore(userInfo: UserModel, type: String):Double{
//        return when(type){
//            Constants.allTimeScore-> userInfo.allTimeScore
//            Constants.weeklyScore->userInfo.weeklyScore
//            Constants.monthlyScore->userInfo.monthlyScore
//            else -> 0.0
//        }
//    }
}