/*
 * Copyright (c) Jan Sohn aka. xxAROX
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package xxAROX.WDForms;

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolCodecs;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import xxAROX.WDForms.event.FormSendEvent;
import xxAROX.WDForms.forms.types.ProxySettingsForm;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class WDForms extends Plugin {
    private static WDForms instance;
    public static WDForms getInstance() {return instance;}
    final private static HashMap<ProxiedPlayer, FormPlayerSession> sessions = new HashMap<>();
    @Getter private static long settings_ticker_id;
    @Getter private static final List<Function<ProxiedPlayer, ProxySettingsForm.ProxySettingsFormBuilder>> globalSettings = new ArrayList<>();
    private static final Map<String, BiFunction<ProxiedPlayer, String, String>> translators = new HashMap<>();


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

        getProxy().getEventManager().subscribe(FormSendEvent.class, event -> {
            String formData = event.getFormRequestPacket().getFormData();
            for (BiFunction<ProxiedPlayer, String, String> handler : translators.values()) formData = handler.apply(event.getPlayer(), formData);
            event.getFormRequestPacket().setFormData(formData);
        });
    }

    public final String registerTranslator(BiFunction<ProxiedPlayer, String, String> handler){
        String id = UUID.randomUUID().toString();
        translators.put(id, handler);
        return id;
    }
    public static FormPlayerSession getSession(ProxiedPlayer player){return sessions.getOrDefault(player, null);}
}
