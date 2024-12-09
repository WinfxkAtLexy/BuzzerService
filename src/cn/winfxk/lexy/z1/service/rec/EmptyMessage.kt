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
* Created Date: 2024/12/9  13:12 */
package cn.winfxk.lexy.z1.service.rec

import cn.winfxk.lexy.z1.message.Message
import cn.winfxk.lexy.z1.service.Client
import com.alibaba.fastjson2.JSONObject

class EmptyMessage(type: String, message: JSONObject,client: Client) : RecMessage(type, message,client) {
    companion object {
        private val emptyJson = JSONObject();
        private const val emptyMessage = "服务端未对此消息做任何处理！";
    }

    override fun respond(): Message {
        return Message(
            isSuccess = false,
            message = emptyMessage,
            json = emptyJson
        );
    }
}