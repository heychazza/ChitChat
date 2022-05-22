package sh.charlie.chitchat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author https://github.com/lucypoulton/papi-kyorify
 */
public final class Kyorifier {
    private static final Map<Character, String> COLOURS = new HashMap<>(15);
    private static final Map<Character, String> FORMATTERS = new HashMap<>(6);
    private static final Pattern pattern = Pattern.compile("&(?<code>[\\da-fk-or])|[&{\\[<]?[#x](?<hex>(&?(?<!\\/#)[A-Fa-f\\d]){6})[}\\]>]?");

    static {
        COLOURS.put('0', "black");
        COLOURS.put('1', "dark_blue");
        COLOURS.put('2', "dark_green");
        COLOURS.put('3', "dark_aqua");
        COLOURS.put('4', "dark_red");
        COLOURS.put('5', "dark_purple");
        COLOURS.put('6', "gold");
        COLOURS.put('7', "gray");
        COLOURS.put('8', "dark_gray");
        COLOURS.put('9', "blue");
        COLOURS.put('a', "green");
        COLOURS.put('b', "aqua");
        COLOURS.put('c', "red");
        COLOURS.put('d', "light_purple");
        COLOURS.put('e', "yellow");
        COLOURS.put('f', "white");

        FORMATTERS.put('k', "obfuscated");
        FORMATTERS.put('l', "bold");
        FORMATTERS.put('m', "strikethrough");
        FORMATTERS.put('n', "underlined");
        FORMATTERS.put('o', "italic");
        FORMATTERS.put('r', "reset");
    }

    public static String kyorify(String input) {
        final AtomicBoolean activeFormatters = new AtomicBoolean(false);

        List<String> appliedFormats = new ArrayList<>();

        return pattern.matcher(input.replace("§", "&")).replaceAll(result -> {
            final Matcher matcher = (Matcher) result;
            final var hex = matcher.group("hex");
            final var code = matcher.group("code");
            var colour = hex == null ? COLOURS.get(code.charAt(0)) : "#" + hex.replace("&", "");

            if (colour == null) {
                // If there is no color, it means there is a formatter.
                final var formatter = FORMATTERS.get(code.charAt(0));
                activeFormatters.set(!formatter.equals("reset"));

                var format = !formatter.equals("reset") ? "<" + formatter + ">" : "";

                if(!formatter.equals("reset")) {
                    appliedFormats.add(formatter);
                }

                return format;
            } else {
                var formatMsg = "";
                if(appliedFormats.size() > 0) {
                    for (String appliedFormat : appliedFormats) {
                        formatMsg += "<!" + appliedFormat + ">";
                    }
                }

                var out = formatMsg + "<" + colour + ">";

                activeFormatters.set(false);
                appliedFormats.clear();
                return out;
            }
        });
    }
}