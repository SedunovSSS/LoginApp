package com.sedunovsss.fragmenttest

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    lateinit var login : ImageButton
    lateinit var register : ImageButton
    lateinit var logout : ImageButton
    lateinit var delete : ImageButton
    lateinit var change : ImageButton
    lateinit var showUser : TextView
    lateinit var errorText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login = findViewById(R.id.logButton)
        register = findViewById(R.id.regButton)
        logout = findViewById(R.id.logoutButton)
        delete = findViewById(R.id.delButton)
        change = findViewById(R.id.changeButton)
        showUser = findViewById(R.id.showUser)
        errorText = findViewById(R.id.error)

        val classLogin = Login()
        val classRegistration = Registration()
        val classChangePassword = ChangePassword()
        val classDeleteAccount = DeleteAccount()

        var userCookie = ""

        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        try {
            userCookie = prefs.getString("userCookie", "").toString()
        }
        catch (e: Exception){
            userCookie = ""
        }
        if (userCookie != ""){
            showUser.setText(userCookie)
        }

        login.setOnClickListener(View.OnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.transaction, R.anim.transaction_back)
            transaction.replace(R.id.fragmentContainerView2, classLogin).addToBackStack(null).commit()
        })

        register.setOnClickListener(View.OnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.transaction, R.anim.transaction_back)
            transaction.replace(R.id.fragmentContainerView2, classRegistration).addToBackStack(null).commit()
        })

        logout.setOnClickListener(View.OnClickListener {
            userCookie = ""
            prefs?.edit()?.putString("userCookie", userCookie)?.apply()
            showUser.setText(R.string.user_not_logged_in)
        })

        change.setOnClickListener(View.OnClickListener {
            userCookie = prefs.getString("userCookie", "").toString()
            if (userCookie == ""){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.transaction, R.anim.transaction_back)
                transaction.replace(R.id.fragmentContainerView2, classLogin).addToBackStack(null).commit()
            }
            else{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.transaction, R.anim.transaction_back)
                transaction.replace(R.id.fragmentContainerView2, classChangePassword).addToBackStack(null).commit()
            }
        })

        delete.setOnClickListener(View.OnClickListener {
            userCookie = prefs.getString("userCookie", "").toString()
            if (userCookie == ""){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.transaction, R.anim.transaction_back)
                transaction.replace(R.id.fragmentContainerView2, classLogin).addToBackStack(null).commit()
            }
            else{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.transaction, R.anim.transaction_back)
                transaction.replace(R.id.fragmentContainerView2, classDeleteAccount).addToBackStack(null).commit()
            }
        })
        errorText.setOnClickListener(View.OnClickListener {
            if (errorText.text.toString() != ""){
                errorText.setText("")
            }
        })
    }
}