package top.pigest.disabletheend.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class DisableTheEndConfig {
    @Expose
    public static final String CONFIG_NAME = "force_spawn";

    @SerializedName("enabled")
    private boolean enabled = false;

    @SerializedName("spawn_pos")
    private SpawnPos spawnPos = new SpawnPos();

    @SerializedName("spawn_rotation")
    private SpawnRotation spawnRotation = new SpawnRotation();

    public void setSpawnRotation(Vec2f vec2f) {
        this.getSpawnRotation().setX(vec2f.x);
        this.getSpawnRotation().setY(vec2f.y);
        ConfigManager.saveDisableTheEndConfig(this);
    }

    public void setSpawnPos(Vec3d vec3d) {
        this.getSpawnPos().setX(vec3d.x);
        this.getSpawnPos().setY(vec3d.y);
        this.getSpawnPos().setZ(vec3d.z);
        ConfigManager.saveDisableTheEndConfig(this);
    }

    public static class SpawnRotation {
        @SerializedName("x")
        private float x = 0;
        @SerializedName("y")
        private float y = 0;

        public void setY(float y) {
            this.y = y;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public float getX() {
            return x;
        }
    }

    public static class SpawnPos {
        @SerializedName("x")
        private double x = 0;
        @SerializedName("y")
        private double y = 0;
        @SerializedName("z")
        private double z = 0;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }

    public SpawnRotation getSpawnRotation() {
        return spawnRotation;
    }

    public SpawnPos getSpawnPos() {
        return spawnPos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ConfigManager.saveDisableTheEndConfig(this);
    }
}
