package top.pigest.disabletheend.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisableTheEndConfig {

    @Expose
    public static final String CONFIG_NAME = "disable_the_end";

    @SerializedName("force_spawn")
    private ForceSpawnConfig forceSpawnConfig = new ForceSpawnConfig();

    @SerializedName("anti_anonymous")
    private boolean antiAnonymous = false;

    @SerializedName("disable_end_portal")
    private boolean disableEndPortal = true;

    @SerializedName("disable_named_entity_log")
    private boolean disableNamedEntityLog = true;

    public boolean isDisableEndPortal() {
        return disableEndPortal;
    }

    public boolean isAntiAnonymous() {
        return antiAnonymous;
    }

    public boolean isDisableNamedEntityLog() {
        return disableNamedEntityLog;
    }

    public ForceSpawnConfig getForceSpawnConfig() {
        return forceSpawnConfig;
    }
}
