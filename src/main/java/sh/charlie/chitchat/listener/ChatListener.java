package sh.charlie.chitchat.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sh.charlie.chitchat.ChitChatPlugin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChatListener implements Listener {

    private final ChitChatPlugin plugin;

    public ChatListener(ChitChatPlugin plugin) {
        this.plugin = plugin;
    }

    private String replaceColors(String finalReplacement, String code) {
        finalReplacement = finalReplacement.replace(code + "l", "<bold>");
        finalReplacement = finalReplacement.replace(code + "o", "<italic>");
        finalReplacement = finalReplacement.replace(code + "r", "<reset>");

        finalReplacement = finalReplacement.replace(code + "a", "<green>");
        finalReplacement = finalReplacement.replace(code + "b", "<aqua>");
        finalReplacement = finalReplacement.replace(code + "c", "<red>");
        finalReplacement = finalReplacement.replace(code + "d", "<light_purple>");
        finalReplacement = finalReplacement.replace(code + "e", "<yellow>");
        finalReplacement = finalReplacement.replace(code + "f", "<white>");

        finalReplacement = finalReplacement.replace(code + "0", "<black>");
        finalReplacement = finalReplacement.replace(code + "1", "<dark_blue>");
        finalReplacement = finalReplacement.replace(code + "2", "<dark_green>");
        finalReplacement = finalReplacement.replace(code + "3", "<dark_aqua>");
        finalReplacement = finalReplacement.replace(code + "4", "<dark_red>");
        finalReplacement = finalReplacement.replace(code + "5", "<dark_purple>");
        finalReplacement = finalReplacement.replace(code + "6", "<gold>");
        finalReplacement = finalReplacement.replace(code + "7", "<gray>");
        finalReplacement = finalReplacement.replace(code + "8", "<dark_gray>");
        finalReplacement = finalReplacement.replace(code + "9", "<blue>");

        return finalReplacement;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        e.setCancelled(true);

        List<String> formats = plugin.getFormats().stream().sorted(Comparator.comparing(item -> plugin.getConfig().getInt("formats." + item + ".priority"))).toList();

        for (String format : formats) {
            if (!format.equals("default") && !player.hasPermission("chatformat." + format)) continue;

            StringBuilder miniStr = new StringBuilder();

            List<String> configKeys = Arrays.asList("channel", "prefix", "name", "suffix", "chat");

            for (String configKey : configKeys) {
                String text = plugin.getConfig().getString("formats." + format + "." + configKey);
                List<String> textTooltip = plugin.getConfig().getStringList("formats." + format + "." + configKey + "_tooltip");
                String textClickCommand = plugin.getConfig().getString("formats." + format + "." + configKey + "_click_command");

                if (textTooltip.size() > 0) {
                    miniStr.append("<hover:show_text:\"").append(String.join("\n", textTooltip)).append("\">");
                }

                if (textClickCommand != null && !textClickCommand.isEmpty()) {
                    miniStr.append("<click:suggest_command:").append(textClickCommand).append(">");
                }

                miniStr.append(text);

                if (textClickCommand != null && !textClickCommand.isEmpty()) {
                    miniStr.append("</click>");
                }

                if (textTooltip.size() > 0) {
                    miniStr.append("</hover>");
                }
            }

            MiniMessage miniMessage = plugin.getMiniMessage();

            miniStr = new StringBuilder(miniStr.toString().replace("%message%", miniMessage.escapeTags(e.getMessage())));

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                miniStr = new StringBuilder(PlaceholderAPI.setPlaceholders(e.getPlayer(), miniStr.toString()));
            }

            String finalReplacement = miniStr.toString();

            finalReplacement = replaceColors(finalReplacement, "&");
            finalReplacement = replaceColors(finalReplacement, "§");

            System.out.println(finalReplacement);
            plugin.getAdventure().all().sendMessage(miniMessage.deserialize(finalReplacement));
            break;
        }
    }
}
