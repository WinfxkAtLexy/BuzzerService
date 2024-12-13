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
* Created Date: 2024/12/12  10:24 */
package cn.winfxk.lexy.z1

import cn.winfxk.libk.log.Log
import cn.winfxk.libk.log.OnLogListener
import cn.winfxk.libk.log.Type
import cn.winfxk.libk.tool.Tool
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class Logsave : OnLogListener, Runnable, AutoCloseable {
    private var logFile = File(logDir, "${logFormat.format(Date())}.log")
    private val log = Log(this.javaClass.simpleName)
    private var writer = BufferedWriter(FileWriter(logFile.also {
        it.parentFile?.also { p ->
            if (! p.exists() || ! p.isDirectory)
                if (! p.mkdirs()) log.w("创建日志文件失败")
        }
        if (! it.exists() || ! it.isFile)
            if (it.createNewFile()) log.i("创建一个新日志文件[${it.name}]!")
            else log.w("日志文件创建失败[${it.name}]！")
    }, true));
    @Volatile private var isClose = false;

    companion object {
        val defaultDir = File("BuzzerServices/");
        val logDir = File(defaultDir, "logs/");
        val logFormat = SimpleDateFormat("yyyy/MM/MM-dd");
        private lateinit var main: Logsave;
        fun getMain() = main;
    }

    init {
        main = this;
    }

    override fun logListener(type: Type, tag: String?, message: Any?, throwable: Throwable?, title: String, context: String) {
        if (! isClose) writer.write("\n${Log.clearColor(context) ?: ""}");
    }

    override fun run() {
        log.i("日志守护者启动！将会自动保存日志。");
        val sleep = 1000 * 3600L;
        var text = logFormat.format(Date());
        var newText: String?;
        while (isRunning) {
            Tool.sleep(sleep);
            writer.flush();
            newText = logFormat.format(Date());
            if (! newText.equals(text)) {
                rsetLogFile();
                text = newText;
                log.i("已切换到新的日志文件[${logFile.name}].")
            }
            log.i("日志已保存至文件.")
        }
    }

    private fun rsetLogFile() {
        if (! logDir.exists()) logDir.mkdirs();
        val close = writer;
        close.flush();
        logFile = File(logDir, "${logFormat.format(Date())}.log")
        logFile.parentFile?.also { if (! it.exists() || ! it.isDirectory) if (! it.mkdirs()) log.w("创建日志文件失败") }
        if (! logFile.exists() || ! logFile.isFile) if (logFile.createNewFile())
            log.i("创建一个新日志文件[${logFile.name}]!")
        else log.w("日志文件创建失败[${logFile.name}]！")
        writer = BufferedWriter(FileWriter(logFile, true))
        close.close();
    }

    override fun close() {
        isClose = true;
        writer.flush();
        writer.close();
        log.i("关闭日志记录器.")
    }
}