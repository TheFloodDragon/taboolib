@file:Isolated

package taboolib.platform.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.tabooproject.reflex.Reflex.Companion.getProperty
import taboolib.common.Isolated

val isBukkitServerRunning: Boolean
    get() {
        return try {
            !Bukkit.getServer().getProperty<Boolean>("console/stopped")!!
        } catch (ex: NoSuchFieldException) {
            !Bukkit.getServer().getProperty<Boolean>("console/hasStopped")!!
        }
    }

val onlinePlayers: List<Player>
    get() = Bukkit.getOnlinePlayers().toList()

fun Any.broadcast() = Bukkit.broadcastMessage(this.toString())