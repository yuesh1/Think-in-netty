package com.yueshi.netty.fakewx.moude;

import io.netty.util.AttributeKey;

/**
 * @author: yuesh1 create: 2022-10-21 16:47
 */
public interface Attributes {

	AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

}
