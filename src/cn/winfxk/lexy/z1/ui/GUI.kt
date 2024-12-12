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
* Created Date: 2024/11/20  14:56 */
package cn.winfxk.lexy.z1.ui

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.appTitle
import cn.winfxk.lexy.z1.ui.cp.ContentPane
import cn.winfxk.lexy.z1.ui.cp.OfStatus
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.Tool
import cn.winfxk.tool.view.MyJFrame
import cn.winfxk.tool.view.MyJPanel
import java.awt.Color
import java.awt.Cursor
import java.awt.Point
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.util.*

class GUI : MyJPanel(), WindowListener {
    private val log = Log(this.javaClass.simpleName);
    private val frame = MyJFrame();
    private var isInitialized = false;
    private val cp: ContentPane;

    companion object {
        private lateinit var main: GUI;
        private val deploy = Deploy.deploy;
        private val point = deploy.toolkit.getBestCursorSize(20, 20).let { Point(it.width - 1, it.height - 1) }
        val cursor: Cursor = toolkit.createCustomCursor(deploy.getGTIcon(), point, "gt")
        val selectCursor: Cursor = toolkit.createCustomCursor(deploy.getFishIcon(), point, "fish")
        private val width = deploy.config.getInt("width", Tool.getMath(1600, 800, (deploy.screenSize.width / 1.2).toInt()));
        private val height = deploy.config.getInt("height", Tool.getMath(820, 600, (deploy.screenSize.height / 1.5).toInt()));
        fun getMain() = main;
        val backg = Color.white;
        /**
         * 判断是否支持图形界面
         */
        fun isGuiSupported(): Boolean {
            val desktop = System.getenv("DESKTOP_SESSION")
            if (desktop != null && desktop.isNotEmpty()) return true
            val x11 = System.getenv("DISPLAY")
            if (x11 != null && x11.isNotEmpty()) return true
            val headless = System.getProperty("java.awt.headless")
            if ("true".equals(headless, ignoreCase = true)) return false
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            return osName.contains("win")
        }
    }

    init {
        log.i("正在初始化用户界面...")
        main = this;
        cp = ContentPane();
        frame.title = appTitle;
        frame.setSize(GUI.width, GUI.height);
        frame.setLocation((deploy.screenSize.width / 2 - GUI.width / 2).coerceAtLeast(0), (deploy.screenSize.height / 2 - GUI.height / 2).coerceAtLeast(0))
        frame.contentPane = this;
        frame.iconImage = deploy.getIcon();
        frame.defaultCloseOperation = MyJFrame.HIDE_ON_CLOSE;
        frame.addWindowListener(this)
        frame.addComponentListener(this)
        frame.background = GUI.backg;
        frame.cursor = GUI.cursor;
        this.setSize(frame.size);
        cp.setLocation(0, 0);
        add(cp)
    }

    fun getFrame() = frame;
    override fun start() {
        cp.size = size;
        cp.start();
    }

    override fun isVisible() = frame.isVisible;
    fun showFrame() {
        if (! isGuiSupported()) return
        if (! frame.isVisible) {
            log.i("显示主界面")
            frame.isVisible = true;
            cp.setPage(OfStatus.Status)
        }
        frame.title = appTitle;
        start();
        if (! isInitialized) log.i("用户界面初始化完毕！")
        isInitialized = true;
    }

    fun close() {
        if (isGuiSupported()) return;
        log.i("正在关闭用户界面..")
    }

    override fun windowOpened(e: WindowEvent?) {
    }

    override fun windowClosing(e: WindowEvent?) {
        log.i("用户界面隐藏")
    }

    override fun windowClosed(e: WindowEvent?) {
    }

    override fun windowIconified(e: WindowEvent?) {
    }

    override fun windowDeiconified(e: WindowEvent?) {
    }

    override fun windowActivated(e: WindowEvent?) {
    }

    override fun windowDeactivated(e: WindowEvent?) {
    }
}