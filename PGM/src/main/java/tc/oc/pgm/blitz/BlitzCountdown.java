package tc.oc.pgm.blitz;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import tc.oc.commons.core.chat.Component;
import tc.oc.commons.core.chat.Components;
import tc.oc.commons.core.util.Comparables;
import tc.oc.pgm.countdowns.MatchCountdown;
import tc.oc.pgm.match.Match;

import java.time.Duration;

public class BlitzCountdown extends MatchCountdown {

    private final BlitzProperties properties;

    public BlitzCountdown(Match match, BlitzProperties properties) {
        super(match);
        this.properties = properties;
    }

    @Override
    public BaseComponent barText(Player viewer) {
        if(Comparables.greaterThan(remaining, Duration.ZERO)) {
            return new Component(
                new TranslatableComponent(
                    "blitz.countdown",
                    secondsRemaining(ChatColor.YELLOW)
                ),
                ChatColor.AQUA
            );
        } else {
            return Components.blank();
        }
    }

    @Override
    public void onEnd(Duration total) {
        super.onEnd(total);
        match.needMatchModule(BlitzMatchModuleImpl.class).activate(properties);
    }

}
