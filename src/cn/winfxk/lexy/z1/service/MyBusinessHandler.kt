/* 
* Copyright Notice
* © [2024] Winfxk. All rights reserved.
* The software, its source code, and all related documentation are the intellectual property of Winfxk. Any reproduction or distribution of this software or any part thereof must be clearly attributed to Winfxk and the original author. Unauthorized copying, reproduction, or distribution without proper attribution is strictly prohibited.
* For inquiries, support, or to request permission for use, please contact us at:
* Email: admin@winfxk.cn
* QQ: 2508543202
* Visit our homepage for more information: http://Winfxk.cn
* 
* --------- Create message ---------
* Created by IntelliJ ID
* Author： Winfxk
* Created PCUser: kc4064 
* Web: http://winfxk.com
* Created Date: 2024/11/20  16:06 */
package cn.winfxk.lexy.z1.service

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.message.MessageType
import cn.winfxk.lexy.z1.message.OnMessageResponse
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.utils.objToString
import cn.winfxk.libk.tool.utils.toJson
import com.alibaba.fastjson2.JSONObject
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class MyBusinessHandler : ChannelInboundHandlerAdapter() {
    init {
        log.i("初始化监听处理器..")
    }

    companion object {
        private val log = Log(MyBusinessHandler::class.java.simpleName)
        private val clients = ConcurrentHashMap<String, Client>();
        private val request = ConcurrentHashMap<String, OnMessageResponse>();
        fun getClients() = clients;
        fun addResponse(ID: String, response: OnMessageResponse) {
            request[ID] = response;
        }
        /**
         * 本次启动后接收到的请求数
         */
        @Volatile
        var requestCount = 0;
        /**
         * 当前服务器的最大请求数
         */
        @Volatile
        var maxRequestCount = Deploy.deploy.config.getLong("总请求数", 0);
        /**
         * 最大报警数
         */
        @Volatile
        var maxAlarmsCount = Deploy.deploy.config.getLong("最大报警数", 0);
        /**
         * 最大客户端数
         */
        fun getMaxClientCount(): Int = Deploy.deploy.clients.listFiles { a, b -> File(a, b).isFile }?.size ?: 0;
        /**
         * 当前已连接的客户端数量
         */
        fun getClientCount() = clients.size;
    }

    /**
     * 接收到消息
     */
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val message = msg.objToString();
        if (message.isNullOrBlank()) {
            log.i("接收到空消息，忽略")
            return;
        }
        val channel = ctx.channel();
        val id = channel.id().toString();
        val client = clients.getOrElse(id) {
            log.i("客户端连接异常!正在构建..")
            Client(this, channel)
        }
        try {
            val root = message.toJson();
            val clientID = root["ClientID"].objToString();
            val clientName = root["ClientName"].objToString();
            clientName?.also { client.name = it; }
            clientID?.also { client.id = it }
            if (! clientName.isNullOrBlank() && ! clientID.isNullOrBlank()) client.isInitialized = true;
            val type = root["type"].objToString();
            val messageID = root["ID"].objToString();
            if (type.isNullOrBlank() || messageID.isNullOrBlank()) {
                log.e("请求异常！跳过请求操作.$root");
                return;
            }
            if (type.equals(MessageType.Live.name, ignoreCase = true)) return;
            log.i("已接收到消息[${message.length}]")
            val json = root["json"];
            if (json == null || json !is JSONObject) {
                log.e("请求异常！未知的请求体。跳过请求操作.$root")
                return;
            }
            if (type == MessageType.Response.name) {
                log.i("正在执行回执[$messageID].");
                val response = request.remove(messageID);
                if (response == null) {
                    log.i("回执处理器[$messageID]未找到。")
                    return;
                }
                response.onResponse(json);
                return;
            }
            if (! type.equals(MessageType.Request.name)) {
                log.i("跳过一个非正常请求[$type]")
                return;
            }
            log.i("等待消息处理")
            requestCount += 1;
            maxRequestCount += 1;
            client.reload();
            client.receiveMessages(messageID, json)?.also {
                it.setID(messageID)
                it.setType(MessageType.Response)
                client.sendMessage(it);
            }
        } catch (e: Exception) {
            log.e("处理消息时出现异常${channel.remoteAddress()}${if (client.isInitialized) "[${client.name}]" else ""}", e)
        }
    }
    /**
     * 当链接活跃
     */
    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
        if (ctx == null) return;
        val channel = ctx.channel();
        val client = Client(this, channel)
        val id = channel.id().toString();
        if (clients.containsKey(id)) clients.remove(id)?.close();
        clients[id] = client;
        log.i("一个新的客户端接入：${client.IP}")
    }
    /**
     * 当链接不在活跃
     */
    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        if (ctx == null) return;
        val channel = ctx.channel();
        val id = channel.id().toString();
        val client = clients[id];
        log.i("一个客户端断开：${ctx.channel().remoteAddress()}${client?.let { if (it.isInitialized) "[${it.name}]" else "" } ?: ""}")
        clients.remove(id);
    }
}