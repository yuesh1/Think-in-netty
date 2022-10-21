package com.yueshi.netty.fakewx.handler;

import com.yueshi.netty.fakewx.socket.command.Command;
import com.yueshi.netty.fakewx.socket.packet.LoginRequestPacket;
import com.yueshi.netty.fakewx.socket.packet.Packet;
import com.yueshi.netty.fakewx.socket.serializer.JSONSerializer;
import com.yueshi.netty.fakewx.socket.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author: yuesh1 create: 2022-10-21 11:42
 */
@Data
public class PocketCodeC {

	private int MAGIC_NUMBER = 0x12345678;

	private static final Map<Byte, Class<? extends Packet>> packetTypeMap;

	private static final Map<Byte, Serializer> serializerMap;

	static {
		packetTypeMap = new HashMap<>();
		packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);

		serializerMap = new HashMap<>();
		Serializer serializer = new JSONSerializer();
		serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
	}

	public ByteBuf encode(Packet packet) {
		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
		byte[] bytes = Serializer.DEFAULT.serializer(packet);

		byteBuf.writeInt(MAGIC_NUMBER);
		byteBuf.writeByte(packet.getVersion());
		byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
		byteBuf.writeByte(packet.getCommand());
		byteBuf.writeByte(bytes.length);
		byteBuf.writeBytes(bytes);

		return byteBuf;
	}

	public Packet decode(ByteBuf byteBuf) {
		// skip magic number
		byteBuf.skipBytes(4);
		// skip version
		byteBuf.skipBytes(1);

		byte serializerAlgorithm = byteBuf.readByte();
		byte command = byteBuf.readByte();
		int length = byteBuf.readInt();
		byte[] bytes = new byte[length];
		byteBuf.readBytes(bytes);

		Class<? extends Packet> requestType = getRequestType(command);
		Serializer serializer = getSerializer(serializerAlgorithm);
		if (serializer != null && requestType != null) {
			return serializer.deserializer(requestType, bytes);
		}
		return null;
	}

	private Serializer getSerializer(byte serializeAlgorithm) {

		return serializerMap.get(serializeAlgorithm);
	}

	private Class<? extends Packet> getRequestType(byte command) {

		return packetTypeMap.get(command);
	}

}
