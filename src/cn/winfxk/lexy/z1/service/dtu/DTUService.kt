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
* Created Date: 2025/1/3  08:41 */
package cn.winfxk.lexy.z1.service.dtu

import cn.winfxk.lexy.z1.Deploy.Companion.deploy
import cn.winfxk.lexy.z1.close
import cn.winfxk.libk.log.Log
import cn.winfxk.tool.view.JOptionPane
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder

class DTUService : ChannelInitializer<Channel>(), Runnable {
    private lateinit var group: NioEventLoopGroup;
    private lateinit var worker: NioEventLoopGroup;
    private val log = Log(this.javaClass.simpleName)

    companion object {
        private lateinit var main: DTUService;
        fun getMain() = main;
    }

    init {
        log.i("DTU监听服务初始化中...")
        main = this;
    }


    override fun initChannel(ch: Channel?) {
        if (ch == null) {
            log.e("触发了一个异常请求！Channel为空！")
            return
        }
        val pipeline = ch.pipeline();
        pipeline.addLast(StringDecoder());
        pipeline.addLast(StringEncoder());
        pipeline.addLast(DTUlinkAdapter());
    }

    override fun run() {
        try {
            val port = deploy.getDTUPort();
            log.i("准备在端口${port}上创建DTU监听服务！")
            group = NioEventLoopGroup();
            worker = NioEventLoopGroup();
            val serverBootstrap = ServerBootstrap();
            serverBootstrap.group(group, worker).channel(NioServerSocketChannel::class.java).childHandler(this)
            val future = serverBootstrap.bind(port).sync();
            log.i("DTU监听服务已启动！")
            DTUSwitchService().start();
            future.channel().closeFuture().sync();
            log.w("DTU监听服务终止！")
        } catch (e: Exception) {
            log.e("DTU监听服务启动失败！", e)
            group.shutdownGracefully();
            worker.shutdownGracefully();
            JOptionPane.showMessageDialog(null, "DTU监听服务无法正常运行！请检查端口是否被占用或程序运行状态，若无法确认问题，请提交日志报告！")
            close(- 1)
        }
    }
}