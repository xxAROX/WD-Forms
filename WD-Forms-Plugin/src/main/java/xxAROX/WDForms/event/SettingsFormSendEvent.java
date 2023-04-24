package xxAROX.WDForms.event;


import dev.waterdog.waterdogpe.event.defaults.PlayerEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class SettingsFormSendEvent extends PlayerEvent {
    public SettingsFormSendEvent(ProxiedPlayer player) {
        super(player);
    }
}
