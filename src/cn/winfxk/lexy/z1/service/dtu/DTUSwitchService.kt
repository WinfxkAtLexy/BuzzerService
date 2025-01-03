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
* Created Date: 2025/1/3  09:26 */
package cn.winfxk.lexy.z1.service.dtu

import cn.winfxk.lexy.z1.isRunning
import cn.winfxk.lexy.z1.link.LinkMain
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.Tool

class DTUSwitchService : Thread() {
    private val log = Log(this.javaClass.simpleName)
    @Volatile private var waring = false;

    init {
        log.i("DTU状态服务初始化》。。");
    }

    override fun run() {
        while (isRunning) {
            Tool.sleep(1000);
            if (! isRunning) break;
            if (LinkMain.isEmpty()) {
                if (waring) normal();
            } else if (! waring) waring();
        }
    }

    private fun waring() {
        waring = true;
        log.i("切换至报警状态...")
        for (i in 1 .. 4) {
            DTUlinkAdapter.sendMessage("a$i")
            Tool.sleep(200);
        }
    }

    private fun normal() {
        waring = false;
        log.i("移除至报警状态...")
        for (i in 1 .. 4) {
            DTUlinkAdapter.sendMessage("b$i")
            Tool.sleep(200);
        }
    }
}