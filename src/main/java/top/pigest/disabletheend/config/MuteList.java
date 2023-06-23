package top.pigest.disabletheend.config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MuteList {
    @SerializedName("mutes")
    private List<MuteEntry> muteEntries = new ArrayList<>();

    public static class MuteEntry {
        @SerializedName("uuid")
        private String UUID;

        @SerializedName("reason")
        private String reason;

        @SerializedName("expiry")
        private String expiry;

        public String getExpiry() {
            return expiry;
        }

        public String getReason() {
            return reason;
        }

        public String getUUID() {
            return UUID;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }
    }

    public List<MuteEntry> getMuteEntries() {
        return muteEntries;
    }
}
