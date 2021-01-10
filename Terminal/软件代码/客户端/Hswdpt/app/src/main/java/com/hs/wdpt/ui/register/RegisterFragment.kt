package com.hs.wdpt.ui.register

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.util.HttpDataGet
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception
import java.net.URLEncoder

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container, false)
        rootView = root
        try {
            initView()
        } catch (e: Exception) {
            Log.v("initView", e.toString())
        }

        return root
    }

    ///view///
    lateinit var rootView: View
    lateinit var register: Button
    lateinit var userName: EditText
    lateinit var userPassword: EditText
    lateinit var againPassword: EditText
    lateinit var mailBox: EditText
    private fun initView() {
        register = rootView.findViewById(R.id.register)
        userName = rootView.findViewById(R.id.name)
        userPassword = rootView.findViewById(R.id.password)
        againPassword = rootView.findViewById(R.id.again_password)
        mailBox = rootView.findViewById(R.id.mailbox)
        register.setOnClickListener {
            val name = userName.text.toString()
            val up = userPassword.text.toString()
            val ap = againPassword.text.toString()
            val mail = mailBox.text.toString()
            when {
                name == "" -> {
                    Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                name.length > 10 ->{
                    Toast.makeText(context, "密码长度不能大于10", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                up == "" -> {
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                up != ap -> {
                    Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mail == "" -> {
                    Toast.makeText(context, "请输入邮箱", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                up.length < 6 -> {
                    Toast.makeText(context, "密码长度不能小于6", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                up.length > 15 -> {
                    Toast.makeText(context, "密码长度不能大于15", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                checkIrregalChar(name) == 0 -> {
                    Toast.makeText(context, "用户名包含了不规则字符", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val reqUrl =
                HttpDataGet.baseUrl + HttpDataGet.URI_Register + "?account=${URLEncoder.encode(name)}&password=${up}&email=${mail}&isAdmin=0"
            object : HttpDataGet() {
                override fun call(data: Any, what: Int) {
                    Log.v("Error", this.response)
                    when (what) {
                        HttpDataGet.dataMap -> {
                            val dt = data as Map<String, Any>
                            if (dt.containsKey("state")) {
                                val state = dt["state"] as Boolean
                                if (state) {
                                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
                                    (context as MainActivity).onBackPressed()
                                } else {
                                    if (dt.containsKey("message")) {
                                        val msg = dt["message"] as String
                                        Toast.makeText(
                                            context,
                                            "注册失败: 原因${msg}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                       // Log.v("tag","注册失败: 原因${msg}")
                                    } else Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
                override fun error(error: String) {
                    Toast.makeText(context, "注册失败: ${error}", Toast.LENGTH_SHORT).show()
                }
            }.request(reqUrl)
        }
    }

    val irregular: String = "`~!@#$%^&*()_-+=\"|{}[]:;',.<>/?"
    fun checkIrregalChar(str: String): Int {
        for (i in 0..str.length - 1) {
            if (irregular.indexOf(str[i]) >= 0) {
                return 0;
            }
        }
        return 1;
    }
}