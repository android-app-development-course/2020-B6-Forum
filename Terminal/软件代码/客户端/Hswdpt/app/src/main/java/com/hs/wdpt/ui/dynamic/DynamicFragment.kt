package com.hs.wdpt.ui.dynamic

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.home.HomePageItem
import com.hs.wdpt.ui.home.HomePageItemRecycleAdapter
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import java.lang.Exception

class DynamicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dynamic, container, false)
        rootView = root
        recyclerView =  root.findViewById(R.id.dynamic_recycleView)
        refreshLayout = root.findViewById(R.id.dynamic_freshLayout)
        initView()
        return root
    }

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private val homePageItemList = ArrayList<HomePageItem>()
    private val adapter = HomePageItemRecycleAdapter(homePageItemList)
    fun initView() {
        ////view
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        //刷新首页
        refreshLayout.setOnRefreshListener {
            refresh()
        }
        refresh()
    }
    private fun refresh() {
        homePageItemList.clear()
        val user = UserStatic(context as Context)
        if(!user.isLogin()){
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        val reqUrl = HttpDataGet.baseUrl + HttpDataGet.URI_get + "?id=${user.getLoginId()}"
        object : HttpDataGet() {
            override fun call(data: Any, what: Int) {
                Log.v("Error", this.response)
                when (what) {
                    HttpDataGet.dataMap -> {
                        val dt = data as Map<String, Any>
                        try {
                            for (question in (dt["questions"] as ArrayList<Any>)) {
                                question as Map<String, Any>
                                homePageItemList.add(
                                    0,
                                    HomePageItem(
                                        question["title"] as String,
                                        "问题描述:" + question["content"] as String,
                                        question["id"] as String,
                                        (question["publishTime"] as String).split(".")[0].replace(
                                            "T",
                                            " "
                                        ),
                                        "我"
                                    )
                                )
                            }
                            adapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            Log.v("HomeList", e.toString())
                        }
                    }
                }
                refreshLayout.isRefreshing = false
            }
            override fun error(error: String) {
                Toast.makeText(context, "刷新失败", Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false
            }
        }.request(reqUrl)
    }
}