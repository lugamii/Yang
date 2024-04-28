// Decompiled with: CFR 0.152
// Class Version: 8
package me.tulio.yang.utilities.string;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.chat.Clickable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@Getter
public class MessageFormat {
    private String message;
    @Setter
    private List<String> messages;
    @Setter
    private HashMap<String, String> variables = Maps.newHashMap();
    @Setter
    private HashMap<String, Clickable> clickableVariable = Maps.newHashMap();

    public MessageFormat(Object object) {
        if (object instanceof List) {
            this.messages = (List)object;
        } else {
            this.message = (String)object;
        }
    }

    public void setMessage(Object object) {
        if (object instanceof List) {
            this.messages = (List)object;
        } else {
            this.message = (String)object;
        }
    }

    public MessageFormat add(String variable, String value) {
        this.variables.put(variable.toLowerCase(), value);
        return this;
    }

    public MessageFormat setClickable(String variable, Clickable clickable) {
        this.clickableVariable.put(variable.toLowerCase(), clickable);
        return this;
    }

    public void send(CommandSender sender) {
        if (this.messages != null) {
            this.messages.forEach(formatted -> {
                for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                    String variable = entry.getKey();
                    String value = entry.getValue();
                    if (this.clickableVariable.containsKey(variable)) {
                        return;
                    }
                    formatted = formatted.replace(variable, value);
                }
                sender.sendMessage(CC.translate(formatted));
            });
        } else if (this.message != null) {
            String formatted2 = this.message;
            for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                String variable = entry.getKey();
                String value = entry.getValue();
                formatted2 = formatted2.replace(variable, value);
            }
            sender.sendMessage(CC.translate(formatted2));
        }
    }

    public void broadcast() {
        if (this.message == null && this.messages != null) {
            this.messages.forEach(formatted -> {
                for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                    String variable = entry.getKey();
                    String value = entry.getValue();
                    formatted = formatted.replace(variable, value);
                }
                Bukkit.broadcastMessage(CC.translate(formatted));
            });
            return;
        }
        if (this.message != null) {
            String formatted2 = this.message;
            for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                String variable = entry.getKey();
                String value = entry.getValue();
                formatted2 = formatted2.replace(variable, value);
            }
            Bukkit.broadcastMessage(CC.translate(formatted2));
        }
    }

    public List<String> toList() {
        ArrayList<String> lines = Lists.newArrayList();
        if (this.messages != null) {
            this.messages.forEach(formatted -> {
                for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                    String variable = entry.getKey();
                    String value = entry.getValue();
                    formatted = formatted.replace(variable, value);
                }
                lines.add(CC.translate(formatted));
            });
        }
        return lines;
    }

    public String toString() {
        if (this.message != null) {
            String formatted = this.message;
            for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                String variable = entry.getKey();
                String value = entry.getValue();
                formatted = formatted.replace(variable, value);
            }
            return CC.translate(formatted);
        }
        return "";
    }

    private String getVariable(String string) {
        if (string == null) {
            return "";
        }
        if (string.contains("{")) {
            StringBuilder variable = new StringBuilder();
            boolean add = false;
            for (char s : string.toCharArray()) {
                if (s == '{') {
                    add = true;
                }
                if (s == '}') {
                    variable.append(s);
                    break;
                }
                if (!add) continue;
                variable.append(s);
            }
            return variable.toString();
        }
        return string;
    }

    public MessageFormat() {

    }
}
