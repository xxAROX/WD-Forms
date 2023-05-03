package xxAROX.WDForms.utils.autoback;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xxAROX.WDForms.FormPlayerSession;
import xxAROX.WDForms.forms.types.Form;
import xxAROX.WDForms.forms.types.MenuForm;
import xxAROX.WDForms.forms.types.ModalForm;
import xxAROX.WDForms.utils.autoback.buttons.BackButton;
import xxAROX.WDForms.utils.autoback.buttons.CloseButton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
public final class AutoBack {
    @Getter @Setter private static boolean enabled = false;
    @Setter private static int expireTicks = 8;
    @Setter private static CloseButton closeButton = CloseButton.DEFAULT;
    private final static HashMap<String, AutoBackEntry> entries = new HashMap<>();

    public static void handleForm(FormPlayerSession session, int formId, Form<?> form){
        long current_tick = session.getPlayer().getProxy().getCurrentTick();
        Set<String> entries_to_remove = new HashSet<>();
        entries.keySet().stream().filter(xuid -> current_tick >= entries.get(xuid).getExpireTick()).forEach(entries_to_remove::add);
        if (entries_to_remove.size() > 0) {
            entries_to_remove.forEach(entries::remove);
            entries_to_remove.clear();
        }
        AutoBackEntry entry = entries.getOrDefault(session.getPlayer().getXuid(), null);
        if (entry != null && !entry.canGoBack(form)) entries.remove(session.getPlayer().getXuid());
        entry = entries.getOrDefault(session.getPlayer().getXuid(), null);
        if (form instanceof MenuForm menuForm && entry == null) menuForm.setButtons(menuForm.getButtons().stream().map(button -> (button instanceof BackButton ? closeButton : button)).toList());
        session.getPlayer().getProxy().getScheduler().scheduleDelayed(() -> {
            if (session.getPlayer() == null || !session.getPlayer().isConnected()) return;
            Form<?> applyTo = session.getForms().getOrDefault(formId, null);
            if (applyTo != null) applyAutoBack(session, applyTo);
        }, 1);
    }

    public static void cacheForm(ProxiedPlayer player, Form<?> form){
        if (!enabled) return;
        long tick = player.getProxy().getCurrentTick() +expireTicks;
        if (entries.containsKey(player.getXuid())) entries.get(player.getXuid()).setPreviousForm(form).setExpireTick(tick);
        else entries.put(player.getXuid(), new AutoBackEntry(tick, form));
    }

    private static void applyAutoBack(FormPlayerSession session, Form<?> form){
        if (form instanceof ModalForm) return;
        AutoBackEntry entry = entries.getOrDefault(session.getPlayer().getXuid(), null);
        if (entry != null) {
            Form<?> previous_form = entry.getPreviousForm();
            if (form instanceof MenuForm) ((MenuForm) form).getButtons().stream().filter(button -> button instanceof BackButton && (button.getOnClick() == null && button.getOnClickPlayer() == null)).forEach(button -> button.setOnClickPlayer((btn, player) -> previous_form.sendTo(player)));
            if (form.getOnClose() == null) {
                form.setOnClosePlayer(previous_form::sendTo);
            }
        }
    }
}
