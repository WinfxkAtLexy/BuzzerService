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
* Created Date: 2025/1/3  08:44 */
package cn.winfxk.lexy.z1.service.dtu

import cn.winfxk.libk.log.Log
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DTUlinkAdapter : ChannelInboundHandlerAdapter() {
    private var ip = "";
    private lateinit var channel: Channel;
    private val key = UUID.randomUUID();

    init {
        log.i("初始化DTU监听处理器")
    }

    companion object {
        private val log = Log(DTUlinkAdapter::class.java.simpleName)
        private val map = ConcurrentHashMap<UUID, DTUlinkAdapter>();
        fun sendMessage(message: String) {
            for (adapter in map.values) try {
                adapter.sendMessage(message)
            } catch (e: Exception) {
                log.e("向DTU[${adapter.ip}]发送消息[$message]时出错", e)
            }
        }
    }

    fun sendMessage(message: String) {
        channel.writeAndFlush(message);
        channel.flush();
        log.i("发送消息[$message]到客户机DTU$ip")
    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg == null) {
            log.w("接收到DTU客户端($ip)空消息，忽略")
            return;
        }
        val message = msg.toString();
        log.i("接收到DTU客户端($ip)的消息[$message]")
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        if (ctx == null) return;
        this.channel = ctx.channel();
        val address = channel.remoteAddress() ?: return;
        val ip = (address as InetSocketAddress).address.hostAddress;
        log.i("一个DTU客户端($ip)进入网络组")
        this.ip = ip;
        map[key] = this;
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        if (ctx == null) return;
        val channel = ctx.channel();
        val address = channel.remoteAddress() ?: return;
        val ip = (address as InetSocketAddress).address.hostAddress;
        log.i("一个DTU客户端($ip)离开网络组")
        map.remove(key);
        channel.close();
    }
}