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
* Created Date: 2024/11/28  11:09 */
package cn.winfxk.lexy.z1.ui.cp

import cn.winfxk.lexy.z1.ui.set.Setting
import cn.winfxk.lexy.z1.ui.state.Status
import cn.winfxk.libk.log.Log
import cn.winfxk.tool.view.MyJPanel
import javax.swing.JComponent

class ContentPane : MyJPanel() {
    private val set = Setting();
    private val state = Status();
    private var label = OfStatus.Status;
    private val log by lazy { Log(this.javaClass.simpleName) }

    companion object {
        private lateinit var main: ContentPane;
        fun getMain() = main;
    }

    init {
        main = this;
        add(state);
        set.setLocation(0, 0);
        state.setLocation(0, 0);
    }

    override fun start() {
        set.setSize(width, height)
        state.setSize(width, height)
        set.start();
        state.start();
    }

    fun setPage(leable: OfStatus) {
        if (leable == this.label) {
            log.i("正在更新页面：$leable")
            updateUI()
            components.forEach { if (it is JComponent) it.updateUI(); }
            return;
        }
        removeAll();
        val mjp = when (leable) {
            OfStatus.Status  -> state
            OfStatus.Setting -> set
        };
        log.i("正在设置页面：$leable")
        add(mjp);
        mjp.start();
        mjp.updateUI();
        updateUI();
        label = leable;
    }
}