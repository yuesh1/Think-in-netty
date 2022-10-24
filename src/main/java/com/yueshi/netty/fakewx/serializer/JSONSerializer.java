package com.yueshi.netty.fakewx.serializer;

import com.alibaba.fastjson.JSON;

/**
 * @author: yuesh1 create: 2022-10-21 11:23
 */
public class JSONSerializer implements Serializer {

	@Override
	public byte getSerializerAlgorithm() {
		return SerializerAlgorithm.JSON;
	}

	@Override
	public byte[] serialize(Object object) {
		return JSON.toJSONBytes(object);
	}

	@Override
	public <T> T deserialize(Class<T> clazz, byte[] bytes) {
		return JSON.parseObject(bytes, clazz);
	}

}