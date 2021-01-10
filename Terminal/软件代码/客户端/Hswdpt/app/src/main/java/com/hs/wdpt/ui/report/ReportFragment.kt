package com.hs.wdpt.ui.report

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import kotlinx.android.synthetic.main.fragment_report.*
import java.net.URLEncoder

class ReportFragment : Fragment() {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_report, container, false)
        if (arguments == null || !arguments?.containsKey("reportId")!!) {
            Toast.makeText(context, "帖子不存在", Toast.LENGTH_SHORT).show()
            (context as MainActivity).onBackPressed()
            return root
        }
        val reportId = requireArguments()["reportId"]
        val report1: CheckBox = root.findViewById(R.id.checkBox)
        val report2: CheckBox = root.findViewById(R.id.checkBox2)
        val report3: CheckBox = root.findViewById(R.id.checkBox3)
        val report4: CheckBox = root.findViewById(R.id.checkBox4)
        val report5: CheckBox = root.findViewById(R.id.checkBox5)
        val report6: CheckBox = root.findViewById(R.id.checkBox6)
        val report7: CheckBox = root.findViewById(R.id.checkBox7)
        val report8: EditText = root.findViewById(R.id.checkBox8)
        val user = UserStatic(context as Context)
        val rest = report8.text.toString()
        var type: String = ""

        val reportButton: Button = root.findViewById(R.id.bt_report)
        reportButton.setOnClickListener {

            if (report1.isChecked)
                type=type+"1"
            else
                type=type+"0"

            if (report2.isChecked)
                type=type+"1"
            else
                type=type+"0"

            if (report3.isChecked())
                type=type+"1"
            else
                type=type+"0"

            if (report4.isChecked())
                type=type+"1"
            else
                type=type+"0"

            if (report5.isChecked())
                type=type+"1"
            else
                type=type+"0"

            if (report6.isChecked())
                type=type+"1"
            else
                type=type+"0"

            if (report7.isChecked())
                type=type+"1"
            else
                type=type+"0"

            if(user.isLogin()){
                val reqUrl =
                    HttpDataGet.baseUrl + HttpDataGet.URI_report + "?userId=${user.getLoginId()}&reportId=${reportId}&type=${type}&content=${URLEncoder.encode(rest)}&reportType=0"
                print(reqUrl)
                object : HttpDataGet() {
                    override fun call(data: Any, what: Int) {
                        Log.v("Error", this.response)
                        when (what) {
                            HttpDataGet.dataMap -> {
                                val dt = data as Map<String, Any>
                                if (dt.containsKey("state")) {
                                    val state = dt["state"] as Boolean
                                    if (state) {
                                        Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show()
                                        (context as MainActivity).onBackPressed()
                                    } else {
                                        if (dt.containsKey("message")) {
                                            val msg = dt["message"] as String
                                            Toast.makeText(
                                                context,
                                                "举报失败: 原因${msg}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else Toast.makeText(context, "举报失败", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                            HttpDataGet.dataArr ->{
                                //
                            }
                        }
                    }
                    override fun error(error: String) {
                        Toast.makeText(context, "举报失败: ${error}", Toast.LENGTH_SHORT).show()
                    }
                }.request(reqUrl)
            }else {
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
                    .show()
                (context as MainActivity).switchFragment(LoginFragment())
            }

        }
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

}