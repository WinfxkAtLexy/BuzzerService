package cn.winfxk.lexy.z1

import cn.winfxk.lexy.z1.link.CallService
import cn.winfxk.lexy.z1.service.Service
import cn.winfxk.lexy.z1.service.dtu.DTUService
import cn.winfxk.lexy.z1.tray.MySystemTray
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.Tool
import cn.winfxk.tool.view.JOptionPane
import cn.winfxk.tool.view.MyJPanel
import java.awt.Color
import java.awt.Font
import java.awt.Window
import javax.swing.JLabel
import javax.swing.JWindow
import javax.swing.SwingConstants
import kotlin.system.exitProcess

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
* Created Date: 2024/11/19  15:59 */
class Start : MyJPanel() {
    val frame = JWindow();
    private val label = JLabel("初始化中请稍后...");
    @Volatile
    private var isRunning = true;

    companion object {
        private val deploy = Deploy.deploy;
        private val width = Tool.getMath(600, 400, (deploy.screenSize.width / 4));
        private val height = Tool.getMath(300, 150, (deploy.screenSize.height / 3.6).toInt());
        private val x = deploy.screenSize.width / 2 - width / 2;
        private val y = deploy.screenSize.height / 2 - height / 2;
        private val backg = Color.WHITE;
        private val font = Font("楷体", Font.BOLD, 17);
    }

    init {
        frame.type = Window.Type.UTILITY;
        frame.setBounds(Start.x, Start.y, Start.width, Start.height);
        frame.background = backg;
        frame.isAlwaysOnTop = true;
        frame.contentPane = this;
        label.font = Start.font;
        label.background = backg;
        label.isOpaque = false;
        label.setLocation(0, 0)
        label.horizontalAlignment = SwingConstants.CENTER;
        label.verticalAlignment = SwingConstants.CENTER;
        add(label);
    }

    override fun start() {
        label.size = size;
    }

    fun showFrame(title: String? = null, alpha: Boolean = true) {
        frame.isVisible = true;
        if (title != null) label.text = title;
        start();
        if (alpha) Thread {
            frame.opacity = 0.0f;
            for (i in 0 .. 100) {
                if (! isRunning) break;
                frame.opacity = i.toFloat() / 100;
                Tool.sleep(1)
            }
            if (isRunning) frame.opacity = 1f;
        }.start()
    }

    override fun setTitle(title: String) {
        label.text = title;
    }

    fun hideFrame(alpha: Boolean = true) {
        isRunning = false;
        if (alpha) Thread {
            frame.opacity = 1.0f;
            for (i in 100 downTo 0) {
                frame.opacity = i.toFloat() / 100;
                Tool.sleep(1)
            }
            frame.isVisible = false;
        }.start() else frame.isVisible = false;
    }
}

const val appTitle = "Buzzer Service"
const val version = "v1.0.alpha"
var isRunning = false;
fun main() {
    Log.i("程序初始化...")
    val logsave = Logsave();
    Log.addListener(logsave)
    Thread(logsave).start();
    isRunning = true;
    val log = Log("initialize");
    log.i("应用程序加载中...")
    val start = Start();
    try {
        start.showFrame();
        val deploy = Deploy.deploy;
        deploy.init();
        Service().start();
        GUI().showFrame();
        CallService().start();
        Thread(DTUService()).start();
        if (MySystemTray.isSystemTray) MySystemTray().start(); else Log.i("当前系统环境暂不支持托盘，跳过创建..")
        log.i("应用程序已加载完成！")
        start.hideFrame()
    } catch (e: Exception) {
        log.e("加载应用程序时出现异常！！", e);
        log.i("1")
        start.frame.dispose();
        JOptionPane.showMessageDialog(null, "加载应用程序时出现异常！\n${e.message}")
        log.i("2")
        close(- 1);
    }
}
@Synchronized
fun close(status: Int = - 1) {
    isRunning = false;
    Log.i("Close", "正在结束进程和服务..")
    GUI.getMain().close()
    if (MySystemTray.isInitialized()) MySystemTray.getMain().close();
    Service.getMain().shutdown()
    Log.i("Close", "关闭前保存数据")
    Deploy.deploy.config.save();
    Log.i("Close", "关闭应用程序...")
    Logsave.getMain().close();
    exitProcess(status)
}