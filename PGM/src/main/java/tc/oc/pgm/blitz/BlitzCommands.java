package tc.oc.pgm.blitz;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.SuggestException;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.command.CommandSender;
import tc.oc.commons.bukkit.commands.CommandUtils;
import tc.oc.commons.core.commands.Commands;
import tc.oc.pgm.match.Match;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;

public class BlitzCommands implements Commands {

    @Inject BlitzCommands() {}

    @Command(
        aliases = {"blitz"},
        desc = "Activates the blitz module before or during a match.",
        usage = "<lives> [delay] [type]",
        max = 3
    )
    @CommandPermissions("pgm.blitz")
    public List<String> blitz(CommandContext args, CommandSender sender) throws CommandException, SuggestException {
        final Match match = tc.oc.pgm.commands.CommandUtils.getMatch(sender);
        final BlitzMatchModule blitz = match.needMatchModule(BlitzMatchModuleImpl.class);
        if(blitz.activated()) {
            throw CommandUtils.newCommandException(sender, new TranslatableComponent("blitz.active"));
        } else if(match.countdowns().anyRunning(BlitzCountdown.class)) {
            throw CommandUtils.newCommandException(sender, new TranslatableComponent("blitz.queued"));
        } else {
            Duration delay = CommandUtils.getDuration(args, 1, match.hasStarted() ? Duration.ofSeconds(30) : Duration.ZERO);
            BlitzProperties properties = BlitzProperties.create(
                match,
                args.getInteger(0, 1),
                CommandUtils.getEnum(args, sender, 2, Lives.Type.class, Lives.Type.INDIVIDUAL)
            );
            match.countdowns().start(new BlitzCountdown(match, properties), delay);
        }
        return null;
    }

}
