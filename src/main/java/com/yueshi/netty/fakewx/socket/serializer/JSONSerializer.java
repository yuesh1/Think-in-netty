package com.yueshi.netty.fakewx.socket.serializer;

import com.alibaba.fastjson2.JSON;

/**
 * @author: yuesh1 create: 2022-10-21 11:23
 */
public class JSONSerializer implements Serializer {

	@Override
	public byte getSerializerAlgorithm() {
		return SerializerAlgorithm.JSON;
	}

	@Override
	public byte[] serializer(Object object) {
		return JSON.toJSONBytes(object);
	}

	@Override
	public <T> T deserializer(Class<T> clazz, byte[] bytes) {
		return JSON.parseObject(bytes, clazz);
	}

}
