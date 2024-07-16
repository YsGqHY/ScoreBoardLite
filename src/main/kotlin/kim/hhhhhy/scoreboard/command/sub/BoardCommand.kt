package kim.hhhhhy.scoreboard.command.sub

import kim.hhhhhy.scoreboard.data.PlayerBoardData
import kim.hhhhhy.scoreboard.feature.ScoreBoard
import kim.hhhhhy.scoreboard.feature.ScoreBoard.sendBoardOff
import kim.hhhhhy.scoreboard.feature.ScoreBoard.sendBoardOn
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.onlinePlayers

object BoardCommand {

    val toggle = subCommand {
        execute<Player> { sender, _, _ ->
            val toggle = PlayerBoardData.handle.toggle(sender)
            if (toggle) {
                ScoreBoard.evalBoard(sender)
                val board = ScoreBoard.playerBoardCache[sender.name] ?: return@execute
                    sender.sendBoardOn(board)
                sender.sendMessage("已启用计分板")
            } else {
                sender.sendBoardOff()
                sender.sendMessage("已禁用计分板")
            }
        }
        dynamic("player", optional = true, permission = "scoreboardlite.toggle.other") {
            suggestion<CommandSender> { _, _ ->
                onlinePlayers.map { it.name }
            }
            execute<CommandSender> { sender, ctx, _ ->
                val name = ctx["player"]
                val player = Bukkit.getPlayerExact(name) ?: return@execute sender.sendMessage("玩家 $name 已离线")
                val toggle = PlayerBoardData.handle.toggle(player)
                if (toggle) {
                    ScoreBoard.evalBoard(player)
                    val board = ScoreBoard.playerBoardCache[name] ?: return@execute
                    player.sendBoardOn(board)
                    sender.sendMessage("已为玩家 $name 启用计分板")
                } else {
                    player.sendBoardOff()
                    sender.sendMessage("已为玩家 $name 禁用计分板")
                }
            }
        }
    }
}