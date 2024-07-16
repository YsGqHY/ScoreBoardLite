package kim.hhhhhy.scoreboard

import kim.hhhhhy.scoreboard.data.BoardConfig
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object ScoreBoardLite : Plugin() {

    // 项目使用TabooLib Start Jar 创建!
    override fun onEnable() {
        info("动态解析Kether的计分板!")
        BoardConfig.reloadConfig()
    }
}