package com.yueshi.netty.fakewx.protocol.response;

import com.yueshi.netty.fakewx.protocol.command.Command;
import com.yueshi.netty.fakewx.protocol.Packet;
import lombok.Data;

/**
 * @author: yuesh1 create: 2022-10-24 10:29
 */
@Data
public class LoginResponsePacket extends Packet {

	private boolean success;

	private String reason;

	@Override
	public Byte getCommand() {
		return Command.LOGIN_RESPONSE;
	}

}
