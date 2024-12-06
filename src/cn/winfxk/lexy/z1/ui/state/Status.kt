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
* Created Date: 2024/11/28  11:13 */
package cn.winfxk.lexy.z1.ui.state

import cn.winfxk.lexy.z1.ui.state.title.TitleView
import cn.winfxk.tool.view.MyJPanel

class Status : MyJPanel() {
    private val titleView = TitleView();
    private val requests = Requests();
    private val alarms = Alarms();
    private val connections = Connections();
    private val footer = Footer();

    init {
        titleView.setLocation(0, 0)
        add(titleView);
        add(requests);
        add(alarms);
        add(connections);
        add(footer)
    }

    override fun start() {
        titleView.setSize(width, Math.max(height / 6, 50))
        titleView.start();
        footer.setSize(width, (titleView.height / 4).coerceAtLeast(30))
        footer.setLocation(0, height - footer.height - 5);
        footer.start();
        connections.setSize(width, ((footer.y - 5 - titleView.height) / 5) * 3);
        connections.setLocation(0, titleView.height + 1);
        connections.start();
        requests.setSize((width - 3) / 2, footer.y - connections.y - connections.height);
        requests.setLocation(0, connections.height + 1 + connections.y);
        requests.start();
        alarms.size = requests.size;
        alarms.setLocation(requests.width + 2, requests.y);
        alarms.start();
    }
}