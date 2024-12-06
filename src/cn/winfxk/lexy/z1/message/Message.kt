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
* Created Date: 2024/11/21  10:27 */
package cn.winfxk.lexy.z1.message

import cn.winfxk.libk.tool.Tool
import cn.winfxk.libk.tool.utils.toJsonString
import com.alibaba.fastjson2.JSONObject
import java.util.*

class Message(private val isSuccess: Boolean = false, private val message: String, private val json: JSONObject = emptyJson, private var type: MessageType = MessageType.Response) {
    companion object {
        private val emptyJson = JSONObject();
        private fun createID(): String = UUID.randomUUID().toString() + "-" + Tool.CompressNumber(System.currentTimeMillis());
    }

    private var ID = createID();
    fun getID(): String = ID;
    fun setID(ID: String): Message {
        this.ID = ID;
        return this;
    }

    fun getType(): MessageType = type;
    fun setType(type: MessageType): Message {
        this.type = type;
        return this;
    }

    override fun toString(): String {
        val json = JSONObject()
        json["isSuccess"] = isSuccess
        json["message"] = message
        json["type"] = type.name;
        json["json"] = this.json;
        json["ID"] = getID();
        return json.toJsonString()
    }
}