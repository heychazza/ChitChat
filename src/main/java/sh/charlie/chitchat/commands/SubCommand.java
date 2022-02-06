package sh.charlie.chitchat.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import sh.charlie.chitchat.ChitChatPlugin;

@RequiredArgsConstructor
@Getter
public abstract class SubCommand {

    @Getter(AccessLevel.NONE)
    protected final ChitChatPlugin plugin;
    private final String name;
    private final String permission;

    public abstract void execute(Player player, String[] args);

}
