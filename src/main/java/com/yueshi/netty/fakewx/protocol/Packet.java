package com.yueshi.netty.fakewx.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: yuesh1 create: 2022-10-21 11:12
 */
@Data
public abstract class Packet {

	/**
	 * 协议版本
	 */
	@JSONField(deserialize = false, serialize = false)
	private Byte version = 1;

	/**
	 * 指令
	 */
	public abstract Byte getCommand();

}
