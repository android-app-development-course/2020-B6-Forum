package com.hs.wdpt

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    var boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar //消除APP该Activity界面标题栏
        if (actionBar != null) { //消除APP该Activity界面标题栏
            actionBar.hide() //消除APP该Activity界面标题栏
        }
        setContentView(R.layout.splash) //这个是新建的empty Activity对应的xml部署文件名字
        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            boolean = true
            finish() //关闭当前活动
        }

        val myThread: Thread = object : Thread() {
            //创建子线程
            override fun run() {
                try {
                    val logo =
                        findViewById(R.id.imageView) as ImageView //图片

                    val animation = AnimationUtils.loadAnimation(this@Splash, R.anim.alpha)
                    logo.startAnimation(animation) //开始执行动画
                    sleep(4000) //使程序休眠五秒
                    if(boolean)return
                    val it = Intent(applicationContext, MainActivity::class.java) //启动MainActivity
                    startActivity(it)
                    finish() //关闭当前活动
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        myThread.start() //启动线程

    }

}