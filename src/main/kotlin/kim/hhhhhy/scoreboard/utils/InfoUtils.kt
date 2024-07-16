package kim.hhhhhy.scoreboard.utils

import org.bukkit.entity.Player
import taboolib.common.platform.function.info
import taboolib.common.util.asList
import taboolib.common5.util.replace


object InfoUtils {
    private val viewCondition: Regex by lazy { Regex("\\{condition?[:=] ?(.*?)}") }


    fun List<String>.parseContent(player: Player):  List<String> {
        val text = this
        val newText = arrayListOf<String>()
        text.forEach { t ->
            val condition = viewCondition.find(t)?.groupValues?.get(1)
            if (condition == null) {
                newText += t
            } else {
                if (!condition.evalKetherBoolean(player)) {
                    return@forEach
                }
                newText += t.replace(viewCondition, "")
            }
        }
        return newText.asList()
    }

}







