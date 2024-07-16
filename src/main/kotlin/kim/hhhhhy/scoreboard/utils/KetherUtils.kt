package kim.hhhhhy.scoreboard.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture


fun ScriptFrame.getBukkitPlayer(name: ParsedAction<*>? = null): Player {
    val player = name?.let { Bukkit.getPlayerExact(this.newFrame(name).run<String>().get()) }
        ?: script().sender?.castSafely<Player>()
        ?: error("No player selected.")
    return player
}

fun ScriptFrame.getBukkitPlayer(name: String? = null): Player {
    val player = name?.let { Bukkit.getPlayerExact(it) }
        ?: script().sender?.castSafely<Player>()
        ?: error("No player selected.")
    return player
}

fun List<String>.parseKether(
    player: Player?,
    vars: Map<String, Any> = mapOf(),
    sets: List<Pair<String, Any>> = emptyList()
): List<String> {
    if (this.isEmpty()) {
        return listOf("")
    }
    return KetherFunction.parse(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun String?.parseKether(
    player: Player?,
    vars: Map<String, Any> = mapOf(),
    sets: List<Pair<String, Any>> = emptyList()
): String {
    if (this.isNullOrBlank()) {
        return ""
    }
    return KetherFunction.parse(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun List<String>.evalKether(
    player: Player?,
    vars: Map<String, Any> = mapOf(),
    sets: List<Pair<String, Any>> = emptyList()
): CompletableFuture<Any?> {
    if (this.isEmpty()) {
        val future = CompletableFuture<Any?>()
        future.complete(null)
        return future
    }
    return KetherShell.eval(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun String?.evalKether(
    player: Player?,
    vars: Map<String, Any> = mapOf(),
    sets: List<Pair<String, Any>> = emptyList()
): CompletableFuture<Any?> {
    if (this.isNullOrBlank()) {
        val future = CompletableFuture<Any?>()
        future.complete(null)
        return future
    }
    return KetherShell.eval(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun String?.evalKetherBoolean(
    player: Player?,
    vars: Map<String, Any> = mapOf(),
    sets: List<Pair<String, Any>> = emptyList()
): Boolean {
    if (this.isNullOrBlank()) {
        return true
    }
    return try {
        KetherShell.eval(this, ScriptOptions.builder().apply {
            sender(player ?: console())
            vars(vars)
            sets.forEach {
                set(it.first, it.second)
            }
        }.build()).thenApply {
            Coerce.toBoolean(it)
        }.get()
    } catch (_: Exception) {
        false
    }
}

fun List<String>.evalKetherBoolean(
    player: Player?,
    vars: Map<String, Any> = mapOf(),
    sets: List<Pair<String, Any>> = emptyList()
): Boolean {
    if (this.isEmpty()) {
        return true
    }
    return try {
        KetherShell.eval(this, ScriptOptions.builder().apply {
            sender(player ?: console())
            vars(vars)
            sets.forEach {
                set(it.first, it.second)
            }
        }.build()).thenApply {
            Coerce.toBoolean(it)
        }.get()
    } catch (_: Exception) {
        false
    }
}
