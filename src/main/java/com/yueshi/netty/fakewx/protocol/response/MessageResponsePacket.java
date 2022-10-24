package com.yueshi.netty.fakewx.protocol.response;

import com.yueshi.netty.fakewx.protocol.command.Command;
import com.yueshi.netty.fakewx.protocol.Packet;
import lombok.Data;

/**
 * @author: yuesh1 create: 2022-10-21 16:37
 */
@Data
public class MessageResponsePacket extends Packet {

	private String message;

	@Override
	public Byte getCommand() {
		return Command.MESSAGE_RESPONSE;
	}

}
