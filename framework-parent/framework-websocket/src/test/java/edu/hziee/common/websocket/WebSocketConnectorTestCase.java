package edu.hziee.common.websocket;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.hziee.common.lang.transport.Receiver;
import edu.hziee.common.websocket.bto.NestedBean;
import edu.hziee.common.websocket.bto.SampleReq;

public class WebSocketConnectorTestCase {

	private AbstractApplicationContext	ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext(new String[] { "spring/websocketConnector.xml" });
	}

	@After
	public void tearDown() throws Exception {
		ctx = null;
	}

	@Test
	public void testSend() {

		SampleReq signal = new SampleReq();
		signal.setIntField(1);
		signal.setShortField((byte) 1);
		signal.setByteField((byte) 1);
		signal.setLongField(1L);
		signal.setStringField("中文");

		signal.setByteArrayField(new byte[] { 127 });

		ArrayList<NestedBean> listField = new ArrayList<NestedBean>();
		NestedBean bean1 = new NestedBean();
		bean1.setIntField(1);
		bean1.setShortField((short) 1);
		listField.add(bean1);
		NestedBean bean2 = new NestedBean();
		bean2.setIntField(2);
		bean2.setShortField((short) 2);
		listField.add(bean2);
		signal.setListField(listField);

		NestedBean bean3 = new NestedBean();
		bean3.setIntField(3);
		bean3.setShortField((short) 3);
		signal.setBeanField(bean3);

		WebSocketConnector sender = (WebSocketConnector) ctx.getBean("connector");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		sender.send(signal, new Receiver() {

			@Override
			public void messageReceived(Object msg) {
				System.out.println(msg);
			}
		});

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
