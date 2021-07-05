package taboolib.platform.type

import com.flowpowered.math.vector.Vector3d
import org.spongepowered.api.Game
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.Property
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.effect.sound.SoundType
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.gamemode.GameMode
import org.spongepowered.api.entity.living.player.gamemode.GameModes
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.service.permission.SubjectData
import org.spongepowered.api.service.whitelist.WhitelistService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.chat.ChatTypes
import org.spongepowered.api.text.title.Title
import org.spongepowered.api.util.Tristate
import taboolib.common.platform.ProxyGameMode
import taboolib.common.platform.ProxyPlayer
import taboolib.common.reflect.Reflex.Companion.static
import taboolib.common.util.Location
import java.net.InetSocketAddress
import java.util.*

/**
 * TabooLib
 * taboolib.platform.type.SpongePlayer
 *
 * @author tr
 * @since 2021/6/21 15:49
 */
class SpongePlayer(val player: Player) : ProxyPlayer {

    override val origin: Any
        get() = player

    override val name: String
        get() = player.name

    override val address: InetSocketAddress?
        get() = player.connection.address

    override val uniqueId: UUID
        get() = player.uniqueId

    override val ping: Int
        get() = player.connection.latency

    override val locale: String
        get() = player.locale.displayName

    override val world: String
        get() = player.world.name

    override val location: Location
        get() {
            val loc = player.location
            return Location(world, loc.x, loc.y, loc.z, player.headRotation.y.toFloat(), player.headRotation.x.toFloat())
        }

    override var isOp: Boolean
        get() = player.hasPermission("*")
        set(value) {
            player.subjectData.setPermission(SubjectData.GLOBAL_CONTEXT, "*", if (value) Tristate.TRUE else Tristate.UNDEFINED)
        }

    override var compassTarget: Location
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override var bedSpawnLocation: Location?
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override var displayName: String?
        get() = player.displayNameData.displayName().get().toPlain()
        set(value) {
            player.displayNameData.displayName().set(Text.of(value ?: ""))
        }

    override var playerListName: String?
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override var gameMode: ProxyGameMode
        get() = ProxyGameMode.fromString(player.gameMode().get().name)
        set(value) {
            player.gameMode().set(GameModes::class.java.static(value.name)!!)
        }

    override val isSneaking: Boolean
        get() = player.get(Keys.IS_SNEAKING).get()

    override val isSprinting: Boolean
        get() = player.get(Keys.IS_SPRINTING).get()

    override val isBlocking: Boolean
        get() = error("unsupported")

    override var isGliding: Boolean
        get() = player.get(Keys.IS_ELYTRA_FLYING).get()
        set(value) {
            player.getValue(Keys.IS_ELYTRA_FLYING).get().set(value)
        }

    override var isGlowing: Boolean
        get() = player.get(Keys.GLOWING).get()
        set(value) {
            player.getValue(Keys.GLOWING).get().set(value)
        }

    override var isSwimming: Boolean
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override val isRiptiding: Boolean
        get() = error("unsupported")

    override val isSleeping: Boolean
        get() = player.get(Keys.IS_SLEEPING).get()

    override val sleepTicks: Int
        get() = error("unsupported")

    override var isSleepingIgnored: Boolean
        get() = player.isSleepingIgnored
        set(value) {
            player.isSleepingIgnored = value
        }

    override val isDead: Boolean
        get() = player.health().get() <= 0

    override val isConversing: Boolean
        get() = error("unsupported")

    override val isLeashed: Boolean
        get() = error("unsupported")

    override val isOnGround: Boolean
        get() = player.isOnGround

    override val isInsideVehicle: Boolean
        get() = player.vehicle.isPresent

    override var hasGravity: Boolean
        get() = player.gravity().get()
        set(value) {
            player.gravity().set(value)
        }

    override val attackCooldown: Int
        get() = error("unsupported")

    override var playerTime: Long
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override val firstPlayed: Long
        get() = player.firstPlayed().get().toEpochMilli()

    override val lastPlayed: Long
        get() = player.lastPlayed().get().toEpochMilli()

