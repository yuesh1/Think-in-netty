package com.yueshi.netty.fakewx.protocol.request;

import com.yueshi.netty.fakewx.protocol.command.Command;
import com.yueshi.netty.fakewx.protocol.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: yuesh1 create: 2022-10-21 16:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestPacket extends Packet {

	private String message;

	@Override
	public Byte getCommand() {
		return Command.MESSAGE_REQUEST;
	}

}
