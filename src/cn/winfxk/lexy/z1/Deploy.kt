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
* Created Date: 2024/11/22  09:23 */
package cn.winfxk.lexy.z1

import cn.winfxk.libk.config.Config
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.utils.getImageByjar
import cn.winfxk.libk.tool.utils.getStreamByJar
import cn.winfxk.libk.tool.utils.readFont
import cn.winfxk.libk.tool.utils.throwException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File

class Deploy {
    val toolkit: Toolkit = Toolkit.getDefaultToolkit();
    val screenSize: Dimension = toolkit.screenSize;
    val defaultDir = File("BuzzerServices/");
    private lateinit var icon: BufferedImage;
    private lateinit var settingIcon: BufferedImage;
    private lateinit var backIcon: BufferedImage;
    private lateinit var listIcon: BufferedImage;
    private lateinit var gtIcon: BufferedImage;
    private lateinit var catIcon: BufferedImage;
    private lateinit var fishIcon: BufferedImage;
    private lateinit var normalIcon: BufferedImage;
    private lateinit var CautionIcon: BufferedImage;
    private var port = 1998;
    val config = Config(File(defaultDir, "config.json"))
    /**
     * 存储链接过的客户端的信息
     */
    val clients = File(defaultDir, "clients/");
    private val log = Log(this.javaClass.simpleName)
    val scope = Deploy.scope;

    companion object {
        val deploy by lazy { Deploy() }
        val scope = CoroutineScope(Dispatchers.Default);
        val fonts by lazy { MyFont(); }
    }

    fun init() {
        log.i("正在初始化应用程序配置..")
        if (! clients.exists() || ! clients.isDirectory) if (! clients.mkdirs()) log.e("创建客户端信息文件夹时出现异常！创建可能失败！")
        icon = this.getImageByjar("Icon.png") ?: throwException("无法加载资源：Icon.png");
        settingIcon = this.getImageByjar("SettingIcon.png") ?: throwException("无法加载资源：SettingIcon.png");
        listIcon = this.getImageByjar("list.png") ?: throwException("无法加载资源：list.png");
        backIcon = this.getImageByjar("BackIcon.png") ?: throwException("无法加载资源：BackIcon.png");
        gtIcon = this.getImageByjar("gt.png") ?: throwException("无法加载资源：gt.png");
        catIcon = this.getImageByjar("cat.png") ?: throwException("无法加载资源：cat.png");
        fishIcon = this.getImageByjar("fish.png") ?: throwException("无法加载资源：fish.png");
        normalIcon = this.getImageByjar("normal.png") ?: throwException("无法加载资源：normal.png");
        CautionIcon = this.getImageByjar("Caution.png") ?: throwException("无法加载资源：Caution.png");
        port = config.getInt("port", 1998);
        if (port <= 0) {
            log.e("设置的监听端口不正确！");
            throw Exception("设置的监听端口不正确！");
        }
        scope.launch {
            log.i("配置文件守护服务启动.")
            while (isRunning) {
                delay(600000)
                log.i("正在保存配置文件")
                config.save();
            }
        }
    }
    /**
     * 服务器监听端口
     */
    fun getPort() = port;
    fun getIcon() = icon;
    fun getSettingIcon() = settingIcon;
    fun getBackIcon() = backIcon;
    fun getListIcon() = listIcon;
    fun getGTIcon() = gtIcon;
    fun getCatIcon() = catIcon;
    fun getFishIcon() = fishIcon;
    fun getCautionIcon() = CautionIcon;
    fun getNormalIcon() = normalIcon;
    class MyFont {
        private val log by lazy { Log(this.javaClass.simpleName) }
        val hwct = this.getStreamByJar("hwct.ttf".also { log.i("加载字体资源：$it") })?.readFont() ?: throwException("hwct.ttf");
    }
}
