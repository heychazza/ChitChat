package sh.charlie.chitchat;

import sh.charlie.chitchat.util.Kyorifier;

public class TestKyoify {

    public static void main(String[] args) {
        String msg = "<hover:show_text:\"\"><click:suggest_command:/msg iLemon ></click></hover><hover:show_text:\"\uD873\uDF0B§4 <GRAY>iLemon\n" +
                "<DARK_GRAY>▎ <WHITE>Money: <GREEN><BOLD>$<RESET><GREEN>0\n" +
                "<DARK_GRAY>▎ <WHITE>Lemons: <YELLOW>47.88\n" +
                "<DARK_GRAY>▎ <WHITE>Partner: <LIGHT_PURPLE>not married\n" +
                "<DARK_GRAY>(Click to msg player...)\"><click:suggest_command:/msg iLemon ><DARK_GRAY>[<#aa0000><b>Owner</b><DARK_GRAY>] </click></hover><hover:show_text:\"\uD873\uDF0B§4 <GRAY>iLemon\n" +
                "<DARK_GRAY>▎ <WHITE>Money: <GREEN><BOLD>$<RESET><GREEN>0\n" +
                "<DARK_GRAY>▎ <WHITE>Lemons: <YELLOW>47.88\n" +
                "<DARK_GRAY>▎ <WHITE>Partner: <LIGHT_PURPLE>not married\n" +
                "<DARK_GRAY>(Click to msg player...)\"><click:suggest_command:/msg iLemon ><#aa0000>§4i§x§b§8§0§e§0§eL§x§c§6§1§c§1§ce§x§d§5§2§b§2§bm§x§e§3§3§9§3§9o§x§f§1§4§7§4§7n</click></hover><hover:show_text:\"\uD873\uDF0B§4 <GRAY>iLemon\n" +
                "<DARK_GRAY>▎ <WHITE>Money: <GREEN><BOLD>$<RESET><GREEN>0\n" +
                "<DARK_GRAY>▎ <WHITE>Lemons: <YELLOW>47.88\n" +
                "<DARK_GRAY>▎ <WHITE>Partner: <LIGHT_PURPLE>not married\n" +
                "<DARK_GRAY>(Click to msg player...)\"><click:suggest_command:/msg iLemon >§8 [§c(っ◔◡◔)っ §c§k*§r§c♥§c§k*§r§8]<GRAY>: </click></hover><WHITE><WHITE><#ffb3b3>Hello .</#ffb3b3><!italic><white>» </white></!italic><white><hover:show_item:stone>Stone</hover></white><gray> (x1)</gray><!italic> <white>«</white></!italic>. Hello";

        System.out.println(Kyorifier.kyorify(msg));
    }
}
