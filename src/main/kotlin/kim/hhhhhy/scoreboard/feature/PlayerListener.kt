package kim.hhhhhy.scoreboard.feature

import kim.hhhhhy.scoreboard.feature.ScoreBoard.playerBoardCache
import kim.hhhhhy.scoreboard.feature.ScoreBoard.playerTaskCache
import kim.hhhhhy.scoreboard.feature.ScoreBoard.updateBoard
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object PlayerListener {

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        submit(delay = 20L) {
            player.updateBoard(true)
        }
    }

    @SubscribeEvent
    fun onQuit(e: PlayerQuitEvent) {
        playerBoardCache.remove(e.player.name)
        playerTaskCache[e.player.name]?.cancel()
        playerTaskCache.remove(e.player.name)
    }

}