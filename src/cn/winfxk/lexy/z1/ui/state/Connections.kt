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
* Created Date: 2024/11/28  13:13 */
package cn.winfxk.lexy.z1.ui.state

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.isRunning
import cn.winfxk.lexy.z1.service.MyBusinessHandler
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.libk.tool.Tool
import cn.winfxk.tool.view.image.ImageView
import cn.winfxk.tool.view.toCenter
import java.awt.Color
import java.awt.Font
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLabel
import javax.swing.SwingConstants

/**
 * 连接数
 */
class Connections : Selement(), MouseListener {
    private val hint = JLabel("链接状态");
    private val context = JLabel()
    private val hintSize = hintFont.size;
    private val linkHint = JLabel("当前已连接/最大连接数")
    private val icon = JLabel();
    private val runTime = JLabel(getRunTime());

    companion object {
        private val hintFont = Deploy.fonts.hwct.deriveFont(Font.PLAIN, 15f);
        private val contextFont = Deploy.fonts.hwct.deriveFont(Font.BOLD, 50f);
        private const val dayTime = 24 * 60 * 60;
        private const val hourTime = 60 * 60;
    }

    init {
        hint.font = hintFont;
        hint.setLocation(0, 0);
        hint.isOpaque = false;
        hint.toCenter()
        hint.foreground = Color.gray;
        add(hint);
        context.font = contextFont;
        context.isOpaque = false;
        context.toCenter()
        context.setLocation(0, 0)
        add(context);
        Thread(this::resetConnectCount).start()
        Thread(this::resetRunTime).start()
        linkHint.horizontalAlignment = SwingConstants.CENTER;
        linkHint.verticalAlignment = SwingConstants.TOP;
        linkHint.font = Font("楷体", Font.BOLD, 15);
        linkHint.isOpaque = false;
        linkHint.setLocation(0, 0)
        linkHint.foreground = Color.gray
        add(linkHint);
        icon.setSize(24, 24);
        icon.icon = ImageView.getIcon(Deploy.deploy.getListIcon(), icon.size.width, icon.size.height);
        icon.toCenter();
        icon.isOpaque = false;
        icon.addMouseListener(this)
        add(icon);
        runTime.toCenter();
        runTime.font = Deploy.fonts.hwct.deriveFont(Font.BOLD, 12f);
        runTime.foreground = Color(200, 200, 200);
        runTime.isOpaque = false;
        add(runTime)
    }

    override fun start() {
        hint.setSize(hintSize * (hint.text.length + 2), hintSize * (2))
        context.size = size;
        linkHint.size = size;
        linkHint.setLocation(0, linkHint.font.size);
        icon.setLocation(width - icon.width - 10, 5);
        runTime.setSize(width, (runTime.font.size * 1.1).toInt());
        runTime.setLocation(0, (height - runTime.height * 1.2).toInt())
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
        GUI.getMain().getFrame().cursor = GUI.selectCursor
    }

    override fun mouseExited(e: MouseEvent?) {
        GUI.getMain().getFrame().cursor = GUI.cursor
    }

    fun resetConnectCount() {
        while (isRunning) {
            context.text = "${MyBusinessHandler.getClientCount()}/${MyBusinessHandler.getMaxClientCount()}"
            Tool.sleep(5000);
        }
    }

    fun resetRunTime() {
        while (isRunning) {
            runTime.text = getRunTime();
            Tool.sleep(1000);
            MyBusinessHandler.runTime ++;
            MyBusinessHandler.maxRunTime ++;
        }
    }

    private fun getRunTime(): String {
        return "${timeToString(MyBusinessHandler.runTime)}/${timeToString(MyBusinessHandler.maxRunTime)}"
    }

    private fun timeToString(time: Long): String {
        val day = time / dayTime
        val hour = (time % dayTime) / hourTime
        val minute = (time % hourTime) / 60
        val second = time % 60
        return "${day}.${hour}.${minute}.${second}"
    }
}