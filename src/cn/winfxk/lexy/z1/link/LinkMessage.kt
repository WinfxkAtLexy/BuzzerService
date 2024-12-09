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
* Created Date: 2024/11/29  14:38 */
package cn.winfxk.lexy.z1.link

import cn.winfxk.lexy.z1.isRunning
import cn.winfxk.lexy.z1.message.Message
import cn.winfxk.lexy.z1.service.Client
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.Tool

class LinkMessage(client: Client, val message: Message, val key: String) : Runnable {
    var time = System.currentTimeMillis();
    private val log = Log("${client.name}-${this.javaClass.simpleName}")
    @Volatile
    private var isRuning = true;

    init {
        log.i("客户端报警消息已接收")
        Thread(this).start();
    }

    override fun run() {
        var new = time;
        while (isRuning()) {
            new = time;
            Tool.sleep(4000);
            if (! isRuning()) break;
            if (new == time) {
                log.i("客户端报警终止! ")
                LinkMain.getList().remove(key);
                isRuning = false;
                break;
            }
        }
    }

    private fun isRuning() = isRuning && isRunning;
}