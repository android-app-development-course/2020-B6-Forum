package com.hs.wdpt

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.hs.wdpt.ui.dynamic.DynamicFragment
import com.hs.wdpt.ui.home.HomeFragment
import com.hs.wdpt.ui.question.QuestionFragment
import com.hs.wdpt.ui.user.UserFragment
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private var fragmentStack = Stack<Fragment>()
    private var fragmentMap = HashMap<Int, Fragment>()
    private val questionFragment = QuestionFragment()
    private val homeFragment = HomeFragment()
    private val dynamicFragment = DynamicFragment()
    private val userFragment = UserFragment()
    private lateinit var navView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null;
        initFragment()
        navView.setOnNavigationItemSelectedListener {
            switchFragment(it.itemId)
        }
        test();
    }

    private fun test() {
        val requestCode = 1
        val permissions = arrayOf(
            Manifest.permission.INTERNET
        )
        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                permissions,
                requestCode
            )
        }

    }

    private fun initFragment() {
        fragmentMap[R.id.navigation_home] = homeFragment
        fragmentMap[R.id.navigation_question] = questionFragment
        fragmentMap[R.id.navigation_dynamic] = dynamicFragment
        fragmentMap[R.id.navigation_user] = userFragment
        fragmentStack.push(homeFragment)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment)
            .show(homeFragment).commit()
    }

    private fun switchFragment(fragmentId: Int): Boolean {
        if (!fragmentMap.keys.contains(fragmentId)) {
            return false
        }
        return switchFragment(fragmentMap[fragmentId] as Fragment)
    }

    fun clickMenu(itemId: Int){
        if (fragmentMap.keys.contains(itemId))
            navView.selectedItemId = itemId
    }

    fun switchFragment(fragment: Fragment): Boolean {
        if(fragmentStack.empty()){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .show(fragment).commit()
            return true
        }
        val lastFragment = fragmentStack.last()
        fragmentStack.push(fragment)
        val transaction = supportFragmentManager.beginTransaction();
        transaction.hide(lastFragment);//隐藏上个Fragment
        if (!fragment.isAdded) {
            transaction.add(R.id.fragment_container, fragment);
        }
        transaction.show(fragment).commitAllowingStateLoss();
        return true
    }

    private fun returnFragment(): Boolean{
        if(fragmentStack.size > 1){
            when(fragmentStack.last()){
                is UserFragment -> return false
                is DynamicFragment -> return false
                is HomeFragment -> return false
                is QuestionFragment -> return false
            }
            val lastFragment = fragmentStack.pop()
            val showFragment = fragmentStack.last()
            val transaction = supportFragmentManager.beginTransaction();
            transaction.hide(lastFragment);//隐藏上个Fragment
            lastFragment.onDestroy()
            if (showFragment.isAdded == false) {
                transaction.add(R.id.fragment_container, showFragment);
            }
            transaction.show(showFragment).commitAllowingStateLoss();
            return true
        }else return false
    }

    override fun onBackPressed() {
        if(!returnFragment())super.onBackPressed()
    }
}