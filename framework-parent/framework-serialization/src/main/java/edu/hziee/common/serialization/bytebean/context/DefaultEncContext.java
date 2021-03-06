
package edu.hziee.common.serialization.bytebean.context;

import edu.hziee.common.serialization.bytebean.codec.FieldCodecProvider;
import edu.hziee.common.serialization.bytebean.codec.NumberCodec;
import edu.hziee.common.serialization.bytebean.field.ByteFieldDesc;

/**
 * TODO
 * 
 * @author wangqi
 * @version $Id: DefaultEncContext.java 14 2012-01-10 11:54:14Z archie $
 */
public class DefaultEncContext extends AbstractCodecContext implements
		EncContext {

	private Object encObject;
	private EncContextFactory encContextFactory;

	public DefaultEncContext setCodecProvider(FieldCodecProvider codecProvider) {
		this.codecProvider = codecProvider;
		return this;
	}

	/**
	 * @param encClass
	 *            the encClass to set
	 */
	public DefaultEncContext setEncClass(Class<?> encClass) {
		this.targetType = encClass;
		return this;
	}

	/**
	 * @param encImpl
	 *            the encImpl to set
	 */
	public DefaultEncContext setFieldDesc(ByteFieldDesc desc) {
		this.fieldDesc = desc;
		return this;
	}

	/**
	 * @param numberCodec
	 *            the numberCodec to set
	 */
	public DefaultEncContext setNumberCodec(NumberCodec numberCodec) {
		this.numberCodec = numberCodec;
		return this;
	}

	/**
	 * @param encObject
	 *            the encObject to set
	 */
	public DefaultEncContext setEncObject(Object encObject) {
		this.encObject = encObject;
		return this;
	}

	/**
	 * @param encContextFactory
	 *            the encContextFactory to set
	 */
	public DefaultEncContext setEncContextFactory(
			EncContextFactory encContextFactory) {
		this.encContextFactory = encContextFactory;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.taotaosou.common.serialization.bytebean.context.EncContext#getEncObject
	 * ()
	 */
	@Override
	public Object getEncObject() {
		return encObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.taotaosou.common.serialization.bytebean.context.EncContext#getEncClass
	 * ()
	 */
	@Override
	public Class<?> getEncClass() {
		return this.targetType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.taotaosou.common.serialization.bytebean.context.EncContext#
	 * getEncContextFactory()
	 */
	@Override
	public EncContextFactory getEncContextFactory() {
		return encContextFactory;
	}

}
