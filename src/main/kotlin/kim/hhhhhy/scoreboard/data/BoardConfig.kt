package kim.hhhhhy.scoreboard.data

import kim.hhhhhy.scoreboard.feature.ScoreBoard.updateBoard
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.util.onlinePlayers

data class BoardConfig(
    val `if`: String?,
    val def: Boolean?,
    val pri: Int,
    val update: Long,
    val title: String,
    val info: List<String>
) {

    companion object {
        @Config("scoreboard.yml", autoReload = true)
        lateinit var config: Configuration

        private val boards = mutableListOf<BoardConfig>()

        @Awake(LifeCycle.ACTIVE)
        fun register() {
            config.onReload {
                reloadConfig()
                info("已自动重载 scoreboard.yml")
            }
        }

        fun reloadConfig() {
            boards.clear()
            config.getKeys(false).forEach {
                val section = config.getConfigurationSection(it)!!
                boards.add(read(section))
            }
            boards.sortBy { it.pri }
            onlinePlayers.forEach {
                it.updateBoard()
            }
        }

        private fun read(section: ConfigurationSection): BoardConfig {
            return Configuration.deserialize<BoardConfig>(section, ignoreConstructor = true)
        }

        fun write(boardConfig: BoardConfig): ConfigurationSection {
            return Configuration.serialize(boardConfig)
        }

        fun getBoards(): MutableList<BoardConfig> {
            return boards
        }
    }
}
