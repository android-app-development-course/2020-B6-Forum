package com.hs.wdpt.ui.home

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.ui.report.ReportFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import java.lang.Exception
import java.net.URLEncoder

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        rootView = root
        initView()
        return root
    }

    private lateinit var adapter: HomePageItemRecycleAdapter
    private lateinit var rootView: View
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var imageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var msgView: ImageView
    private fun initView() {
        adapter = HomePageItemRecycleAdapter(homeViewModel.homePageItemList)
        ////view
        refreshLayout = rootView.findViewById(R.id.home_freshlayout)
        recyclerView = rootView.findViewById(R.id.home_recycleView)
        searchView = rootView.findViewById(R.id.home_search)
        msgView = rootView.findViewById(R.id.message_image)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        //工具栏
        imageView = rootView.findViewById(R.id.login_image)
        imageView.setOnClickListener {
            if(UserStatic(context as Context).isLogin())
                (context as MainActivity).clickMenu(R.id.navigation_user)
            else (context as MainActivity).switchFragment(LoginFragment())
        }
        //刷新首页
        refreshLayout.setOnRefreshListener {
            refresh()
        }
        refresh()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.homePageItemList.clear()
                adapter.notifyDataSetChanged()
                refresh(query.toString())
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        msgView.setOnClickListener {
            (context as MainActivity).clickMenu(R.id.navigation_dynamic)
        }
    }
    private fun refresh(keyWord: String = "") {
        homeViewModel.homePageItemList.clear()
        var reqUrl =
            HttpDataGet.baseUrl + HttpDataGet.URI_Question_All
        if(keyWord!=""){
            reqUrl = HttpDataGet.baseUrl + HttpDataGet.URI_Question_Search + "?keyword=${URLEncoder.encode(keyWord)}"
        }
        object : HttpDataGet() {
            override fun call(data: Any, what: Int) {
                Log.v("Error", this.response)
                when (what) {
                    HttpDataGet.dataArr -> {
                        val dt = data as ArrayList<Any>
                        try {
                            for (question in dt) {
                                question as Map<String, Any>
                                val reqUrl =
                                    HttpDataGet.baseUrl + HttpDataGet.URI_get + "?id=${question["userId"].toString()}"
                                object : HttpDataGet() {
                                    override fun call(data: Any, what: Int) {
                                        Log.v("Reading", this.response)
                                        when (what) {
                                            HttpDataGet.dataMap -> {
                                                val ddd = data as Map<String, Any>
                                                homeViewModel.homePageItemList.add(
                                                    0,
                                                    HomePageItem(
                                                        question["title"] as String,
                                                        "问题描述:" + question["content"] as String,
                                                        question["id"] as String,
                                                        (question["publishTime"] as String).split(".")[0].replace(
                                                            "T",
                                                            " "
                                                        ),
                                                        ddd["nickName"].toString()
                                                    )
                                                )
                                                homeViewModel.homePageItemList.sortByDescending {
                                                    it -> it.time
                                                }
                                                adapter.notifyDataSetChanged()
                                            }
                                        }
                                    }
                                    override fun error(error: String) {
                                    }
                                }.request(reqUrl)
                            adapter.notifyDataSetChanged()
                            }
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