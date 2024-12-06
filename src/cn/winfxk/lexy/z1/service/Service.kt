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
* Created Date: 2024/11/20  15:58 */
package cn.winfxk.lexy.z1.service

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.close
import cn.winfxk.libk.log.Log
import cn.winfxk.tool.view.JOptionPane
import cn.winfxk.tool.view.StartView
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder

class Service : ChannelInitializer<Channel>(), StartView {
    private val log = Log("NettyService");
    private lateinit var group: NioEventLoopGroup;
    private lateinit var worker: NioEventLoopGroup;
    private val deploy by lazy { Deploy.deploy }
    @Volatile
    private var isRunning: Boolean = false;

    init {
        log.i("正在初始化监听服务..")
        main = this;
    }

    companion object {
        private lateinit var main: Service;
        fun getMain() = main;
    }

    fun isRunning() = isRunning;
    override fun initChannel(ch: Channel?) {
        if (ch == null) {
            log.e("触发了一个异常请求！Channel为空！")
            return
        }
        val pipeline = ch.pipeline();
        pipeline.addLast(StringDecoder());
        pipeline.addLast(StringEncoder());
        pipeline.addLast(MyBusinessHandler());
        pipeline.addLast(SocketException());
    }

    override fun start() {
        Thread {
            try {
                log.i("正在创建端口监听服务：${deploy.getPort()}")
                group = NioEventLoopGroup();
                worker = NioEventLoopGroup();
                val serverBootstrap = ServerBootstrap();
                serverBootstrap.group(group, worker).channel(NioServerSocketChannel::class.java).childHandler(this)
                val future = serverBootstrap.bind(deploy.getPort()).sync();
                log.i("监听服务已启动！")
                isRunning = true;
                future.channel().closeFuture().sync();
                log.w("监听服务终止！")
                if (isRunning) {
                    isRunning = false;
                    log.e("端口监听服务异常终止(${deploy.getPort()})！请检查端口是否被占用或程序运行是否正常！")
                    JOptionPane.showMessageDialog(null, "监听服务终止或无法运行！请检查端口是否被占用或程序运行状态，若无法确认问题，请提交日志报告！")
                    close(- 1)
                }
            } catch (e: Exception) {
                log.e("端口监听服务出现异常！", e);
                isRunning = false;
                JOptionPane.showMessageDialog(null, "监听服务无法正常运行！请检查端口是否被占用或程序运行状态，若无法确认问题，请提交日志报告！")
                close(- 1)
            }
        }.start();
    }


    fun shutdown() {
        log.i("正在关闭监听服务..")
        Deploy.deploy.config.set("总请求数", MyBusinessHandler.maxRequestCount);
        Deploy.deploy.config.set("最大报警数", MyBusinessHandler.maxAlarmsCount)
        isRunning = false;
        group.shutdownGracefully();
        worker.shutdownGracefully()
    }
}