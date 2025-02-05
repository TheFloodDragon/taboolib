package taboolib.module.configuration

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.hocon.HoconFormat
import com.electronwill.nightconfig.json.JsonFormat
import com.electronwill.nightconfig.toml.TomlFormat
import taboolib.module.configuration.Type.values

/**
 * TabooLib
 * taboolib.module.configuration.ConfigFrame
 *
 * @author mac
 * @since 2021/11/21 10:52 下午
 */
enum class Type(private val format: () -> ConfigFormat<out Config>) {

    YAML({ YamlFormat.INSTANCE }),

    TOML({ TomlFormat.instance() }),

    JSON({ JsonFormat.emptyTolerantInstance() }),

    JSON_MINIMAL({ JsonFormat.minimalEmptyTolerantInstance() }),

    HOCON({ HoconFormat.instance() }),

    @Deprecated("命名歧义", ReplaceWith("MINIMAL_JSON"))
    FAST_JSON({ JsonFormat.minimalEmptyTolerantInstance() });

    fun newFormat(): ConfigFormat<out Config> {
        return format()
    }

    companion object {

        init {
            // 保持插入顺序
            System.setProperty("nightconfig.preserveInsertionOrder", "true")
        }

        fun getType(format: ConfigFormat<*>): Type {
            return values().first { it.newFormat().javaClass == format.javaClass }
        }
    }
}