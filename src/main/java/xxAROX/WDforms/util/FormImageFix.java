package xxAROX.WDforms.util;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.Task;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.packet.NetworkStackLatencyPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

final public class FormImageFix {
    private final static HashMap<ProxiedPlayer, HashMap<Long, Runnable>> callbacks = new HashMap<>();

    public static void receiveNetworkStackLatency(ProxiedPlayer player, NetworkStackLatencyPacket packet){
        if (callbacks.containsKey(player) && callbacks.get(player).containsKey(packet.getTimestamp())) {
            Runnable cb = callbacks.get(player).remove(packet.getTimestamp());
            if (callbacks.get(player).size() == 0) callbacks.remove(player);
            cb.run();
        }
    }
    public static void fixModalFormRequest(ProxiedPlayer player){
        ProxyServer.getInstance().getScheduler().scheduleDelayed(() -> {
            if (player.isConnected()) {
                onSendPacket(player, () -> {
                    if (player.isConnected()) {
                        AtomicReference<Integer> times = new AtomicReference<>(5); // send for up to 5 x 10 ticks (or 2500ms)
                        ProxyServer.getInstance().getScheduler().scheduleRepeating(new Task() {
                            @Override public void onRun(int i) {
                                times.set(times.get() - 1);
                                if (times.get() >= 0 && player.isConnected()) {
                                    UpdateAttributesPacket pk = new UpdateAttributesPacket();
                                    List<AttributeData> attrs = new ArrayList<>();
                                    attrs.add(new AttributeData("minecraft:player.experience", 0.0f, 1.0f, 0));
                                    pk.setAttributes(attrs);
                                    pk.setTick(0);
                                    return;
                                }
                                cancel();
                            }
                            @Override public void onCancel() {}
                        }, 10);
                    }
                });
            }
        }, 1);
    }
    private static void onSendPacket(ProxiedPlayer player, Runnable callback){
        long ts = (long) (Math.random() * 1000);
        NetworkStackLatencyPacket packet = new NetworkStackLatencyPacket();
        packet.setFromServer(true);
        packet.setTimestamp(ts);
        player.sendPacketImmediately(packet);
        if (!callbacks.containsKey(player)) callbacks.put(player, new HashMap<>());
        callbacks.get(player).put(ts, callback);

    }
}
