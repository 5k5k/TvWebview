package com.morladim.tvwebview

import android.app.Service
import android.text.TextUtils

import android.content.Intent

import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import java.io.*
import java.net.ServerSocket
import java.net.Socket


/**
 *
 * @Author 5k5k
 * @Date 2022/12/25
 */

class SocketServerService : Service() {
    private var isServiceDestroyed = false
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Thread(TcpServer()).start()
    }

    private inner class TcpServer : Runnable {
        override fun run() {
            var serverSocket: ServerSocket? = null
            try {
                serverSocket = ServerSocket(7788)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
            while (!isServiceDestroyed) {
                try {
                    if (serverSocket == null) {
                        return
                    }
                    Log.i(TAG, "ServerSocket loop listen ClientSocket")
                    //接收客户端的请求，并且阻塞直到接收到消息
                    val client: Socket = serverSocket.accept()
                    Thread {
                        try {
                            responseClient(client)
                        } catch (exception: IOException) {
                            exception.printStackTrace()
                        }
                    }.start()
                } catch (exception: IOException) {
                    exception.printStackTrace()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun responseClient(client: Socket) {
        // 用于接收客户端的消息
        val `in` = BufferedReader(InputStreamReader(client.getInputStream()))
        // 用于向客户端发送消息
        val out = PrintWriter(
            BufferedWriter(
                OutputStreamWriter(
                    client.getOutputStream()
                )
            ), true
        )
        out.println("你好，我是服务端")
        while (!isServiceDestroyed) {
            val str: String = `in`.readLine()
            Log.i(TAG, "收到客户端发来的消息$str")
            if (TextUtils.isEmpty(str)) {
                //客户端断开了连接
                Log.i(TAG, "客户端断开了连接")
                break
            }
            val message = "收到了客户端的消息为：$str"
            // 从客户端收到的消息加工再发送给客户端
            out.println(message)
        }
        out.close()
        `in`.close()
        client.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceDestroyed = true
    }

    companion object {
        private const val TAG = "SocketServerService"
    }
}