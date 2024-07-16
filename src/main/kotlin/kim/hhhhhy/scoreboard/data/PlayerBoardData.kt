package kim.hhhhhy.scoreboard.data

import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

class PlayerBoardData {

    companion object {
        val handle =  PlayerBoardData()
    }

    private val playerToggle = ConcurrentHashMap<String, Boolean>()

    fun getToggle(player: String): Boolean {
        return playerToggle[player] ?: false
    }

    fun toggle(player: String): Boolean {
        playerToggle[player] = !getToggle(player)
        return playerToggle[player]!!
    }

    fun setToggle(player: String, toggle: Boolean) {
        playerToggle[player] = toggle
    }

    fun getToggle(player: Player): Boolean {
        return playerToggle[player.name] ?: false
    }

    fun toggle(player: Player): Boolean {
        playerToggle[player.name] = !getToggle(player)
        return playerToggle[player.name]!!
    }

    fun setToggle(player: Player, toggle: Boolean) {
        playerToggle[player.name] = toggle
    }
}