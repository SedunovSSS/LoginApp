package com.sedunovsss.fragmenttest
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PostRequestSender (urlAddress: String, post: String) {
    private var result = ""
    private val url = URL(urlAddress)
    private val conn = url.openConnection() as HttpURLConnection
    private val postData = post
    fun send(): String{
        try{
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postData.length.toString())
            conn.useCaches = false
            DataOutputStream(conn.outputStream).use { it.writeBytes(postData) }
            BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    result += line
                }
            }
            return result
        }
        catch (e: java.net.NoRouteToHostException){
            return "No Route To Host"
        }
        catch (e: java.net.UnknownHostException){
            return "Unknown Host"
        }
        catch (e: java.net.ConnectException){
            return "Connect error"
        }
        catch (e: Exception){
            return e.toString()
        }
    }
}