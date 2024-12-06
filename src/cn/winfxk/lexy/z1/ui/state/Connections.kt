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
import cn.winfxk.lexy.z1.service.MyBusinessHandler
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.lexy.z1.ui.GUI.Companion.selectCursor
import cn.winfxk.tool.view.image.ImageView
import cn.winfxk.tool.view.toCenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    companion object {
        private val hintFont = Deploy.fonts.hwct.deriveFont(Font.PLAIN, 15f);
        private val contextFont = Deploy.fonts.hwct.deriveFont(Font.BOLD, 50f);
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
        Deploy.scope.launch {
            while (cn.winfxk.lexy.z1.isRunning) {
                context.text = "${MyBusinessHandler.getClientCount()}/${MyBusinessHandler.getMaxClientCount()}"
                delay(5000);
            }
        }
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
    }

    override fun start() {
        hint.setSize(hintSize * (hint.text.length + 2), hintSize * (2))
        context.size = size;
        linkHint.size = size;
        linkHint.setLocation(0, linkHint.font.size);
        icon.setLocation(width - icon.width - 10, 5);
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