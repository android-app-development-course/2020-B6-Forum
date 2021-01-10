package com.hs.wdpt.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.forget.ForgetFragment
import com.hs.wdpt.ui.home.HomeFragment
import com.hs.wdpt.ui.register.RegisterFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import java.net.URLEncoder

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val registerButton: Button = root.findViewById(R.id.register)

        val name = root.findViewById<EditText>(R.id.name)
        val password = root.findViewById<EditText>(R.id.password)




        registerButton.setOnClickListener {
            val activity = context
            if(activity is Activity){
                val mainActivity = activity as MainActivity
                mainActivity.switchFragment(RegisterFragment())
            }
        }
        root.findViewById<Button>(R.id.forget).setOnClickListener {
            val activity = context
            if(activity is Activity){
                val mainActivity = activity as MainActivity
                mainActivity.switchFragment(ForgetFragment())
            }
        }
        root.findViewById<Button>(R.id.land).setOnClickListener {
            val name_test = name.text.toString()
            val password_test = password.text.toString()
            when {
                name_test == "" -> {
                    Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password_test == "" -> {
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val reqUrl =
                HttpDataGet.baseUrl + HttpDataGet.URI_Login + "?account=${URLEncoder.encode(name_test)}&password=${password_test}"
            object : HttpDataGet() {
                override fun call(data: Any, what: Int) {
                    Log.v("Error", this.response)
                    when (what) {
                        HttpDataGet.dataMap -> {
                            val dt = data as Map<String, Any>
                            if (dt.containsKey("state")) {
                                val state = dt["state"] as Boolean
                                if (state) {
                                    var user : UserStatic?= context?.let { it1 -> UserStatic(it1) }
                                    val msg = dt["message"] as String
                                    val msgg = msg.split("&")
                                    val id = msgg[0]
                                    user?.login(id)
                                    Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show()
                                    (context as MainActivity).switchFragment(HomeFragment())
                                } else {
                                    if (dt.containsKey("message")) {
                                        val msg = dt["message"] as String
                                        Toast.makeText(
                                            context,
                                            "登陆失败: 原因${msg}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
                override fun error(error: String) {
                    Toast.makeText(context, "登陆失败: ${error}", Toast.LENGTH_SHORT).show()
                }
            }.request(reqUrl)
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}