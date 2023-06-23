package top.pigest.disabletheend.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import org.apache.commons.io.FileUtils;
import top.pigest.disabletheend.config.MuteList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class Mute {
    public static Path MUTELIST_FILE;
    public static MuteList muteList;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static MuteList getMuteList() {
        MuteList mutelist = new MuteList();

        Path configPath = MUTELIST_FILE;
        if (configPath.toFile().isFile()) {
            try {
                mutelist = GSON.fromJson(FileUtils.readFileToString(configPath.toFile(), StandardCharsets.UTF_8), MuteList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.write(configPath.toFile(), GSON.toJson(mutelist), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mutelist;
    }

    public static void saveMuteList(MuteList muteList) {
        Path configPath = MUTELIST_FILE;
        try {
            FileUtils.write(configPath.toFile(), GSON.toJson(muteList), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mute(GameProfile profile, String reason, String expiry) {
        if(isMuted(profile)) {
            return;
        }
        MuteList.MuteEntry muteEntry = new MuteList.MuteEntry();
        muteEntry.setUUID(profile.getId().toString());
        muteEntry.setExpiry(expiry);
        muteEntry.setReason(reason);
        muteList.getMuteEntries().add(muteEntry);
        saveMuteList(muteList);
    }

    public static void unMute(GameProfile profile) {
        if(!isMuted(profile)) {
            return;
        }
        muteList.getMuteEntries().removeIf(muteEntry -> UUID.fromString(muteEntry.getUUID()).equals(profile.getId()));
        saveMuteList(muteList);
    }

    public static boolean isMutedWithExpiry(GameProfile profile) {
        Optional<MuteList.MuteEntry> optionalMuteEntry = muteList.getMuteEntries().stream().filter(muteEntry -> UUID.fromString(muteEntry.getUUID()).equals(profile.getId())).findFirst();
        if(optionalMuteEntry.isPresent()) {
            MuteList.MuteEntry muteEntry = optionalMuteEntry.get();
            if(muteEntry.getExpiry().equals("permanent")) {
                return true;
            }
            Date date = new Date(Date.parse(muteEntry.getExpiry()) - 1000 * 60 * 60 * 14);
                if(date.before(new Date())) {
                    unMute(profile);
                    return false;
                } else {
                    return true;
                }
        } else {
            return false;
        }
    }

    public static boolean isMuted(GameProfile profile) {
        return muteList.getMuteEntries().stream().anyMatch(muteEntry -> UUID.fromString(muteEntry.getUUID()).equals(profile.getId()));
    }

    public static String getMuteReason(GameProfile profile) {
        if(!isMuted(profile)) {
            return null;
        }
        Optional<MuteList.MuteEntry> optional = muteList.getMuteEntries().stream().filter(muteEntry -> UUID.fromString(muteEntry.getUUID()).equals(profile.getId())).findFirst();
        return optional.map(MuteList.MuteEntry::getReason).orElse(null);
    }

    public static int getMuteRemainingSeconds(GameProfile profile) {
        Optional<MuteList.MuteEntry> optionalMuteEntry = muteList.getMuteEntries().stream().filter(muteEntry -> UUID.fromString(muteEntry.getUUID()).equals(profile.getId())).findFirst();
        if(optionalMuteEntry.isPresent()) {
            MuteList.MuteEntry muteEntry = optionalMuteEntry.get();
            if(muteEntry.getExpiry().equals("permanent")) {
                return -1;
            }
            return (int) (((Date.parse(muteEntry.getExpiry()) - 1000 * 60 * 60 * 14) - System.currentTimeMillis()) / 1000);
        }
        return -1;
    }
}
