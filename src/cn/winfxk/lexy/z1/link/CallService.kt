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
* Created Date: 2024/12/10  10:22 */
package cn.winfxk.lexy.z1.link

import cn.winfxk.lexy.z1.isRunning
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.Tool

class CallService : Thread() {
    private val log = Log(this.javaClass.simpleName)

    init {
        log.i("呼叫服务初始化~")
    }

    override fun run() {
        log.i("呼叫服务启动!");
        while (isRunning) {
            Tool.sleep(1000);
            if (! isRunning) {
                log.i("呼叫服务已关闭!");
                break;
            }
            if (LinkMain.isEmpty()) continue;
            for (link in LinkMain.getList().values) {
                if (! link.isRuning()) continue;
                link.call();
            }
        }
    }
}