    override var absorptionAmount: Double
        get() = player.get(Keys.ABSORPTION).orElse(0.0)
        set(value) {
            player.getValue(Keys.ABSORPTION).get().set(value)
        }

    override var noDamageTicks: Int
        get() = player.get(Keys.INVULNERABILITY_TICKS).orElse(0)
        set(value) {
            player.getValue(Keys.INVULNERABILITY_TICKS).get().set(value)
        }

    override var remainingAir: Int
        get() = player.get(Keys.REMAINING_AIR).orElse(0)
        set(value) {
            player.getValue(Keys.REMAINING_AIR).get().set(value)
        }

    override val maximumAir: Int
        get() = player.get(Keys.MAX_AIR).orElse(0)

    override var level: Int
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override var exp: Float
        get() = error("unsupported")
        set(_) {
            error("unsupported")
        }

    override var exhaustion: Float
        get() = player.exhaustion().get().toFloat()
        set(value) {
            player.exhaustion().set(value.toDouble())
        }

    override var saturation: Float
        get() = player.saturation().get().toFloat()
        set(value) {
            player.exhaustion().set(value.toDouble())
        }

    override var foodLevel: Int
        get() = player.foodLevel().get()
        set(value) {
            player.foodLevel().set(value)
        }

    override var health: Double
        get() = player.health().get()
        set(value) {
            player.health().set(value)
        }

    override var maxHealth: Double
        get() = player.maxHealth().get()
        set(value) {
            player.maxHealth().set(value)
        }

    override var allowFlight: Boolean
        get() = player.get(Keys.CAN_FLY).get()
        set(it) {
            player.getValue(Keys.CAN_FLY).get().set(it)
        }

    override var isFlying: Boolean
        get() = player.get(Keys.IS_FLYING).get()
        set(it) {
            player.getValue(Keys.IS_FLYING).get().set(it)
        }

    override var flySpeed: Float
        get() = player.get(Keys.FLYING_SPEED).get().toFloat()
        set(it) {
            player.getValue(Keys.FLYING_SPEED).get().set(it.toDouble())
        }

    override var walkSpeed: Float
        get() = player.get(Keys.WALKING_SPEED).get().toFloat()
        set(it) {
            player.getValue(Keys.WALKING_SPEED).get().set(it.toDouble())
        }

    override val pose: String
        get() = error("unsupported")

    override val facing: String
        get() = error("unsupported")

    override fun kick(message: String?) {
        player.kick(Text.of(message ?: ""))
    }

    override fun chat(message: String) {
        player.simulateChat(Text.of(message), Cause.of(EventContext.empty(), ""))
    }

    override fun playSound(location: Location, sound: String, volume: Float, pitch: Float) {
        player.playSound(SoundType.of(sound), Vector3d.from(location.x, location.y, location.z), volume.toDouble(), pitch.toDouble())
    }

    override fun playSoundResource(location: Location, sound: String, volume: Float, pitch: Float) {
        playSound(location, sound, volume, pitch)
    }

    override fun sendTitle(title: String?, subtitle: String?, fadein: Int, stay: Int, fadeout: Int) {
        player.sendTitle(Title.builder().title(Text.of(title ?: "")).subtitle(Text.of(subtitle ?: "")).fadeIn(fadein).stay(stay).fadeOut(fadeout).build())
    }

    override fun sendActionBar(message: String) {
        player.sendMessage(ChatTypes.ACTION_BAR, Text.of(message))
    }

    override fun sendMessage(message: String) {
        player.sendMessage(Text.of(message))
    }

    // TODO: 2021/7/6 Sponge Raw Message
    override fun sendRawMessage(message: String) {
        sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return player.hasPermission(permission)
    }

    override fun performCommand(command: String): Boolean {
        return Sponge.getCommandManager().process(player, command).successCount.isPresent
    }

    override fun teleport(loc: Location) {
        val world = Sponge.getServer().getWorld(loc.world ?: return).orElseThrow()
        val location = org.spongepowered.api.world.Location(world, Vector3d.from(loc.x, loc.y, loc.z))
        player.location = location
        player.headRotation = Vector3d.from(loc.yaw.toDouble(), loc.pitch.toDouble(), 0.0)
    }
}