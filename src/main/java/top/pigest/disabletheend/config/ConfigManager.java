package top.pigest.disabletheend.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    public static Path CONFIG_FOLDER;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static DisableTheEndConfig getDisableTheEndConfig() {
        DisableTheEndConfig config = new DisableTheEndConfig();

        if (!CONFIG_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(CONFIG_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path configPath = CONFIG_FOLDER.resolve(DisableTheEndConfig.CONFIG_NAME + ".json");
        if (configPath.toFile().isFile()) {
            try {
                config = GSON.fromJson(FileUtils.readFileToString(configPath.toFile(), StandardCharsets.UTF_8),
                        DisableTheEndConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return config;
    }

    public static void saveDisableTheEndConfig(DisableTheEndConfig config) {
        if (!CONFIG_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(CONFIG_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path configPath = CONFIG_FOLDER.resolve(DisableTheEndConfig.CONFIG_NAME + ".json");
        try {
            FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
