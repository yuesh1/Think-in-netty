package com.yueshi.netty.fakewx.server;

import com.yueshi.netty.fakewx.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author: yuesh1 create: 2022-10-24 15:23
 */
public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		out.add(PacketCodeC.INSTANCE.decode(in));
	}

}
