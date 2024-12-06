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
* Created Date: 2024/11/28  15:42 */
package cn.winfxk.lexy.z1.ui.state

import cn.winfxk.tool.view.MyJPanel
import java.awt.Color
import javax.swing.BorderFactory

abstract class Selement : MyJPanel() {
    companion object {
        private val border = BorderFactory.createLineBorder(Color.CYAN)
    }

    init {
        this.border = Selement.border;
    }
}