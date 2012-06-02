/*******************************************************************************
 * CopyRight (c) 2005-2011 TAOTAOSOU Co, Ltd. All rights reserved.
 * Filename:    FloatCodecTestCase.java
 * Creator:     wangqi
 * Create-Date: 2011-7-13 上午10:36:10
 *******************************************************************************/
package edu.hziee.common.serialization.bytebean.codec.primitive;

import junit.framework.Assert;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.hziee.common.serialization.bytebean.codec.DefaultCodecProvider;
import edu.hziee.common.serialization.bytebean.codec.DefaultNumberCodecs;
import edu.hziee.common.serialization.bytebean.codec.primitive.FloatCodec;
import edu.hziee.common.serialization.bytebean.context.DecResult;
import edu.hziee.common.serialization.bytebean.context.DefaultDecContextFactory;
import edu.hziee.common.serialization.bytebean.context.DefaultEncContextFactory;

/**
 * TODO
 * 
 * @author wangqi
 * @version $Id: FloatCodecTestCase.java 14 2012-01-10 11:54:14Z archie $
 */
public class FloatCodecTestCase {

	private FloatCodec codec;

	private DefaultEncContextFactory encContextFactory;
	private DefaultDecContextFactory decContextFactory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DefaultCodecProvider codecProvider = new DefaultCodecProvider();

		// 初始化解码器集合
		codec = new FloatCodec();
		codecProvider.addCodec(codec);

		encContextFactory = new DefaultEncContextFactory();
		decContextFactory = new DefaultDecContextFactory();

		encContextFactory.setCodecProvider(codecProvider);
		encContextFactory.setNumberCodec(DefaultNumberCodecs
				.getLittleEndianNumberCodec());

		decContextFactory.setCodecProvider(codecProvider);
		decContextFactory.setNumberCodec(DefaultNumberCodecs
				.getLittleEndianNumberCodec());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		codec = null;
		encContextFactory = null;
		decContextFactory = null;
	}

	@Test
	public void testCodec() {

		byte[] assertObj = codec.encode(encContextFactory.createEncContext(
				1.2f, float.class, null));
		System.out.println(ArrayUtils.toString(assertObj));
		DecResult result = codec.decode(decContextFactory.createDecContext(
				assertObj, float.class, null, null));
		System.out.println(result.getValue());
		Assert.assertEquals(1.2f, (Float) result.getValue());

		assertObj = codec.encode(encContextFactory.createEncContext(-1.2f,
				float.class, null));
		System.out.println(ArrayUtils.toString(assertObj));
		result = codec.decode(decContextFactory.createDecContext(assertObj,
				float.class, null, null));
		System.out.println(result.getValue());
		Assert.assertEquals(-1.2f, (Float) result.getValue());

		assertObj = codec.encode(encContextFactory.createEncContext(0f,
				float.class, null));
		System.out.println(ArrayUtils.toString(assertObj));
		result = codec.decode(decContextFactory.createDecContext(assertObj,
				float.class, null, null));
		System.out.println(result.getValue());
		Assert.assertEquals(0f, (Float) result.getValue());

		assertObj = codec.encode(encContextFactory.createEncContext(1f / 3,
				float.class, null));
		System.out.println(ArrayUtils.toString(assertObj));
		result = codec.decode(decContextFactory.createDecContext(assertObj,
				float.class, null, null));
		System.out.println(result.getValue());
		Assert.assertEquals(1f / 3, (Float) result.getValue());

		assertObj = codec.encode(encContextFactory.createEncContext(
				0.000000000000001f, float.class, null));
		System.out.println(ArrayUtils.toString(assertObj));
		result = codec.decode(decContextFactory.createDecContext(assertObj,
				float.class, null, null));
		System.out.println(result.getValue());
		Assert.assertEquals(0.000000000000001f, (Float) result.getValue());
	}
}
