package com.linchunsen.rxtx;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TooManyListenersException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

/**
 * <hr>
 * <h2>简介</h2> 串口通讯的启动类
 * <hr>
 * <table border="1" cellspacing="0" cellpadding="2">
 * <caption><b>文件修改记录</b></caption>
 * <tr>
 * <th>修改日期</th>
 * <th>修改人</th>
 * <th>修改内容</th>
 * </tr>
 * <tbody>
 * <tr>
 * <td>2016年12月29日</td>
 * <td>linchunsen</td>
 * <td>新建文件，并实现基本功能</td>
 * </tr>
 * </tbody>
 * </table>
 */
public class SerialStarter {

	private Properties properties;

	/**
	 * 根据传入的系统配置来定义串口通讯的启动类
	 */
	public SerialStarter(Properties properties) {
		super();
		this.properties = properties;
	}

	private List<SerialWriter> serialWriters = null;

	/**
	 * 将传入的消息逐次发给各个串口
	 * @throws IOException 
	 * @throws UnsupportedCommOperationException 
	 * @throws TooManyListenersException 
	 * @throws PortInUseException 
	 * @throws NoSuchPortException 
	 */
	public void wirte(String message) throws IOException, NoSuchPortException, PortInUseException, TooManyListenersException, UnsupportedCommOperationException {
		//如果写串口器列表还未初始化，则初始化
		if (serialWriters == null) {
			serialWriters = new ArrayList<>();
			Set<Object> keys = properties.keySet();
			Set<String> baseKeys = new HashSet<>();
			String key;
			String[] keyPace;
			// 过滤前缀
			for (Object object : keys) {
				key = object.toString();
				if (key.startsWith("serial.rxtx.")) {
					keyPace = key.split("\\.");
					key = keyPace[2];
					baseKeys.add(key);
				}
			}
			for (String baseKey : baseKeys) {
				SerialContext serialContext = new SerialContext(properties, baseKey);
				serialWriters.add(new SerialWriter(serialContext).init());
			}
		}
		//将消息逐个写到串口
		for (SerialWriter serialWriter : serialWriters) {
			serialWriter.write(message);
		}
		
	}

	/**
	 * 根据传入的报文解析器，以后台进程的形式启动串口读取器
	 * 
	 * @param messageParser
	 *            传入的报文解析器
	 */
	public void startReader(MessageParseAble messageParser) {
		final MessageParseAble messageParse = messageParser;
		Set<Object> keys = properties.keySet();
		Set<String> baseKeys = new HashSet<>();
		String key;
		String[] keyPace;
		// 过滤前缀
		for (Object object : keys) {
			key = object.toString();
			if (key.startsWith("serial.rxtx.")) {
				keyPace = key.split("\\.");
				key = keyPace[2];
				baseKeys.add(key);
			}
		}
		for (String baseKey : baseKeys) {
			SerialContext serialContext = new SerialContext(properties, baseKey);
			new Thread() {
				SerialReader serialTask = new SerialReader(serialContext, messageParse);

				@Override
				public void run() {
					try {
						serialTask.startReaderListener();
					} catch (NoSuchPortException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (PortInUseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TooManyListenersException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedCommOperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}.start();
			System.out.println("正在监听串口 " + baseKey.toUpperCase());
		}
	}

}
