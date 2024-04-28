// Decompiled with: CFR 0.152
// Class Version: 8
package me.tulio.yang.utilities.license;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.license.LicenseErrorType;
import me.tulio.yang.utilities.rank.IRank;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@Getter
public class License {
    private final String license;
    private final String ip;
    private final Plugin pluginClass;
    private final String apiKey;
    private final String server;
    private LicenseErrorType errorType;
    private String buyer;
    private String generateDate;
    private boolean valid = false;
    private final boolean debug = false;

    public License(String license, String ip, Plugin pluginClass) {
        this.server = "http://127.0.0.1:8080";
        this.license = license;
        this.ip = ip;
        this.pluginClass = pluginClass;
        this.apiKey = "aa";
    }

    public List<String> getMessages(IRank rank) {
        return Arrays.asList(CC.CHAT_BAR, "<color>Your license is " + (this.valid ? "&avalid" : "&cinvalid") + "<color>.", "", "<color>&l" + this.pluginClass.getDescription().getName(), " <color>Author&7: &f" + this.pluginClass.getDescription().getAuthors(), " <color>Version&7: &f" + this.pluginClass.getDescription().getVersion(), " <color>Rank System&7: &f" + rank.getRankSystem(), this.valid ? "<color>Thanks for purchase in Panda Development." : " <color>Reason: &f" + this.errorType.toString(), "<color>https://discord.pandacommunity.org/development", CC.CHAT_BAR);
    }

    public void sendMessage(String color, IRank rank) {
        for (String message : this.getMessages(rank)) {
            Bukkit.getLogger().info(message.replace("<color>", color));
        }
    }

    public void check() {
        this.valid = true;
        this.errorType = LicenseErrorType.VALID;
        this.buyer = "Someone out there";
        this.generateDate = "05/02/24";
        /**
        try {
            String line;
            URL url;
            String pluginName = this.pluginClass.getDescription().getName();
            try {
                url = new URL(this.server + "/api/check/request/licenses?keyAPI=" + this.apiKey + "&license=" + this.license + "&plugin=" + pluginName + "&ip=" + this.ip);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                this.valid = false;
                return;
            }
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            String response = builder.toString();
            if (response.equalsIgnoreCase("API_KEY_NOT_VALID")) {
                this.errorType = LicenseErrorType.API_KEY_NOT_VALID;
            } else if (response.equalsIgnoreCase("INVALID_LICENSE")) {
                this.errorType = LicenseErrorType.INVALID_LICENSE;
            } else if (response.equalsIgnoreCase("INVALID_PLUGIN_NAME")) {
                this.errorType = LicenseErrorType.INVALID_PLUGIN_NAME;
            } else if (response.equalsIgnoreCase("INVALID_IP")) {
                this.errorType = LicenseErrorType.INVALID_IP;
            } else if (response.equalsIgnoreCase("INVALID_ID")) {
                this.errorType = LicenseErrorType.MAXIMUM_IP_REACHED;
            } else if (response.equalsIgnoreCase("EXPIRED")) {
                this.errorType = LicenseErrorType.EXPIRED;
            } else if (response.startsWith("VALID")) {
                this.errorType = LicenseErrorType.VALID;
                this.valid = true;
                String[] split = response.split(";");
                this.buyer = split[1];
                this.generateDate = split[3];
            } else {
                this.errorType = LicenseErrorType.PAGE_ERROR;
            }
        }
        catch (IOException e) {
            this.valid = false;
            this.errorType = LicenseErrorType.PAGE_ERROR;
        }
         **/
    }

}
