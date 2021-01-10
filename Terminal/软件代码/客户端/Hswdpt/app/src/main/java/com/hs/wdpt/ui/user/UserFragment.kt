package com.hs.wdpt.ui.user

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
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import java.net.URLEncoder

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        val nickname = root.findViewById<EditText>(R.id.nickname)
        val id = root.findViewById<EditText>(R.id.id)
        val name = root.findViewById<EditText>(R.id.name)
        val student_number = root.findViewById<EditText>(R.id.student_number)
        val college = root.findViewById<EditText>(R.id.college)
        val grade = root.findViewById<EditText>(R.id.grade)
        val phone_number = root.findViewById<EditText>(R.id.phone_number)
        val mailbox = root.findViewById<EditText>(R.id.mailbox)
        val edit = root.findViewById<Button>(R.id.edit)
        val save = root.findViewById<Button>(R.id.save)
        val logout = root.findViewById<Button>(R.id.logout)
        //获取用户id
        var _id = ""
        var user: UserStatic? = context?.let { it1 -> UserStatic(it1) }
        if (user != null) {
            _id = user.getLoginId().toString()
            id.setText(_id)
        }
        Log.v("tag", _id)


        //获取用户信息
        val reqUrl =
            HttpDataGet.baseUrl + HttpDataGet.URI_get + "?id=${_id}"
        object : HttpDataGet() {
            override fun call(data: Any, what: Int) {
                Log.v("Error", this.response)
                when (what) {
                    HttpDataGet.dataMap -> {
                        val dt = data as Map<String, Any>
                        if (dt.containsKey("nickName")) {
                            val _nickname = dt["nickName"] as String
                            nickname.setText(_nickname)
                        }
                        if (dt.containsKey("name")) {
                            val _name = dt["name"]
                            if (_name == null || _name == "null") {
                                name.setText("")
                            } else {
                                name.setText(_name.toString())
                            }
                        }
                        if (dt.containsKey("studentId")) {
                            val _student_number = dt["studentId"]
                            if (_student_number == null) {
                                student_number.setText("")
                            } else {
                                student_number.setText(_student_number.toString())
                            }
                        }
                        if (dt.containsKey("college")) {
                            val _college = dt["college"]
                            if (_college == null) {
                                college.setText("")
                            } else {
                                college.setText(_college.toString())
                            }
                        }
                        if (dt.containsKey("grade")) {
                            val _grade = dt["grade"]
                            if (_grade == null) {
                                grade.setText("")
                            } else {
                                grade.setText(_grade.toString())
                            }
                        }
                        if (dt.containsKey("phoneNumber")) {
                            val _phone_number = dt["phoneNumber"]
                            if (_phone_number == null) {
                                phone_number.setText("")
                            } else {
                                phone_number.setText(_phone_number.toString())
                            }
                        }
                        if (dt.containsKey("email")) {
                            val _mailbox = dt["email"] as String
                            mailbox.setText(_mailbox)
                        }
                    }
                }
            }

            override fun error(error: String) {
                Toast.makeText(context, "失败: ${error}", Toast.LENGTH_SHORT).show()
            }
        }.request(reqUrl)
        nickname.setFocusable(false);
        nickname.setFocusableInTouchMode(false);
        id.setFocusable(false);
        id.setFocusableInTouchMode(false);
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        student_number.setFocusable(false);
        student_number.setFocusableInTouchMode(false);
        college.setFocusable(false);
        college.setFocusableInTouchMode(false);
        grade.setFocusable(false);
        grade.setFocusableInTouchMode(false);
        phone_number.setFocusable(false);
        phone_number.setFocusableInTouchMode(false);
        mailbox.setFocusable(false);
        mailbox.setFocusableInTouchMode(false);
        edit.setOnClickListener {
            nickname.setFocusableInTouchMode(true);
            nickname.setFocusable(true);
            name.setFocusableInTouchMode(true);
            name.setFocusable(true);
            student_number.setFocusableInTouchMode(true);
            student_number.setFocusable(true);
            college.setFocusableInTouchMode(true);
            college.setFocusable(true);
            grade.setFocusableInTouchMode(true);
            grade.setFocusable(true);
            phone_number.setFocusableInTouchMode(true);
            phone_number.setFocusable(true);
            mailbox.setFocusableInTouchMode(true);
            mailbox.setFocusable(true);
        }
        save.setOnClickListener {
            val nickname_test = nickname.text.toString()
            val id_test = id.text.toString()
            val name_test = name.text.toString()
            val student_number_test = student_number.text.toString()
            val college_test = college.text.toString()
            val grade_test = grade.text.toString()
            val phone_number_test = phone_number.text.toString()
            val mailbox_test = mailbox.text.toString()

            when {
                nickname_test == "" -> {
                    Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mailbox_test == "" -> {
                    Toast.makeText(context, "请输入邮箱", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                checkIrregalChar(nickname_test) == 0 -> {
                    Toast.makeText(context, "用户名包含了不规则字符", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val reqUrl =
                HttpDataGet.baseUrl + HttpDataGet.URI_update + "?id=${id_test}&account=${URLEncoder.encode(
                    nickname_test
                )}&name=${URLEncoder.encode(name_test)}&studentId=${student_number_test}" +
                        "&college=${URLEncoder.encode(college_test)}&grade=${URLEncoder.encode(
                            grade_test
                        )}&phoneNumber=${phone_number_test}&email=${mailbox_test}"
            object : HttpDataGet() {
                override fun call(data: Any, what: Int) {
                    Log.v("Error", this.response)
                    when (what) {
                        HttpDataGet.dataMap -> {
                            val dt = data as Map<String, Any>
                            if (dt.containsKey("state")) {
                                val state = dt["state"] as Boolean
                                if (state) {
                                    nickname.setFocusable(false);
                                    nickname.setFocusableInTouchMode(false);
                                    name.setFocusable(false);
                                    name.setFocusableInTouchMode(false);
                                    student_number.setFocusable(false);
                                    student_number.setFocusableInTouchMode(false);
                                    college.setFocusable(false);
                                    college.setFocusableInTouchMode(false);
                                    grade.setFocusable(false);
                                    grade.setFocusableInTouchMode(false);
                                    phone_number.setFocusable(false);
                                    phone_number.setFocusableInTouchMode(false);
                                    mailbox.setFocusable(false);
                                    mailbox.setFocusableInTouchMode(false);
                                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                                } else {
                                    if (dt.containsKey("message")) {
                                        val msg = dt["message"] as String
                                        Toast.makeText(
                                            context,
                                            "修改失败: 原因：${msg}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }

                override fun error(error: String) {
                    Toast.makeText(context, "修改失败: ${error}", Toast.LENGTH_SHORT).show()
                }
            }.request(reqUrl)
        }
        logout.setOnClickListener {
            user?.unLogin()
            (context as MainActivity).switchFragment(LoginFragment())
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
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