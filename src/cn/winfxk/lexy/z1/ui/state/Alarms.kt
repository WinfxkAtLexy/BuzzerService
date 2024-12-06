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
* Created Date: 2024/11/28  13:15 */
package cn.winfxk.lexy.z1.ui.state

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.service.MyBusinessHandler
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.tool.view.image.ImageView
import cn.winfxk.tool.view.toCenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLabel
import javax.swing.SwingConstants

/**
 * 报警数
 */
class Alarms : Selement(), MouseListener {
    private val hint = JLabel("报警状态");
    private val hintTitle = JLabel("当前报警数/最大报警数");
    private val context = JLabel();
    private val icon = JLabel();

    init {
        hint.toCenter();
        hint.setLocation(0, 0)
        hint.font = Deploy.fonts.hwct.deriveFont(Font.PLAIN, 12f)
        hint.foreground = Color.gray;
        add(hint);
        context.font = Deploy.fonts.hwct.deriveFont(Font.BOLD, 30f);
        context.isOpaque = false;
        context.toCenter()
        context.setLocation(0, 0)
        add(context);
        Deploy.scope.launch {
            while (cn.winfxk.lexy.z1.isRunning) {
                context.text = "${MyBusinessHandler.alarmsCount}/${MyBusinessHandler.maxAlarmsCount}"
                delay(5000);
            }
        }
        hintTitle.horizontalAlignment = SwingConstants.CENTER;
        hintTitle.verticalAlignment = SwingConstants.TOP;
        hintTitle.font = Font("楷体", Font.BOLD, 12);
        hintTitle.isOpaque = false;
        hintTitle.setLocation(0, 0)
        hintTitle.foreground = Color.gray
        add(hintTitle);
        icon.setSize(17, 17);
        icon.icon = ImageView.getIcon(Deploy.deploy.getSettingIcon(), icon.size.width, icon.size.height);
        icon.toCenter();
        icon.isOpaque = false;
        icon.addMouseListener(this)
        add(icon);
    }

    override fun start() {
        val hintSize = hint.font.size;
        hint.setSize(hintSize * (hint.text.length + 2), hintSize * (2))
        context.size = size;
        hintTitle.size = size;
        hintTitle.setLocation(0, hintTitle.font.size);
        icon.setLocation(width - icon.width - 14, 10);
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
}