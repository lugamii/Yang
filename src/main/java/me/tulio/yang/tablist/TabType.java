package me.tulio.yang.tablist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TabType {

    CUSTOM("Normal"),
    WEIGHT("Weight");

    public final String format;

    public static TabType getByName(String name) {
        for (TabType type : values()) {
            if (type.format.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
