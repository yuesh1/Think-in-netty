package com.yueshi.netty.fakewx.util;

import com.yueshi.netty.fakewx.moude.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author: yuesh1 create: 2022-10-21 16:54
 */
public class LoginUtil {

	public static void markAsLogin(Channel channel) {
		channel.attr(Attributes.LOGIN).set(true);
	}

	public static boolean hasLogin(Channel channel) {
		Attribute<Boolean> attr = channel.attr(Attributes.LOGIN);
		return attr.get() != null;
	}

}
