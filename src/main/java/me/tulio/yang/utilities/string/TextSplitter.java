// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.string;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class TextSplitter
{
    public static List<String> split(final int length, final List<String> lines, final String linePrefix, final String wordSuffix) {
        final StringBuilder builder = new StringBuilder();
        for (final String line : lines) {
            builder.append(line.trim());
            builder.append(" ");
        }
        return split(length, builder.substring(0, builder.length() - 1), linePrefix, wordSuffix);
    }
    
    public static List<String> split(final int length, final String text, final String linePrefix, final String wordSuffix) {
        if (text.length() <= length) {
            return Lists.newArrayList(linePrefix + text);
        }
        final List<String> lines = new ArrayList<String>();
        final String[] split = text.split(" ");
        StringBuilder builder = new StringBuilder(linePrefix);
        for (int i = 0; i < split.length; ++i) {
            if (builder.length() + split[i].length() >= length) {
                lines.add(builder.toString());
                builder = new StringBuilder(linePrefix);
            }
            builder.append(split[i]);
            builder.append(wordSuffix);
            if (i == split.length - 1) {
                builder.replace(builder.length() - wordSuffix.length(), builder.length(), "");
            }
        }
        if (builder.length() != 0) {
            lines.add(builder.toString());
        }
        return lines;
    }
}
