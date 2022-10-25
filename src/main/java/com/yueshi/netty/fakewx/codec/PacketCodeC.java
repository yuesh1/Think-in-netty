package com.yueshi.netty.fakewx.codec;

import com.yueshi.netty.fakewx.protocol.Packet;
import com.yueshi.netty.fakewx.protocol.command.Command;
import com.yueshi.netty.fakewx.protocol.request.LoginRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.LoginResponsePacket;
import com.yueshi.netty.fakewx.protocol.request.MessageRequestPacket;
import com.yueshi.netty.fakewx.protocol.response.MessageResponsePacket;
import com.yueshi.netty.fakewx.serializer.JSONSerializer;
import com.yueshi.netty.fakewx.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author: yuesh1 create: 2022-10-21 11:42
 */
@Data
public class PacketCodeC {

	public static final PacketCodeC INSTANCE = new PacketCodeC();

	private int MAGIC_NUMBER = 0x12345678;

	private static final Map<Byte, Class<? extends Packet>> packetTypeMap;

	private static final Map<Byte, Serializer> serializerMap;

	static {
		packetTypeMap = new HashMap<>();
		packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
		packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
		packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
		packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);

		serializerMap = new HashMap<>();
		Serializer serializer = new JSONSerializer();
		serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
	}

	public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
		// 1. 创建 ByteBuf 对象
		ByteBuf byteBuf = byteBufAllocator.ioBuffer();
		// 2. 序列化 java 对象
		byte[] bytes = Serializer.DEFAULT.serialize(packet);

		// 3. 实际编码过程
		byteBuf.writeInt(MAGIC_NUMBER);
		byteBuf.writeByte(packet.getVersion());
		byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
		byteBuf.writeByte(packet.getCommand());
		byteBuf.writeInt(bytes.length);
		byteBuf.writeBytes(bytes);

		return byteBuf;
	}

	public void encode(Packet packet, ByteBuf out) {
		byte[] bytes = Serializer.DEFAULT.serialize(packet);
		// 实际编码过程
		out.writeInt(MAGIC_NUMBER);
		out.writeByte(packet.getVersion());
		out.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
		out.writeByte(packet.getCommand());
		out.writeInt(bytes.length);
		out.writeBytes(bytes);
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
			return serializer.deserialize(requestType, bytes);
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
