package com.yueshi.netty.fakewx.socket.packet;

import com.yueshi.netty.fakewx.socket.command.Command;
import lombok.Data;

/**
 * @author: yuesh1 create: 2022-10-21 11:17
 */
@Data
public class LoginRequestPacket extends Packet {

	private Integer userId;

	private String username;

	private String pwd;

	@Override
	public Byte getCommand() {
		return Command.LOGIN_REQUEST;
	}

}
