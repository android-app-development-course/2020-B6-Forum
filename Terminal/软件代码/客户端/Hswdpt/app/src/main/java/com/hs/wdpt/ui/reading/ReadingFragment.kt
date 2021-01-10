package com.hs.wdpt.ui.reading

import android.content.Context
import android.graphics.Color
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import com.ldoublem.thumbUplib.ThumbUpView
import com.ldoublem.thumbUplib.ThumbUpView.OnThumbUp
import java.net.URLEncoder

class ReadingFragment : Fragment() {
    companion object {
        fun newInstance() = ReadingFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reading, container, false)
        rootView = root
        if(arguments!=null){
            if(requireArguments().containsKey("userId")){
                questionId = requireArguments()["userId"] as String
                initView()
            }
        }
        return root
    }
    private lateinit var questionId: String
    private lateinit var rootView : View
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentContent: EditText
    private lateinit var commentSubmit: Button
    private var itemList = ArrayList<Any>()
    private val adapter = ReadingAdapter(itemList)
    private fun initView(){
        refreshLayout = rootView.findViewById(R.id.reading_freshlayout)
        recyclerView = rootView.findViewById(R.id.reading_recycleView)
        commentContent = rootView.findViewById(R.id.comment_content)
        commentSubmit = rootView.findViewById(R.id.comment_submit)
        //
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            refresh()
        }
        refresh()
        commentSubmit.setOnClickListener {
            val user = UserStatic(context as Context)
            if(!user.isLogin()){
                (context as MainActivity).switchFragment(LoginFragment())
                return@setOnClickListener
            }
            val userId = user.getLoginId()
            val content = commentContent.text.toString()
            if(content == ""){
                Toast.makeText(context, "回复不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val reqUrl =
                HttpDataGet.baseUrl + HttpDataGet.URI_Answer + "?userId=${userId}&questionId=${questionId}&content=${URLEncoder.encode(content)}"
            object : HttpDataGet() {
                override fun call(data: Any, what: Int) {
                    Log.v("Response", this.response)
                    when (what) {
                        HttpDataGet.dataMap -> {
                            val dt = data as Map<String, Any>
                            if (dt.containsKey("state")) {
                                val state = dt["state"] as Boolean
                                if (state) {
                                    refresh()
                                } else {
                                    var msg: String = ""
                                    if (dt.containsKey("message")) {
                                        msg = "reason:" + dt["message"] as String
                                    }
                                    Toast.makeText(context, "回复失败$msg", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
                override fun error(error: String) {
                    Toast.makeText(context, "回复失败", Toast.LENGTH_SHORT).show()
                }
            }.request(reqUrl)
        }
    }
    private fun refresh() {
        refreshLayout.isRefreshing = true
        val reqUrl =
            HttpDataGet.baseUrl + HttpDataGet.URI_Question_ByID + "?id=${questionId}"
        object : HttpDataGet() {
            override fun call(data: Any, what: Int) {
                Log.v("Question", this.response)
                when (what) {
                    HttpDataGet.dataMap -> {
                        val dt = data as Map<String, Any>
                        itemList.clear()
                        itemList.add(
                            0,
                            ReadingItem(
                                dt["id"].toString(),
                                dt["title"].toString(),
                                dt["content"].toString(),
                                dt["thumpsUpNumber"].toString(),
                                (dt["publishTime"] as String).split(".")[0].replace(
                                    "T",
                                    " "
                                ),
                                dt["reported"] as Boolean
                            )
                        )
                        for(comment in (dt["answers"] as ArrayList<Any>)){
                            println(comment)
                            val cd = comment as Map<String, Any>
                            val reqUrl =
                                HttpDataGet.baseUrl + HttpDataGet.URI_get + "?id=${cd["userId"].toString()}"
                            object : HttpDataGet() {
                                override fun call(data: Any, what: Int) {
                                    Log.v("Reading", this.response)
                                    when (what) {
                                        HttpDataGet.dataMap -> {
                                            val ddd = data as Map<String, Any>
                                            itemList.add(
                                                1,
                                                CommentItem(
                                                    cd["id"].toString(),
                                                    ddd["nickName"].toString(),
                                                    cd["content"].toString(),
                                                    cd["publishTime"].toString().split(".")[0].replace(
                                                        "T",
                                                        " "
                                                    ),
                                                    cd["thumpsUpNumber"].toString(),
                                                    cd["reported"] as Boolean
                                                )
                                            )
                                            /*itemList.sortByDescending {
                                                it ->
                                                if(it is ReadingItem) it.time
                                                else (it as CommentItem).time
                                            }*/
                                            /*itemList.sortByDescending {
                                                    it->if(it is CommentItem) it.time
                                            else (it as ReadingItem).time
                                            }*/
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                                override fun error(error: String) {
                                }
                            }.request(reqUrl)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun error(error: String) {
                Toast.makeText(context, "问题加载失败", Toast.LENGTH_SHORT).show()
            }
        }.request(reqUrl)
        refreshLayout.isRefreshing = false
    }
}