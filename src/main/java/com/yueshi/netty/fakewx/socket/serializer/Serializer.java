package com.yueshi.netty.fakewx.socket.serializer;

/**
 * @author: yuesh1 create: 2022-10-21 11:20
 */
public interface Serializer {

	byte JSON_SERIALIZER = 1;

	Serializer DEFAULT = new JSONSerializer();

	/**
	 * 序列化算法
	 */
	byte getSerializerAlgorithm();

	byte[] serialize(Object object);

	<T> T deserialize(Class<T> clazz, byte[] bytes);

}
