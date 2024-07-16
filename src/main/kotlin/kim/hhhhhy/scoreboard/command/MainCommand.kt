package kim.hhhhhy.scoreboard.command

import kim.hhhhhy.scoreboard.command.sub.BoardCommand
import kim.hhhhhy.scoreboard.data.BoardConfig
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.function.info
import taboolib.expansion.createHelper

@CommandHeader(
    "scoreboardlite",
    ["sbl"],
    permissionDefault = PermissionDefault.TRUE
)
object MainCommand {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody(permission = "scoreboardlite.toggle.self", permissionDefault = PermissionDefault.TRUE)
    val toggle = BoardCommand.toggle
}