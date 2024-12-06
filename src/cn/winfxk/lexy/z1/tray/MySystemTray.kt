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
* Created Date: 2024/11/20  15:59 */
package cn.winfxk.lexy.z1.tray

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.appTitle
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.libk.log.Log
import cn.winfxk.tool.view.JOptionPane
import cn.winfxk.tool.view.StartView
import cn.winfxk.tool.view.image.ImageView
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseEvent
import java.awt.event.MouseListener


class MySystemTray : StartView, MouseListener {
    private val log = Log(this.javaClass.simpleName);
    private val tray: SystemTray = SystemTray.getSystemTray()
    private val trayIcon: TrayIcon;

    companion object {
        private lateinit var main: MySystemTray;
        @Volatile
        private var isInitialized: Boolean = false
        fun isInitialized() = isInitialized;
        fun getMain() = main;
        val isSystemTray = SystemTray.isSupported()
    }

    init {
        log.i("正在初始化托盘服务..")
        trayIcon = TrayIcon(ImageView.getIcon(Deploy.deploy.getIcon(), 20, 20).image, appTitle, createPopupMenu())
        trayIcon.addMouseListener(this);
        main = this;
    }

    override fun start() {
        tray.add(trayIcon);
        log.i("托盘服务初始化完毕！")
    }

    fun close() {
        if (! isSystemTray) return;
        log.i("正在关闭托盘服务..")
    }

    private fun createPopupMenu(): PopupMenu {
        val popup = PopupMenu()
        val item1 = MenuItem("show main")
        item1.addActionListener {
            if (GUI.isGuiSupported()) {
                log.i("正在打开主界面")
                GUI.getMain().showFrame();
            } else log.i("当前系统环境暂不支持打开用户界面..")
        }
        popup.add(item1)
        popup.addSeparator()
        val exitItem = MenuItem("Exit")
        exitItem.addActionListener {
            if (GUI.isGuiSupported()) {
                if (JOptionPane.showConfirmDialog(null, "确定要关闭服务器吗？这将关闭所有连接和状态监听！", "警告", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE) == 0) {
                    log.i("用户主动退出应用程序");
                    cn.winfxk.lexy.z1.close(0);
                } else log.i("用户点击了退出，但点击了取消")
            } else {
                log.i("用户主动退出应用程序");
                cn.winfxk.lexy.z1.close(0);
            }
        }
        popup.add(exitItem)
        return popup
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e?.button == MouseEvent.BUTTON1)
            if (GUI.isGuiSupported()) {
                log.i("正在打开主界面")
                GUI.getMain().showFrame();
            } else log.i("当前系统环境暂不支持打开用户界面..")
    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }
}