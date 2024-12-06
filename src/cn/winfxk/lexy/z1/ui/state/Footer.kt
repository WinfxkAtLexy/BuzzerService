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
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.tool.view.toCenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JLabel

/**
 * 页脚
 */
class Footer : Selement() {
    private val time = JLabel();
    private val motto = ArrayList<JLabel>();

    companion object {
        private const val mottos = "莱克电气 让世界更干净";
        private val hwct = Deploy.fonts.hwct.deriveFont(Font.PLAIN, 12f);
        private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private val timeFont = Deploy.fonts.hwct.deriveFont(Font.PLAIN, 9f);
    }

    init {
        for (index in mottos.indices)
            motto.add(JLabel().also {
                it.text = mottos[index].toString()
                it.font = hwct;
                it.toCenter()
                it.isOpaque = false;
                it.foreground = Color.gray
                add(it)
            })
        Deploy.scope.launch {
            while (cn.winfxk.lexy.z1.isRunning) {
                delay(100);
                if (! GUI.getMain().isVisible) continue;
                time.text = "${cn.winfxk.lexy.z1.version} - ${format.format(Date())}";
                time.updateUI();
            }
        }
        time.font = timeFont
        time.toCenter()
        time.isOpaque = false
        time.foreground = Color.gray
        add(time)
    }

    override fun start() {
        val size = Dimension((hwct.size * 1.8).toInt().let { if (it < hwct.size) width / mottos.length else it; }, height / 2)
        val x = (width / 2 - (size.width * mottos.length) / 2).coerceAtLeast(0);
        var label: JLabel;
        for (index in motto.indices) {
            label = motto[index];
            label.size = size;
            label.setLocation(index * label.width + x, 5);
        }
        time.setSize(width, (height - size.height) / 2);
        time.setLocation(0, height - time.height - 3);
    }
}