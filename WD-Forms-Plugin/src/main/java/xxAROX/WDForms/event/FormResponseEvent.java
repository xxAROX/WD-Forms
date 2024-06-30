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

package xxAROX.WDForms.event;

import dev.waterdog.waterdogpe.event.CancellableEvent;
import dev.waterdog.waterdogpe.event.Event;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;

@AllArgsConstructor
public class FormResponseEvent extends Event implements CancellableEvent {
    @Getter protected ProxiedPlayer player;
    @Getter protected ModalFormResponsePacket formResponsePacket;
}
