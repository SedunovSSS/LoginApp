package com.sedunovsss.fragmenttest

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class DeleteAccount : Fragment() {
    lateinit var password: EditText
    lateinit var submit: Button
    lateinit var iAgree : CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detete_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serverAddress = resources.getString(R.string.server_address).toString()
        password = view.findViewById(R.id.password)
        submit = view.findViewById(R.id.submit)
        val errorText = activity!!.findViewById<View>(R.id.error) as TextView
        val showUser = activity!!.findViewById<View>(R.id.showUser) as TextView
        iAgree = view.findViewById(R.id.iAgree)

        val prefs = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userCookie = prefs?.getString("userCookie", "").toString()

        submit.setOnClickListener(View.OnClickListener {
            val loginUser = userCookie
            val passwordUser = password.text.toString()
            val urlText = "$serverAddress/delete_user"
            val postData = "login=$loginUser&password=$passwordUser"
            if (iAgree.isChecked()){
                val thread = Thread({
                    val result = PostRequestSender(urlText, postData).send()
                    activity?.runOnUiThread{
                        if (result == "success"){
                            prefs?.edit()?.putString("userCookie", "")?.apply()
                            showUser.setText(R.string.user_not_logged_in)
                            errorText.setText("")
                            parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.transaction, R.anim.transaction_back).addToBackStack(null).replace(R.id.fragmentContainerView2, Login()).commit()
                        }
                        else
                            errorText.setText(result)
                    }
                })
                thread.start()
            }
        })
    }

}