package com.hs.wdpt.ui.home

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.reading.ReadingFragment
import com.hs.wdpt.ui.report.ReportFragment
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class HomePageItemRecycleAdapter(val homePageList: List<HomePageItem>)
    : RecyclerView.Adapter<HomePageItemRecycleAdapter.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.home_item_title)
        val content: TextView = view.findViewById(R.id.home_item_content)
        val reportButton: Button = view.findViewById(R.id.home_item_report)
        val imgView: ImageView = view.findViewById(R.id.item_home_img)
        val timeView: TextView = view.findViewById(R.id.item_home_time)
        val askUser: TextView = view.findViewById(R.id.home_item_askUser)
        lateinit var itemMsg: HomePageItem
        var rootView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        val viewHolder = ViewHolder(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homeItem = homePageList[position]
        holder.title.text = homeItem.title
        holder.content.text = homeItem.content
        holder.timeView.text = homeItem.time
        holder.itemMsg = homeItem

        if(homeItem.askUser!="")holder.askUser.text = "提问者:"+homeItem.askUser
        val listener = View.OnClickListener {
            val activity = holder.rootView.context
            if(activity is Activity){
                val mainActivity = activity as MainActivity
                val fragment = ReadingFragment()
                val args = Bundle()
                args.putString("userId", homeItem.id)
                fragment.arguments = args
                mainActivity.switchFragment(fragment)
            }
        }
        holder.title.setOnClickListener(listener)
        holder.content.setOnClickListener(listener)
        holder.reportButton.setOnClickListener {
            val activity = holder.rootView.context
            if(activity is Activity){
                val mainActivity = activity as MainActivity
                val fragment = ReportFragment()
                val args = Bundle()
                fragment.arguments = args
                args.putString("reportId", homeItem.id)
                mainActivity.switchFragment(fragment)
            }
        }

        if(homeItem.imgUrl!=""){
            val handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        holder.itemMsg.imgUrl.hashCode() -> holder.imgView.setImageBitmap(msg.obj as Bitmap)
                    }
                    super.handleMessage(msg)
                }
            }
            object : Thread() {
                override fun run() {
                    super.run()
                    val conn: HttpURLConnection = URL(homeItem.imgUrl).openConnection() as HttpURLConnection?: return
                    conn.doInput = true
                    conn.requestMethod = "GET"
                    conn.connect()
                    val ins = conn.inputStream
                    val bitmap = BitmapFactory.decodeStream(ins)
                    if(bitmap!=null){
                        val msg = Message.obtain()
                        msg.what = holder.itemMsg.imgUrl.hashCode()
                        msg.obj = bitmap
                        ins.close()
                        handler.sendMessage(msg)
                    }
                }
            }.start()
        }
    }

    override fun getItemCount(): Int = homePageList.size
}