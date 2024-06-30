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

import dev.waterdog.waterdogpe.network.protocol.updaters.ProtocolCodecUpdater;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.cloudburstmc.protocol.bedrock.data.PacketRecipient;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;

public class WDFormsProtocolUpdater implements ProtocolCodecUpdater {
    @Override public BedrockCodec.Builder updateCodec(BedrockCodec.Builder builder, BedrockCodec baseCodec) {
        BedrockPacketDefinition<ModalFormResponsePacket> modalFormResponseDefinition = baseCodec.getPacketDefinition(ModalFormResponsePacket.class);
        BedrockPacketDefinition<ModalFormRequestPacket> modalFormRequestDefinition = baseCodec.getPacketDefinition(ModalFormRequestPacket.class);
        BedrockPacketDefinition<ServerSettingsRequestPacket> serverSettingsRequestDefinition = baseCodec.getPacketDefinition(ServerSettingsRequestPacket.class);
        BedrockPacketDefinition<ServerSettingsResponsePacket> serverSettingsResponseDefinition = baseCodec.getPacketDefinition(ServerSettingsResponsePacket.class);
        builder.registerPacket(ModalFormResponsePacket::new, modalFormResponseDefinition.getSerializer(), modalFormResponseDefinition.getId(), PacketRecipient.BOTH);               // SERVER
        builder.registerPacket(ModalFormRequestPacket::new, modalFormRequestDefinition.getSerializer(), modalFormRequestDefinition.getId(), PacketRecipient.BOTH);                  // CLIENT
        builder.registerPacket(ServerSettingsRequestPacket::new, serverSettingsRequestDefinition.getSerializer(), serverSettingsRequestDefinition.getId(), PacketRecipient.BOTH);   // CLIENT
        builder.registerPacket(ServerSettingsResponsePacket::new, serverSettingsResponseDefinition.getSerializer(), serverSettingsResponseDefinition.getId(), PacketRecipient.BOTH);// SERVER
        return builder;
    }
}
