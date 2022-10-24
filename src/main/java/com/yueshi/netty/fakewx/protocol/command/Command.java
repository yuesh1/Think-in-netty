package com.yueshi.netty.fakewx.protocol.command;

/**
 * 指令
 *
 * @author: yuesh1 create: 2022-10-21 11:16
 */
public interface Command {

	Byte LOGIN_REQUEST = 1;

	Byte LOGIN_RESPONSE = 2;

	Byte MESSAGE_REQUEST = 3;

	Byte MESSAGE_RESPONSE = 4;

}
