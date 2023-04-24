package xxAROX.WDForms;

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolCodecs;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import xxAROX.WDForms.forms.types.ProxySettingsForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class WDForms extends Plugin {
    private static WDForms instance;
    public static WDForms getInstance() {return instance;}
    final private static HashMap<ProxiedPlayer, FormPlayerSession> sessions = new HashMap<>();
    @Getter private static long settings_ticker_id;
    @Getter final private static List<Function<ProxiedPlayer, ProxySettingsForm.ProxySettingsFormBuilder>> globalSettings = new ArrayList<>();


    @Override public void onStartup() {
        instance = this;
        settings_ticker_id = RandomUtils.nextLong() /1000 *1000;
        saveResource("config.yml");
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
