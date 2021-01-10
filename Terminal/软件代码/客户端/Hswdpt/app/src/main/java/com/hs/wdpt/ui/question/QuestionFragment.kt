package com.hs.wdpt.ui.question

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hs.wdpt.MainActivity
import com.hs.wdpt.R
import com.hs.wdpt.ui.home.HomeFragment
import com.hs.wdpt.ui.login.LoginFragment
import com.hs.wdpt.util.HttpDataGet
import com.hs.wdpt.util.UserStatic
import kotlinx.android.synthetic.main.fragment_question.*
import java.net.URLDecoder
import java.net.URLEncoder

class QuestionFragment : Fragment() {
    val fromAlbum = 2
    companion object {
        fun newInstance() = QuestionFragment()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_question, container, false)
        rootView = root

        addPic = root.findViewById(R.id.fab_question_add)
        questionOk = root.findViewById(R.id.fab_question_ok)
        initView()
        return root
    }

    private lateinit var rootView: View
    private lateinit var addPic: FloatingActionButton
    private lateinit var questionOk: FloatingActionButton

    private fun initView(){
        addPic.setOnClickListener{
            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定只显示图片
            intent.type="image/*"
            startActivityForResult(intent,fromAlbum)
        }

        questionOk.setOnClickListener{
            val user = UserStatic(context as Context)
            val title = rootView.findViewById<EditText>(R.id.title).text.toString()
            val content=rootView.findViewById<EditText>(R.id.text).text.toString()
            when {
                title == "" -> {
                    Toast.makeText(context, "请输入标题", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            if(user.isLogin()){
                val reqUrl =
                    HttpDataGet.baseUrl + HttpDataGet.URI_ask + "?userId=${user.getLoginId()}&title=${URLEncoder.encode(title)}&content=${URLEncoder.encode(content)}"
                object : HttpDataGet() {
                    override fun call(data: Any, what: Int) {
                        Log.v("Error", this.response)
                        when (what) {
                            HttpDataGet.dataMap -> {
                                val dt = data as Map<String, Any>
                                if (dt.containsKey("state")) {
                                    val state = dt["state"] as Boolean
                                    if (state) {
                                        Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show()
                                        (context as MainActivity).clickMenu(R.id.navigation_home)
                                        val titleEdit: EditText = rootView.findViewById(R.id.title)
                                        val contentEdit: EditText = rootView.findViewById(R.id.text)
                                        titleEdit.setText("")
                                        contentEdit.setText("")
                                    } else {
                                        if (dt.containsKey("message")) {
                                            val msg = dt["message"] as String
                                            Toast.makeText(
                                                context,
                                                "发布失败: 原因${msg}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT)
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
                        Toast.makeText(context, "发布失败: ${error}", Toast.LENGTH_SHORT).show()
                    }
                }.request(reqUrl)
            }else {
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
                    .show()
                (context as MainActivity).switchFragment(LoginFragment())
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            fromAlbum ->{
                if (resultCode== Activity.RESULT_OK && data !=null){
                    data.data?.let {
                            uri ->
                        val bitmap = getBitmapFromUri(uri)
                        image_choice.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    private fun getBitmapFromUri(uri: Uri)=activity?.contentResolver?.openFileDescriptor(uri,"r")?.use{
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

}






/*
class QuestionFragment : Fragment() {
    val fromAlbum = 2

    companion object {
        fun newInstance() = QuestionFragment()
    }

    private lateinit var viewModel: QuestionViewModel

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_question, container, false)
        val fromAlbumBtn: Button = root.findViewById(R.id.fromAlbumBtn)
        val release = root.findViewById<Button>(R.id.release)
        fromAlbumBtn.setOnClickListener{
            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定只显示图片
            intent.type="image/*"
            startActivityForResult(intent,fromAlbum)
        }

        release.setOnClickListener{
            val user = UserStatic(context as Context)
            val title = root.findViewById<EditText>(R.id.title).text.toString()
            val content=root.findViewById<EditText>(R.id.text).text.toString()
            when {
                title == "" -> {
                    Toast.makeText(context, "请输入标题", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                content == "" -> {
                    Toast.makeText(context, "请输入标题", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            if(user.isLogin()){
                val reqUrl =
                    HttpDataGet.baseUrl + HttpDataGet.URI_Register + "?userid=${user.getLoginId()}&title=${title}&content=${content}"
            }else Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT)
                .show()
        }
        return root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            fromAlbum ->{
                if (resultCode== Activity.RESULT_OK && data !=null){
                    data.data?.let {
                            uri ->
                        val bitmap = getBitmapFromUri(uri)
                        image_choice.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri)=activity?.contentResolver?.openFileDescriptor(uri,"r")?.use{
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
*/