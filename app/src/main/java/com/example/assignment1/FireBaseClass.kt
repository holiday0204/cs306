package com.example.assignment1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.assignment1.Base
import com.example.assignment1.Constants
import com.example.assignment1.UserModel
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FireBaseClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getUserInfo(email: String, callback: UserInfoCallback) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        // Fetch the user document using the email
        usersCollection.document(email).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Convert Firestore document to UserModel
                    val userInfo = documentSnapshot.toObject(UserModel::class.java)
                    callback.onUserInfoFetched(userInfo)
                } else {
                    callback.onUserInfoFetched(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseError", "Error fetching user info", exception)
                callback.onUserInfoFetched(null)
            }
    }

//fun getUserInfo(email: String, callback: UserInfoCallback) {
//    // Fetch user data from Firestore
//    val db = FirebaseFirestore.getInstance()
//
//    db.collection("users")
//        .whereEqualTo("email", email)  // Query documents by email instead of userId
//        .get()
//        .addOnSuccessListener { querySnapshot ->
//            for (document in querySnapshot.documents) {
//                // Assuming there's only one document per email, you can use the first one
//                val userInfo = document.toObject(UserModel::class.java)
//                callback.onUserInfoFetched(userInfo) // Pass the fetched user data
//                return@addOnSuccessListener
//            }
//            callback.onUserInfoFetched(null) // No user data found for the given email
//        }
//        .addOnFailureListener { e ->
//            Log.e("FireBaseClass", "Error fetching user info: $e")
//            callback.onUserInfoFetched(null) // In case of an error
//        }
//}

    interface UserInfoCallback {
        fun onUserInfoFetched(userInfo: UserModel?)
    }


    fun updateProfile(name: String?, imgUri: Uri?, email: String) {
        mFireStore.collection(Constants.user)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    if (!name.isNullOrEmpty()) {
                        document.reference.update("name", name)
                    }
                    if (imgUri != null) {
                        uploadImage(imgUri, email)
                    }
                }
            }
    }

    fun setProfileImage(imageRef: String?, view: ShapeableImageView) {
        if (!imageRef.isNullOrEmpty()) {
            val storageRef = FirebaseStorage.getInstance().reference
            val pathReference = storageRef.child(imageRef)
            val ONE_MEGABYTE: Long = 1024 * 1024
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { byteArray ->
                val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                view.setImageBitmap(bmp)
            }
        }
    }

    fun uploadImage(imgUri: Uri, email: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val profilePicRef = storageRef.child("profile_pictures/$email")
        val uploadTask = profilePicRef.putFile(imgUri)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.storage.downloadUrl
                val userDocument = mFireStore.collection(Constants.user).document(email)
                userDocument.update("image", downloadUrl.toString())
            } else {
                Log.e("ImageUpload", "Unsuccessful")
            }
        }
    }


//    fun updateScore(newScore: Double) {
//        val userDocument = mFireStore.collection(Constants.user).document(getCurrentUserId())
//        getUserInfo(object : UserInfoCallback {
//            override fun onUserInfoFetched(userInfo: UserModel?) {
//                userInfo?.let {
//                    val newAllTimeScore = userInfo.allTimeScore + newScore
//                    val newWeeklyScore = userInfo.weeklyScore + newScore
//                    val newMonthlyScore = userInfo.monthlyScore + newScore
//                    userDocument.update(
//                        Constants.allTimeScore, newAllTimeScore,
//                        Constants.weeklyScore, newWeeklyScore,
//                        Constants.monthlyScore, newMonthlyScore,
//                        Constants.lastGameScore, newScore
//                    ).addOnSuccessListener {
//                        Log.e("DataUpdate", "Updated")
//                    }.addOnFailureListener {
//                        Log.e("DataUpdate", "Failed")
//                    }
//                }
//            }
//        })
//    }

    fun getUserRank(type: String, email: String, callback: UserRankCallback) {
        mFireStore.collection(Constants.user).orderBy(type, Query.Direction.DESCENDING)
            .get().addOnSuccessListener { result ->
                var rank = 1
                for (document in result) {
                    if (document.id == email)
                        break
                    rank += 1
                }
                callback.onUserRankFetched(rank)
            }
            .addOnFailureListener {
                Log.e("QueryResult", "Failure")
                callback.onUserRankFetched(null)
            }
    }

    fun doesDocumentExist(documentId: String): Task<Boolean> {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection(Constants.user).document(documentId)

        return documentRef.get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    task.result?.exists() ?: false
                } else {
                    false
                }
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = Firebase.auth.currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
            Log.d("FireBaseClass", "Current user UID: $currentUserId")
        } else {
            Log.d("FireBaseClass", "No user is signed in")  // Log if no user is signed in
        }
        return currentUserId
    }

    interface UserRankCallback {
        fun onUserRankFetched(rank: Int?)
    }
}