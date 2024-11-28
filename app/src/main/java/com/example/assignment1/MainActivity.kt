package com.example.assignment1
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        auth = Firebase.auth

        //use to put delay
        Handler(Looper.getMainLooper()).postDelayed({

            val user = auth.currentUser
            if (user != null) {
                //if theres already an account go to home
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
                //use to destroy current activity
                finish()
            } else {
                //go to sign in
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                //use to destroy current activity
                finish()
            }
        },3000)
    }
}
