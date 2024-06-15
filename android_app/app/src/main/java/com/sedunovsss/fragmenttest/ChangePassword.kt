package com.sedunovsss.fragmenttest

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ChangePassword : Fragment() {
    lateinit var oldPassword: EditText
    lateinit var newPassword: EditText
    lateinit var submit: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serverAddress = resources.getString(R.string.server_address).toString()
        oldPassword = view.findViewById(R.id.oldPassword)
        newPassword = view.findViewById(R.id.newPassword)
        submit = view.findViewById(R.id.submit)
        val errorText = activity!!.findViewById<View>(R.id.error) as TextView
        val prefs = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userCookie = prefs?.getString("userCookie", "").toString()

        submit.setOnClickListener(View.OnClickListener {
            val oldPasswordText = oldPassword.text.toString()
            val newPasswordText = newPassword.text.toString()
            val urlText = "$serverAddress/change_password"
            val postData = "login=$userCookie&password=$oldPasswordText&new_password=$newPasswordText"
            val thread = Thread({
                val result = PostRequestSender(urlText, postData).send()
                activity?.runOnUiThread{
                    if (result == "success")
                        errorText.setText("")
                    else
                        errorText.setText(result)
                }
            })
            thread.start()
        })
    }

}