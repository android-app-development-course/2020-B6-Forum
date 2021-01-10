package com.hs.wdpt.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class HttpDataGet {
    companion object {
        const val dataArr = 888
        const val dataMap = 666
        const val dataError = 444
        const val baseUrl = "http://10.243.136.210:9090"
        //
        const val URI_Register = "/user/register"
        const val URI_Question_All = "/question/getAll"
        const val URI_Question_ByID = "/question/get"
        const val URI_Question_Search = "/question/keywordSearch"
        const val URI_Answer = "/user/answer"
        const val URI_Login ="/user/login"
        const val URI_findPassword = "/user/findPassword"
        const val URI_resetPassword = "/user/resetPassword"
        const val URI_update = "/user/update"
        const val URI_get = "/user/get"
        const val URI_ask="/user/ask"
        const val URI_report="/user/report"
        //
        fun httpRequest(url: String, retry: Int = 8, method: String = "GET"): String {
            var result = ""
            for (i in 1 until retry * 10) {
                try {
                    val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
                    if (method.toLowerCase(Locale.ROOT) == "post")
                        conn.doOutput = true
                    conn.doInput = true
                    conn.requestMethod = method
                    conn.connectTimeout = 200
                    conn.connect()
                    val inputStreamReader = InputStreamReader(conn.inputStream)
                    result += inputStreamReader.readText()
                    break
                } catch (e: Exception) {
                    //e.printStackTrace()
                    result = ""
                }
            }
            return result
        }

        private fun getMapFromJsonObject(jsonObject: JSONObject): Map<String, Any> {
            val result = HashMap<String, Any>()
            for (i in jsonObject.keys()) {
                when (val j = jsonObject[i]) {
                    is JSONArray -> result[i] = getJsonArray(j)
                    is JSONObject -> result[i] = getMapFromJsonObject(j)
                    else -> result[i] = j
                }
            }
            return result
        }

        private fun getJsonArray(jsonArray: JSONArray): ArrayList<Any> {
            val arr = ArrayList<Any>()
            for (i in 0 until jsonArray.length()) {
                when (val j = jsonArray[i]) {
                    is JSONArray -> arr.add(getJsonArray(j))
                    is JSONObject -> arr.add(getMapFromJsonObject(j))
                    else -> arr.add(j)
                }
            }
            return arr
        }
    }

    abstract fun call(data: Any, what: Int)
    abstract fun error(error: String)
    public var response: String = ""
    fun request(url: String, retry: Int = 8, method: String = "GET") {
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == dataArr||msg.what == dataMap) {
                    this@HttpDataGet.call(msg.obj, msg.what)
                }
                else if (msg.what == dataError) {
                    this@HttpDataGet.error(msg.obj as String)
                }
            }
        }
        val thread = object : Thread() {
            override fun run() {
                super.run()
                val msg = Message.obtain()
                val result = httpRequest(url, retry, method)
                response = result
                println(result)
                if (result != "") {
                    try {
                        when {
                            result.startsWith('[') -> {
                                msg.what = dataArr
                                msg.obj = getJsonArray(JSONArray(result))
                            }
                            result.startsWith('{') -> {
                                msg.what = dataMap
                                msg.obj = getMapFromJsonObject(JSONObject(result))
                            }
                            else -> throw Exception("Cannot analyze: $result")
                        }
                    } catch (e: Exception) {
                        msg.what = dataError
                        msg.obj = e.toString()
                    }
                } else {
                    msg.what = dataError
                    msg.obj = "Null Response!"
                }
                handler.sendMessage(msg)
            }
        }
        thread.isDaemon = true
        thread.start()
    }
}