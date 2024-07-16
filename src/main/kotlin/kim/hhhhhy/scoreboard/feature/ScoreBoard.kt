package kim.hhhhhy.scoreboard.feature

import kim.hhhhhy.scoreboard.data.BoardConfig
import kim.hhhhhy.scoreboard.data.PlayerBoardData
import kim.hhhhhy.scoreboard.utils.InfoUtils.parseContent
import kim.hhhhhy.scoreboard.utils.Service
import kim.hhhhhy.scoreboard.utils.evalKetherBoolean
import kim.hhhhhy.scoreboard.utils.parseKether
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common.util.asList
import taboolib.module.chat.colored
import taboolib.module.nms.sendScoreboard
import java.util.concurrent.ConcurrentHashMap


object ScoreBoard {
    private val uniqueChars = (1..50).map { '黑' + it }

    val playerBoardCache = ConcurrentHashMap<String, BoardConfig>()

    val playerTaskCache = ConcurrentHashMap<String, PlatformExecutor.PlatformTask>()

    fun evalBoard(player: Player) {
        val boards = BoardConfig.getBoards()
        Service.run("ScoreBoardLite:eval") {
            boards.forEach { board ->
                val condition = board.`if` ?: return player.parseBoard(board)

                if (condition.evalKetherBoolean(player).not()) {
                    return@forEach
                }
                return player.parseBoard(board)
            }
        }
    }

    fun Player.sendBoardOn(board: BoardConfig) {
        Service.run("ScoreBoardLite:eval") {
            val content = arrayListOf<String>()
            content += board.title
            content.addAll(board.info)
            val display = content.asList().parseContent(this)
            sendScoreboard(*display.asList().colored().mapIndexed { index, s -> "§${uniqueChars[index]}$s" }.toTypedArray())
            playerTaskCache[this.name] ?: startTask(this)
        }
    }

    fun Player.sendBoardOff() {
        sendScoreboard("")
        stopTask(this)
    }

    fun Player.updateBoard(isJoin: Boolean = false) {
        evalBoard(this)
        val board = playerBoardCache[name] ?: return
        val toggle = if (isJoin) {
            board.def ?: false
        } else {
            PlayerBoardData.handle.getToggle(name)
        }
        PlayerBoardData.handle.setToggle(this, toggle)
        if (toggle) {
            sendBoardOn(board)
        } else {
            sendBoardOff()
        }
    }

    fun Player.parseBoard(board: BoardConfig) {
        playerBoardCache[this.name] = BoardConfig(
            board.`if`,
            board.def,
            board.pri,
            board.update,
            board.title.parseKether(this),
            board.info.parseKether(this)
        )
    }

    fun startTask(player: Player) {
        val board = playerBoardCache[player.name] ?: return
        playerTaskCache[player.name] = submitAsync(period = board.update) {
            player.updateBoard()
        }
    }

    fun stopTask(player: Player) {
        playerTaskCache[player.name]?.cancel()
        playerTaskCache.remove(player.name)
    }
}