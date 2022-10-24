package com.yueshi.netty.fakewx.client;

import com.yueshi.netty.fakewx.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;

/**
 * @author: yuesh1 create: 2022-10-24 15:52
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) throws Exception {

		System.out.println(new Date() + "receive Server message, message is [ " + msg.getMessage() + " ]");
	}

}
