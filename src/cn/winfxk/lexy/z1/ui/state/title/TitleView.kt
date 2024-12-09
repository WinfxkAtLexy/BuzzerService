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
* Created Date: 2024/11/28  13:14 */
package cn.winfxk.lexy.z1.ui.state.title

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.lexy.z1.ui.cp.ContentPane
import cn.winfxk.lexy.z1.ui.cp.OfStatus
import cn.winfxk.lexy.z1.ui.state.Selement
import cn.winfxk.libk.log.Log
import cn.winfxk.tool.view.MyJPanel
import cn.winfxk.tool.view.image.ImageView
import cn.winfxk.tool.view.toCenter
import java.awt.Dimension
import java.awt.Font
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLabel

/**
 * 标题
 */
class TitleView : Selement(), MouseListener {
    private val title = JLabel("莱克电气");
    private val ztitle = ZTT();
    private val icon = JLabel();
    private val log by lazy { Log(this.javaClass.simpleName) }
    private val iconView = MyIconView();

    companion object {
        private const val ztt = "产线状态智能监控平台";
        private val zttFont = Font("楷体", Font.BOLD, 20);
    }

    init {
        title.background = GUI.backg;
        title.font = Font("楷体", Font.BOLD, 35);
        title.toCenter()
        title.setLocation(0, 0);
        add(title)
        ztitle.background = GUI.backg;
        add(ztitle)
        background = GUI.backg;
        icon.setSize(20, 20);
        icon.icon = ImageView.getIcon(Deploy.deploy.getSettingIcon(), icon.size.width, icon.size.height);
        icon.addMouseListener(this)
        add(icon);
        iconView.size = icon.size;
        add(iconView);
    }

    override fun start() {
        title.setSize(width, (height * (title.font.size.toDouble() / (title.font.size + ztitle.font.size))).toInt())
        ztitle.setSize(width / 5 * 2, height - title.height);
        ztitle.setLocation(width / 2 - ztitle.width / 2, title.height);
        ztitle.start();
        icon.setLocation(width - icon.width - 14, 10);
        iconView.setLocation(icon.x - iconView.width - 5, icon.y)
        iconView.start();
    }

    private class ZTT : MyJPanel() {
        private val list = ArrayList<JLabel>();

        init {
            for (index in ztt.indices)
                list.add(JLabel(ztt.subSequence(index, index + 1).toString()).also {
                    it.font = zttFont;
                    it.background = GUI.backg;
                    it.toCenter()
                    add(it);
                })
        }

        override fun start() {
            val size = Dimension((width / list.size).coerceAtLeast(zttFont.size + 4), height);
            var label: JLabel;
            for (index in list.indices) {
                label = list[index];
                label.size = size;
                label.setLocation(index * size.width, 0);
            }
        }
    }

    override fun mouseClicked(e: MouseEvent?) {
        log.i("跳转到设置页面")
        ContentPane.getMain().setPage(OfStatus.Setting)
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