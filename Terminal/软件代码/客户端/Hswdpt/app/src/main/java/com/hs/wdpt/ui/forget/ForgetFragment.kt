package com.hs.wdpt.ui.forget

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
import android.widget.Toast
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.home.HomeFragment
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic

class ForgetFragment : Fragment() {

    companion object {
        fun newInstance() = ForgetFragment()
    }

    private lateinit var viewModel: ForgetViewModel

    //设置检测不规则字符
    val irregular:String = "`~!@#$%^&*()_-+=\"|{}[]:;',.<>/?"
    fun checkIrregalChar(str :String):Int
    {
        for(i in 0..str.length-1)
        {
            if(irregular.indexOf(str[i])>=0)
            {
                return 0;
            }
        }
        return 1;
    }
    //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_forget, container, false)
        val mailbox = root.findViewById<EditText>(R.id.mailbox)
        val name = root.findViewById<EditText>(R.id.name)
        val verification_code = root.findViewById<EditText>(R.id.verification_code)
        val password = root.findViewById<EditText>(R.id.password)
        val confirm_password = root.findViewById<EditText>(R.id.confirm_password)
        val set_code = root.findViewById<Button>(R.id.set_code)
        val confirm = root.findViewById<Button>(R.id.confirm)

        set_code.setOnClickListener {
            set_code.isEnabled = false
            val name_test = name.text.toString()
            val mailbox_test = mailbox.text.toString()

            when {
                name_test == "" -> {
                    Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mailbox_test == "" -> {
                    Toast.makeText(context, "请输入邮箱", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                checkIrregalChar(name_test) == 0 -> {
                    Toast.makeText(context, "用户名包含了不规则字符", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
//            if (password.text.toString().length < 6) {
//                Toast.makeText(context, "密码长度不能小于6", Toast.LENGTH_SHORT).show()
//            } else if (checkIrregalChar(password.text.toString()) == 0) {
//                Toast.makeText(context, "密码包含了不规则字符", Toast.LENGTH_SHORT).show()
//            } else if(password.text.toString()!=confirm_password.text.toString()){
//                Toast.makeText(context,"两次密码输入不一致", Toast.LENGTH_SHORT).show()
//            } else {
//                val activity = context;
//                if (activity is Activity) {
//                    val mainActivity = activity as MainActivity
//                    mainActivity.switchFragment(LoginFragment())
//                }
//            }


        val reqUrl =
            HttpDataGet.baseUrl + HttpDataGet.URI_findPassword + "?account=${name_test}&email=${mailbox_test}"
        object : HttpDataGet() {
            override fun call(data: Any, what: Int) {
                Log.v("Error", this.response)
                when (what) {
                    HttpDataGet.dataMap -> {
                        val dt = data as Map<String, Any>
                        if (dt.containsKey("state")) {
                            val state = dt["state"] as Boolean
                            if (state) {
                                Toast.makeText(context,"发送验证码成功,请在邮箱查看",Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context,"发送验证码失败",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                set_code.isEnabled = true
            }

            override fun error(error: String) {
                Toast.makeText(context, "发送验证码失败", Toast.LENGTH_SHORT).show()
                set_code.isEnabled = true
            }
        }.request(reqUrl)
    }

        confirm.setOnClickListener {
            val name_test = name.text.toString()
            val code=verification_code.text.toString()
            val password_test = password.text.toString()
            val confirm_password_test = confirm_password.text.toString()

            when {
                code == "" -> {
                    Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password_test == "" -> {
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password_test != confirm_password_test -> {
                    Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password_test.length < 6 -> {
                    Toast.makeText(context, "密码长度不能小于6", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password_test.length > 15 -> {
                    Toast.makeText(context, "密码长度不能大于15", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val reqUrl =
                HttpDataGet.baseUrl + HttpDataGet.URI_resetPassword + "?account=${name_test}&password=${password_test}&emailCode=${code}"
            object : HttpDataGet() {
                override fun call(data: Any, what: Int) {
                    Log.v("Error", this.response)
                    when (what) {
                        HttpDataGet.dataMap -> {
                            val dt = data as Map<String, Any>
                            if (dt.containsKey("state")) {
                                val state = dt["state"] as Boolean
                                if (state) {
                                    Toast.makeText(context, "重置密码成功", Toast.LENGTH_SHORT).show()
                                    (context as MainActivity).onBackPressed()
                                } else {
                                    if (dt.containsKey("message")) {
                                        val msg = dt["message"] as String
                                        Toast.makeText(
                                            context,
                                            "重置密码失败: 原因：${msg}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else Toast.makeText(context, "重置密码失败", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
                override fun error(error: String) {
                    Toast.makeText(context, "重置密码失败: ${error}", Toast.LENGTH_SHORT).show()
                }
            }.request(reqUrl)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}