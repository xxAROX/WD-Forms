package xxAROX.WDForms;

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolCodecs;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import xxAROX.WDForms.forms.types.ProxySettingsForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WDForms extends Plugin {
    private static WDForms instance;
    public static WDForms getInstance() {return instance;}
    final private static HashMap<ProxiedPlayer, FormPlayerSession> sessions = new HashMap<>();
    @Getter final private static List<ProxySettingsForm> globalSettings = new ArrayList<>();


    @Override public void onStartup() {
        instance = this;
    }
    @Override public void onEnable() {
        getProxy().getEventManager().subscribe(PlayerLoginEvent.class, (event -> {
            FormPlayerSession session = new FormPlayerSession(event.getPlayer());
            sessions.put(event.getPlayer(), session);
        }));
        ProtocolCodecs.addUpdater(new WDFormsProtocolUpdater());
    }
    public static FormPlayerSession getSession(ProxiedPlayer player){return sessions.getOrDefault(player, null);}
}
