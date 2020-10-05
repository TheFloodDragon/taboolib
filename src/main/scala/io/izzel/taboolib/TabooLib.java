package io.izzel.taboolib;

import io.izzel.taboolib.common.plugin.IllegalAccess;
import io.izzel.taboolib.common.plugin.InternalPlugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.config.TConfigWatcher;
import io.izzel.taboolib.module.db.local.Local;
import io.izzel.taboolib.module.db.local.LocalPlayer;
import io.izzel.taboolib.module.dependency.Dependency;
import io.izzel.taboolib.module.locale.TLocaleLoader;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.util.Files;
import io.izzel.taboolib.util.IO;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.NumberConversions;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * @Author 坏黑
 * @Since 2019-07-05 10:39
 * <p>
 * 注意与 TabooLib4.x 版本的兼容
 * 可能存在同时运行的情况
 */
@Dependency(
        maven = "org.slf4j:slf4j-api:1.7.25",
        url = "http://repo.ptms.ink/repository/maven-public/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/org.slf4j-slf4j-api-1.7.25.jar"
)
@Dependency(
        maven = "com.zaxxer:HikariCP:3.1.0",
        url = "http://repo.ptms.ink/repository/maven-releases/com/zaxxer/HikariCP/3.1.0/HikariCP-3.1.0.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/com.zaxxer-HikariCP-3.1.0.jar"
)
@Dependency(
        maven = "org.scala-lang:scala-library:2.12.8",
        url = "http://repo.ptms.ink/repository/maven-public/org/scala-lang/scala-library/2.12.8/scala-library-2.12.8.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/scala-library-2.12.8.jar"
)
@Dependency(
        maven = "org.kotlinlang:kotlin-stdlib:1.4.0-rc",
        url = "http://repo.ptms.ink/repository/maven-releases/org/kotlinlang/kotlin-stdlib/1.4.0-rc/kotlin-stdlib-1.4.0-rc-rc.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-stdlib-1.4.0-rc-1.4.0-rc.jar"
)
@Dependency(
        maven = "org.kotlinlang:kotlin-stdlib-jdk7:1.4.0-rc",
        url = "http://repo.ptms.ink/repository/maven-releases/org/kotlinlang/kotlin-stdlib-jdk7/1.4.0-rc/kotlin-stdlib-jdk7-1.4.0-rc-rc.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-stdlib-jdk7-1.4.0-rc.jar"
)
@Dependency(
        maven = "org.kotlinlang:kotlin-stdlib-jdk8:1.4.0-rc",
        url = "http://repo.ptms.ink/repository/maven-releases/org/kotlinlang/kotlin-stdlib-jdk8/1.4.0-rc/kotlin-stdlib-jdk8-1.4.0-rc-rc.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-stdlib-jdk8-1.4.0-rc.jar"
)
@Dependency(
        maven = "org.kotlinlang:kotlin-reflect:1.4.0-rc",
        url = "http://repo.ptms.ink/repository/maven-releases/org/kotlinlang/kotlin-reflect/1.4.0-rc/kotlin-reflect-1.4.0-rc-rc.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-reflect-1.4.0-rc.jar"
)
@Dependency(
        maven = "org.kotlinlang:kotlin-test:1.4.0-rc",
        url = "http://repo.ptms.ink/repository/maven-releases/org/kotlinlang/kotlin-test/1.4.0-rc/kotlin-test-1.4.0-rc-rc.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-test-1.4.0-rc.jar"
)
@Dependency(
        maven = "com.google.inject:guice:4.2.2",
        url = "http://repo.ptms.ink/repository/maven-releases/com/google/inject/guice/4.2.2/guice-4.2.2.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/guice-4.2.2.jar"
)
@Dependency(
        maven = "com.mongodb:mongodb:3.12.2",
        url = "http://repo.ptms.ink/repository/maven-public/com/mongodb/MongoDB/3.12.2/MongoDB-3.12.2-all.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/mongo-java-driver-3.12.2.jar"
)
public class TabooLib {

    private static TabooLib inst = new TabooLib();
    private static TLogger logger;
    private static TConfig config;

    // 当前运行版本
    private static double version;

    // 内部语言文件
    private final YamlConfiguration internal = new YamlConfiguration();

    @SuppressWarnings("BusyWait")
    public TabooLib() {
        inst = this;
        logger = TLogger.getUnformatted("TabooLib");
        // 配置文件从 config.yml 修改为 settings.yml 防止与老版本插件冲突
        config = TConfig.create(getPlugin(), "settings.yml");
        // 配置更新
        try {
            config.migrate();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 加载版本号
        try {
            version = NumberConversions.toDouble(IO.readFully(Files.getResource("__resources__/version"), StandardCharsets.UTF_8));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 加载内部语言文件
        try {
            internal.loadFromString(IO.readFully(Files.getResource("__resources__/lang/internal.yml"), StandardCharsets.UTF_8));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 加载日志屏蔽
        try {
            IllegalAccess.init();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 加载 TabooLib 语言文件
        TLocaleLoader.load(getPlugin(), false);
        // 加载 TabooLib
        TabooLibLoader.init();
        // 创建线程检测服务器是否关闭
        Executors.newSingleThreadExecutor().submit(() -> {
            while (NMS.handle().isRunning()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 保存数据
            Local.saveFiles();
            LocalPlayer.saveFiles();
            // 关闭文件监听
            TConfigWatcher.getInst().unregisterAll();
            // 关闭插件
            PluginLoader.stop(getPlugin());
        });
    }

    public static InternalPlugin getPlugin() {
        return InternalPlugin.getPlugin();
    }

    public static File getTabooLibFile() {
        return new File("libs/TabooLib.jar");
    }

    public YamlConfiguration getInternal() {
        return internal;
    }

    public static TabooLib getInst() {
        return inst;
    }

    public static TLogger getLogger() {
        return logger;
    }

    public static TConfig getConfig() {
        return config;
    }

    public static double getVersion() {
        return version;
    }
}
