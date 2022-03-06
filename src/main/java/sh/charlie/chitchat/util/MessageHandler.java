package sh.charlie.chitchat.util;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import sh.charlie.chitchat.ChitChatPlugin;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class MessageHandler {
    private static ChitChatPlugin plugin = JavaPlugin.getPlugin(ChitChatPlugin.class);

    /**
     * Deserialize Legacy Ampersand Messages
     * @param message Ampersand Message
     * @return {@link Component}
     */
    public static Component deserializeAmpersand(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    /**
     * Deserialize a MiniMessage message
     * Serialized Messages use MiniMessage
     * @param message MiniMessage serialized message
     * @return {@link Component}
     */
    public static Component deserialize(String message) {
        return plugin.getMiniMessage().deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Deserializes an Ampersand message into a String
     * This has been marked Deprecated as Kyori Components should be used where possible for future
     * releases of Minecraft, where Paper uses Kyori by default.
     * @param message Serialized Ampersand Message
     * @return {@link ChatColor#translateAlternateColorCodes(char, String)} serialised string
     */
    @Deprecated
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Send a Component to a Players' Chat
     * @param player Player
     * @param message Chat Component
     */
    public static void message(Player player, Component message) {
        player.sendMessage(message);
    }

    /**
     * Send a Component to a Players' Chat
     * @param sender Player
     * @param message Chat Component
     */
    public static void message(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    /**
     * Send a Component to every player on the server
     * @param message Chat Component
     */
    public static void broadcast(Component message) {
        Bukkit.broadcast(message, "");
    }

    /**
     * Send a MiniMessage serialized message to a player
     * @param player Player
     * @param message MiniMessage serialized message
     */
    public static void message(Player player, String message) {
        player.sendMessage(deserialize(message));
    }

    /**
     * Send a MiniMessage serialized message to a player
     * @param player Player
     * @param placeholders Varargs for MiniMessage parse replacements
     * @param message MiniMessage serialized message
     */
    public static void message(Player player, String message, Object... placeholders) {
        player.sendMessage(parse(message, placeholders));
    }

    /**
     * Send a MiniMessage serialized message to a player
     * @param sender Player
     * @param message MiniMessage serialized message
     */
    public static void message(CommandSender sender, String message) {
        sender.sendMessage(deserialize(message));
    }

    /**
     * Send a MiniMessage serialized message to a player
     * @param sender CommandSender
     * @param placeholders Varargs for MiniMessage parse replacements
     * @param message MiniMessage serialized message
     */
    public static void message(CommandSender sender, String message, Object... placeholders) {
        sender.sendMessage(parse(message, placeholders));
    }

    /**
     * Send a MiniMessage serialized message to the entire server
     * @param message Serialized Message
     */
    public static void broadcast(String message) {
        Bukkit.broadcast(deserialize(message), "");
    }

    public static Title buildTitle(String title, String subtitle) {
        return buildTitle(
                parse(title),
                parse(subtitle)
        );
    }

    /**
     * Build a title.
     *
     * @param title        The title of the title
     * @param subtitle     The subtitle of the title
     * @param placeholders Placeholders to be replaced in the title/subtitle
     * @return The built title.
     */
    public static Title buildTitle(String title, String subtitle, Object... placeholders) {
        return buildTitle(
                parse(title, placeholders),
                parse(subtitle, placeholders),
                5,
                40,
                5
        );
    }

    /**
     * Build a title.
     *
     * @param title        The title of the title
     * @param subtitle     The subtitle of the title
     * @param fadeInTicks  How long the title will take to fade in in ticks
     * @param stayTicks    How long the title should stay in ticks
     * @param fadeOutTicks How long the title will take to fade out in ticks
     * @param placeholders Placeholders to be replaced in the title/subtitle
     * @return The built title.
     */
    public static Title buildTitle(String title, String subtitle, long fadeInTicks, long stayTicks, long fadeOutTicks, Object... placeholders) {
        return buildTitle(
                parse(title, placeholders),
                parse(subtitle, placeholders),
                fadeInTicks,
                stayTicks,
                fadeOutTicks
        );
    }

    /**
     * Build a title.
     *
     * @param title    The title of the title
     * @param subtitle The subtitle of the title
     * @return The built title
     */
    public static Title buildTitle(Component title, Component subtitle) {
        return buildTitle(title, subtitle, 5, 40, 5);
    }

    /**
     * Build a title.
     *
     * @param title        The title of the title
     * @param subtitle     The subtitle of the title
     * @param fadeInTicks  How long (in ticks) to
     * @param stayTicks    How long the title should stay in ticks
     * @param fadeOutTicks How long the title will take to fade out in ticks
     * @return The built title
     */
    public static Title buildTitle(Component title, Component subtitle, long fadeInTicks, long stayTicks, long fadeOutTicks) {
        //noinspection UnstableApiUsage
        return Title.title(title, subtitle, Title.Times.of(
                Duration.ofMillis(fadeInTicks * 50),
                Duration.ofMillis(stayTicks * 50),
                Duration.ofMillis(fadeOutTicks * 50)
        ));
    }

    /**
     * Parse MiniMessage markdown into a component. This should be used rather than whenever
     * there are placeholders which are of non-String types. This allows cleaner code in implementation,
     * as it removes the need for all values to be a String.
     *
     * @param text         The MiniMessage syntax
     * @param placeholders A key, value array of placeholders.
     * @return The formatted component
     */
    public static Component parse(String text, @Nullable Object... placeholders) {
        if (placeholders.length > 0) {

            if(placeholders.length % 2 != 0) {
                throw new IllegalStateException("Placeholders Must be in a key: replacement order, found missing value!");
            }

            final TagResolver.Builder TAG_BUILDER = TagResolver.builder();

            for(int i = 0; i < placeholders.length; i += 2) {

                String key = String.valueOf(placeholders[i]);
                Component value = deserialize(String.valueOf(placeholders[i + 1]));

                TAG_BUILDER.tag(key, Tag.selfClosingInserting(value));

            }

            return MiniMessage.miniMessage().deserialize(text, TAG_BUILDER.build()).decoration(TextDecoration.ITALIC, false);
        }

        return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> parse(List<String> text, @Nullable Object... placeholders) {
        List<Component> result = Lists.newArrayList();
        for(String str : text) {
            result.add(parse(str, placeholders));
        }
        return result;
    }

    public static Component parse(String text) {
        return deserialize(text);
    }

    public static String replaceLegacyCodes(String content) {
        return replaceSpecificLegacyCodes(content, LegacyChatColor.values());
    }

    public static String replaceLegacyColourCodes(String content) {
        return replaceSpecificLegacyCodes(content, LegacyChatColor.getColours());
    }

    /** thnx phil **/
    public static String replaceSpecificLegacyCodes(String content, LegacyChatColor... legacyChatColors) {
        for (LegacyChatColor color : legacyChatColors) {
            content = content.replace(
                    String.format("&%s", color.getCode()),
                    String.format("<%s>", color.name())
            ).replace(
                    String.format("§%s", color.getCode()),
                    String.format("<%s>", color.name())
            );
        }

        return plugin.convertHex(content);
    }

    /**
     * fuck placeholderapi
     * @param player
     * @param component
     * @return
     */
    public static Component replacePlaceholderApiPlaceholders(Player player, Component component) {

        final String HEX_PATTERN_STRING = "#(?:[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})";
        Pattern pattern = PlaceholderAPI.getPlaceholderPattern();

        TextReplacementConfig replacementConfig = TextReplacementConfig.builder().match(pattern).replacement(((matchResult, builder) -> {
            String placeholder = matchResult.group(0);
            String replaced = PlaceholderAPI.setPlaceholders(player, placeholder);

            return parse(replaceLegacyCodes(replaced));
        })).build();

        return component.replaceText(replacementConfig);
    }

    /**
     * Deserialize a List of Strings to Kyori Components
     * @param lore Serialized Lore
     * @return List of Components
     */
    public static List<Component> deserialize(List<String> lore) {
        List<Component> list = Lists.newArrayList();

        for(String s : lore) {
            list.add(deserialize(s));
        }

        return list;
    }

    /** thnx phil **/
    public enum LegacyChatColor {
        AQUA("b"),
        DARK_GREEN("2"),
        BLACK("0"),
        BLUE("9"),
        BOLD("l"),
        DARK_AQUA("3"),
        DARK_BLUE("1"),
        DARK_GRAY("8"),
        DARK_PURPLE("5"),
        DARK_RED("4"),
        GOLD("6"),
        GRAY("7"),
        GREEN("a"),
        ITALIC("o"),
        LIGHT_PURPLE("d"),
        OBFUSCATED("k"),
        RED("c"),
        RESET("r"),
        STRIKETHROUGH("m"),
        UNDERLINED("n"),
        WHITE("f"),
        YELLOW("e");

        private final String color;

        /**
         * Constructor for the Enum.
         *
         * @param color the legacy colour code
         */
        LegacyChatColor(String color) {
            this.color = color;
        }

        private static final LegacyChatColor[] COLORS = new LegacyChatColor[]{
                AQUA,
                DARK_GREEN,
                BLACK, BLUE,
                DARK_AQUA,
                DARK_BLUE,
                DARK_GRAY,
                DARK_PURPLE,
                DARK_RED,
                GOLD,
                GRAY,
                GREEN,
                LIGHT_PURPLE,
                RED, WHITE,
                YELLOW,
                BOLD
        };

        /**
         * Get an array of all COLORS (non formatting codes)
         *
         * @return the array of all colors
         */
        public static LegacyChatColor[] getColours() {
            return COLORS;
        }

        /**
         * Get an array of all colors + bold (all codes allowed for nicknames)
         *
         * @return the array of all colors + bold
         */
        public static LegacyChatColor[] getNicknameCodes() {
            return COLORS;
        }


        /**
         * Get the legacy colour code.
         *
         * @return the legacy colour code
         */
        public String getCode() {
            return this.color;
        }
    }

    public static String getNiceEnum(Enum<?> enumType) {
        return WordUtils.capitalize(enumType.toString().toLowerCase().replace("_", " "));
    }

}
