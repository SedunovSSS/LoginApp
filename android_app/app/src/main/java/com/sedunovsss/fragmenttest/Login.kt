package com.sedunovsss.fragmenttest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment


class Login : Fragment() {
    lateinit var login: EditText
    lateinit var password: EditText
    lateinit var submit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serverAddress = resources.getString(R.string.server_address).toString()
        login = view.findViewById(R.id.login)
        password = view.findViewById(R.id.password)
        submit = view.findViewById(R.id.submit)
        val errorText = activity!!.findViewById<View>(R.id.error) as TextView
        val prefs = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val showUser = activity!!.findViewById<View>(R.id.showUser) as TextView
        submit.setOnClickListener(View.OnClickListener {
            val loginUser = login.text.toString()
            val passwordUser = password.text.toString()
            val urlText = "$serverAddress/login"
            val postData = "login=$loginUser&password=$passwordUser"
            val thread = Thread({
                val result = PostRequestSender(urlText, postData).send()
                activity?.runOnUiThread{
                    if (result == "success"){
                        prefs?.edit()?.putString("userCookie", loginUser.toString().lowercase())?.apply()
                        showUser.setText(loginUser.lowercase())
                        errorText.setText("")
                    }
                    else
                        errorText.setText(result)
                }
            })
            thread.start()
        })
    }
}