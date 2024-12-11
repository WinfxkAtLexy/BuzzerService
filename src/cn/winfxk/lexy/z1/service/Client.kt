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
* Created Date: 2024/11/21  09:42 */
package cn.winfxk.lexy.z1.service

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.message.Message
import cn.winfxk.lexy.z1.message.MessageType
import cn.winfxk.lexy.z1.message.OnMessageResponse
import cn.winfxk.lexy.z1.service.rec.CallMessage
import cn.winfxk.lexy.z1.service.rec.EmptyMessage
import cn.winfxk.libk.config.Config
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.tab.Tabable
import com.alibaba.fastjson2.JSONObject
import io.netty.channel.Channel
import java.io.File
import java.net.InetSocketAddress
import java.text.SimpleDateFormat
import java.util.*

class Client(val handler: MyBusinessHandler, val channel: Channel) : Tabable {
    val address = (channel.remoteAddress() as InetSocketAddress)
    val IP = address.address.hostName;
    lateinit var id: String;
    lateinit var name: String;
    var isInitialized = false;
    lateinit var config: Config;
    var type = "";

    companion object {
        const val allType = "all";
        private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 接收到消息
     */
    fun receiveMessages(messageID: String, json: JSONObject): Message? {
        val type = (json["type"] ?: "").toString();
        return when (type.lowercase()) {
            CallMessage.type.lowercase() -> CallMessage(type, json, this)
            else                         -> EmptyMessage(type, json, this)
        }.respond()
    }

    fun sendMessage(message: Message, response: OnMessageResponse? = null) {
        val msg = message.toString();
        if (message.getType().equals(MessageType.Request) && response != null) {
            Log.i(tag, "正在发送消息并等待回执[${msg.length}]..")
            MyBusinessHandler.addResponse(message.getID(), response);
        } else if (message.getType().equals(MessageType.Response))
            Log.i(tag, "正在回复消息[${msg.length}]..")
        else Log.i(tag, "正在发送消息[${msg.length}]..")
        channel.writeAndFlush(msg)
    }

    override fun getTAG(): String {
        return if (isInitialized) "$name($id)" else IP;
    }

    fun close() {
        Log.i(tag, "正在关闭链接[${if (isInitialized) "$name-" else ""}$IP:${address.port}]..")
        config.set("上次在线时间", format.format(Date()));
        config.save();
    }
    /**
     * 刷新客户端信息
     */
    fun reload() {
        if (! isInitialized) return;
        config = Config(File(Deploy.deploy.clients, "$id.json"));
        val ips = config.getMap("IPs", HashMap()).let {
            if (it.isNullOrEmpty()) return@let HashMap<String, Any?>();
            HashMap<String, Any?>().also { map -> map.putAll(it) }
        }
        ips[IP] = format.format(Date());
        config.set("name", name);
        config.set("请求次数", config.getInt("请求次数", 0) + 1);
        if (! config.file.exists() || ! config.file.isFile) config.save();
    }
}