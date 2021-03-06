/**
 * 
 */
package edu.hziee.common.websocket.codec;

import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hziee.common.lang.ByteUtil;
import edu.hziee.common.lang.Transformer;
import edu.hziee.common.serialization.bytebean.codec.AnyCodec;
import edu.hziee.common.serialization.bytebean.codec.DefaultCodecProvider;
import edu.hziee.common.serialization.bytebean.codec.DefaultNumberCodecs;
import edu.hziee.common.serialization.bytebean.codec.array.LenArrayCodec;
import edu.hziee.common.serialization.bytebean.codec.array.LenListCodec;
import edu.hziee.common.serialization.bytebean.codec.bean.BeanFieldCodec;
import edu.hziee.common.serialization.bytebean.codec.bean.EarlyStopBeanCodec;
import edu.hziee.common.serialization.bytebean.codec.primitive.ByteCodec;
import edu.hziee.common.serialization.bytebean.codec.primitive.CStyleStringCodec;
import edu.hziee.common.serialization.bytebean.codec.primitive.IntCodec;
import edu.hziee.common.serialization.bytebean.codec.primitive.LenByteArrayCodec;
import edu.hziee.common.serialization.bytebean.codec.primitive.LongCodec;
import edu.hziee.common.serialization.bytebean.codec.primitive.ShortCodec;
import edu.hziee.common.serialization.bytebean.context.DefaultDecContextFactory;
import edu.hziee.common.serialization.bytebean.context.DefaultEncContextFactory;
import edu.hziee.common.serialization.bytebean.field.DefaultField2Desc;
import edu.hziee.common.serialization.protocol.annotation.SignalCode;
import edu.hziee.common.serialization.protocol.xip.XipHeader;
import edu.hziee.common.serialization.protocol.xip.XipSignal;

/**
 * @author ubuntu-admin
 * 
 */
public class BinaryWebSocketFrameEncoder implements Transformer<Object, BinaryWebSocketFrame> {

	private static final Logger	logger		= LoggerFactory.getLogger(BinaryWebSocketFrameEncoder.class);

	private BeanFieldCodec			byteBeanCodec;
	private int									dumpBytes	= 256;
	private boolean							isDebugEnabled;

	@Override
	public BinaryWebSocketFrame transform(Object signal) {
		BinaryWebSocketFrame frame = new BinaryWebSocketFrame();
		if (signal instanceof XipSignal) {
			byte[] bytes = encodeXip((XipSignal) signal);
			if (logger.isDebugEnabled()) {
				logger.debug("signal as hex:{} \r\n{} ", ByteUtil.bytesAsHexString(bytes, dumpBytes));
			}
			if (null != bytes) {
				frame.setBinaryData(ChannelBuffers.wrappedBuffer(bytes));
			}
		}
		return frame;
	}

	private byte[] encodeXip(XipSignal signal) {
		byte[] bytesBody = getByteBeanCodec().encode(
				getByteBeanCodec().getEncContextFactory().createEncContext(signal, signal.getClass(), null));

		SignalCode attr = signal.getClass().getAnnotation(SignalCode.class);
		if (null == attr) {
			throw new RuntimeException("invalid signal, no messageCode defined.");
		}

		XipHeader header = createHeader((byte) 1, signal.getIdentification(), attr.messageCode(), bytesBody.length);

		// 更新请求类型
		header.setTypeForClass(signal.getClass());

		byte[] bytes = ArrayUtils.addAll(
				getByteBeanCodec().encode(
						getByteBeanCodec().getEncContextFactory().createEncContext(header, XipHeader.class, null)), bytesBody);

		if (logger.isDebugEnabled() && isDebugEnabled) {
			logger.debug("encode XipSignal:" + signal);
			logger.debug("and XipSignal raw bytes -->");
			logger.debug(ByteUtil.bytesAsHexString(bytes, dumpBytes));
		}

		return bytes;
	}

	private XipHeader createHeader(byte basicVer, UUID id, int messageCode, int messageLen) {

		XipHeader header = new XipHeader();

		header.setTransaction(id);

		int headerSize = getByteBeanCodec().getStaticByteSize(XipHeader.class);

		header.setLength(headerSize + messageLen);
		header.setMessageCode(messageCode);
		header.setBasicVer(basicVer);

		return header;
	}

	public void setByteBeanCodec(BeanFieldCodec byteBeanCodec) {
		this.byteBeanCodec = byteBeanCodec;
	}

	public BeanFieldCodec getByteBeanCodec() {
		if (byteBeanCodec == null) {
			DefaultCodecProvider codecProvider = new DefaultCodecProvider();

			// 初始化解码器集合
			codecProvider.addCodec(new AnyCodec()).addCodec(new ByteCodec()).addCodec(new ShortCodec())
					.addCodec(new IntCodec()).addCodec(new LongCodec()).addCodec(new CStyleStringCodec())
					.addCodec(new LenByteArrayCodec()).addCodec(new LenListCodec()).addCodec(new LenArrayCodec());

			// 对象解码器需要指定字段注释读取方法
			EarlyStopBeanCodec byteBeanCodec = new EarlyStopBeanCodec(new DefaultField2Desc());
			codecProvider.addCodec(byteBeanCodec);

			DefaultEncContextFactory encContextFactory = new DefaultEncContextFactory();
			DefaultDecContextFactory decContextFactory = new DefaultDecContextFactory();

			encContextFactory.setCodecProvider(codecProvider);
			encContextFactory.setNumberCodec(DefaultNumberCodecs.getBigEndianNumberCodec());

			decContextFactory.setCodecProvider(codecProvider);
			decContextFactory.setNumberCodec(DefaultNumberCodecs.getBigEndianNumberCodec());

			byteBeanCodec.setDecContextFactory(decContextFactory);
			byteBeanCodec.setEncContextFactory(encContextFactory);

			this.byteBeanCodec = byteBeanCodec;
		}
		return byteBeanCodec;
	}

	public int getDumpBytes() {
		return dumpBytes;
	}

	public void setDumpBytes(int dumpBytes) {
		this.dumpBytes = dumpBytes;
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}

}
