package com.hs.wdpt.util

import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

abstract class UpLoadImg {
    abstract fun onUpLoaded(result: String)
    private val nextLine = "\r\n"
    private val twoHyphens = "--"
    private val boundary = java.util.UUID.randomUUID().toString()
    fun upLoadFile(url: String, filePath: String, upLoadName: String) {
        object : Thread() {
            override fun run() {
                super.run()
                val file = File(filePath)
                var connection: HttpURLConnection? = null
                var result = ""
                try {
                    val url1 = URL(url)
                    connection = url1.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.doOutput = true
                    connection.doOutput = true
                    connection.setRequestProperty(
                        "Content-Type",
                        "multipart/form-data;boundary=$boundary"
                    )
                    val outputStream: OutputStream = DataOutputStream(connection.outputStream)
                    var header = twoHyphens + boundary + nextLine
                    //分隔符参数设置
                    header += "Content-Disposition: form-data;name=\"file\";filename=\"$upLoadName\"$nextLine$nextLine"
                    //写入输出流
                    outputStream.write(header.toByteArray())
                    //读取文件并写入
                    val inputStream = FileInputStream(file)
                    val bytes = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(bytes).also { length = it } != -1) {
                        outputStream.write(bytes, 0, length)
                    }
                    //文件写入完成后加回车
                    outputStream.write(nextLine.toByteArray())
                    //写入结束分隔符
                    val footer =
                        nextLine + twoHyphens + boundary + twoHyphens + nextLine
                    outputStream.write(footer.toByteArray())
                    outputStream.flush()
                    val inputStream1: InputStream = connection.inputStream
                    result += InputStreamReader(inputStream1).readText()
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                onUpLoaded(result)
            }
        }.start()
    }
}

fun main(){
    object : UpLoadImg(){
        override fun onUpLoaded(result: String) {
            println(result)
        }
    }.upLoadFile(HttpDataGet.baseUrl + "/picUpload", "E:\\Media\\Pictures\\Pictures\\emm.png", "1342143")
    Thread.sleep(100000)
}