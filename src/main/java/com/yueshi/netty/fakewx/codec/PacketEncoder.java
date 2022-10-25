package com.yueshi.netty.fakewx.codec;

import com.yueshi.netty.fakewx.protocol.Packet;
import com.yueshi.netty.fakewx.codec.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: yuesh1 create: 2022-10-24 15:27
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
		PacketCodeC.INSTANCE.encode(msg, out);
	}

}
