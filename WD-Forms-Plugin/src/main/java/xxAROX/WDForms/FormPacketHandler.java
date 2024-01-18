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

import dev.waterdog.waterdogpe.network.PacketDirection;
import dev.waterdog.waterdogpe.network.protocol.handler.PluginPacketHandler;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;

import java.util.List;

public record FormPacketHandler(FormPlayerSession session) implements PluginPacketHandler {
    @Override public PacketSignal handlePacket(BedrockPacket packet, PacketDirection direction) {
        if (direction.equals(PacketDirection.FROM_USER)) {
            if (packet instanceof NetworkStackLatencyPacket networkStackLatencyPacket) {
                if (networkStackLatencyPacket.getTimestamp() == WDForms.getSettings_ticker_id()) {
                    return session.handleProxySettings_response(session.getPlayer());
                }// else session.handleImageFix_response(networkStackLatencyPacket);
            }
            else if (packet instanceof ModalFormResponsePacket modalFormResponsePacket) return modalFormResponsePacket.getFormId() < 0 ? session.response(modalFormResponsePacket) : PacketSignal.UNHANDLED;
            else if (packet instanceof ServerSettingsResponsePacket serverSettingsResponsePacket) return serverSettingsResponsePacket.getFormId() < 0 ? session.response(serverSettingsResponsePacket) : PacketSignal.UNHANDLED;
            else if (packet instanceof ServerSettingsRequestPacket) session.handleProxySettings_request(session.getPlayer());
        } else {
            if (packet instanceof UpdateAttributesPacket updateAttributesPacket) {
                session.setEntityRuntimeId(updateAttributesPacket.getRuntimeEntityId());
                List<AttributeData> attributes = updateAttributesPacket.getAttributes().stream().filter(attributeData -> attributeData.getName().equals("minecraft:player.level")).toList();
                if (attributes.size() == 1) session.setLevel(attributes.get(0).getValue());
            }
        }
        return PacketSignal.UNHANDLED;
    }
}
