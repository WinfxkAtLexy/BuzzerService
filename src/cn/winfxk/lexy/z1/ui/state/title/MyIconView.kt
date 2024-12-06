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
* Created Date: 2024/12/2  08:09 */
package cn.winfxk.lexy.z1.ui.state.title

import cn.winfxk.lexy.z1.Deploy
import cn.winfxk.lexy.z1.isRunning
import cn.winfxk.lexy.z1.link.LinkMain
import cn.winfxk.lexy.z1.link.LinkMessage
import cn.winfxk.lexy.z1.message.Message
import cn.winfxk.lexy.z1.ui.GUI
import cn.winfxk.libk.log.Log
import cn.winfxk.tool.view.MyJPanel
import cn.winfxk.tool.view.image.ImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.swing.JLabel

class MyIconView : MyJPanel() {
    private val icon = JLabel();
    private var warning = ImageView.getIcon(Deploy.deploy.getCautionIcon())
    private var normal = ImageView.getIcon(Deploy.deploy.getNormalIcon())
    private var type: IconType = IconType.Normal;
    private val log by lazy { Log(this.javaClass.simpleName) }

    init {
        isOpaque = true;
        icon.setLocation(0, 0)
        icon.isOpaque = true;
        icon.background = GUI.backg;
        icon.icon = normal;
        startWhile();
        add(icon)
    }

    private fun startWhile() {
        Deploy.scope.launch {
            while (isRunning) {
                delay(100);
                if (LinkMain.isEmpty()) if (type != IconType.Normal) resetIcon(IconType.Normal)
                if (! LinkMain.isEmpty()) if (type != IconType.Warning) resetIcon(IconType.Warning)
            }
        }
    }

    override fun start() {
        var isreset = false;
        if (warning.iconWidth != size.width || warning.iconHeight != size.height) {
            isreset = true;
            warning = ImageView.getIcon(Deploy.deploy.getCautionIcon(), size.width, size.height)
        }
        if (normal.iconWidth != size.width || normal.iconHeight != size.height) {
            isreset = true;
            normal = ImageView.getIcon(Deploy.deploy.getNormalIcon(), size.width, size.height)
        }
        icon.size = size;
        if (isreset) resetIcon(type)
    }

    private fun resetIcon(type: IconType) {
        icon.icon = when (type) {
            IconType.Warning -> warning
            IconType.Normal  -> normal
        }
        if (type != this.type) log.i("设置服务器消息状态为：$type")
        this.type = type
    }
}