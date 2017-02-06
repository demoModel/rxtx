package com.linchunsen.rxtx;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

/**
 * <hr>
 * <h2>简介</h2> 串口通讯的常规读取器
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
public class SerialWriter {

	private SerialContext serialContext;
	private OutputStream outputStream;

	public SerialWriter(SerialContext serialContext) {
		super();
		this.serialContext = serialContext;
	}

	// 标识串口是否已经初始化
	private boolean isInited = false;

	/**
	 * 初始化写串口器
	 * 
	 * @return 初始化后的写串口器对象
	 * @throws UnsupportedCommOperationException
	 * @throws TooManyListenersException
	 * @throws IOException
	 * @throws PortInUseException
	 * @throws NoSuchPortException
	 */
	public SerialWriter init() throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException,
			UnsupportedCommOperationException {
		SerialPortRxtx serialPortRxtx = new SerialPortRxtx(this.serialContext);
		serialPortRxtx.open();
		this.outputStream = serialPortRxtx.getOutputStream();
		this.isInited = true;
		return this;
	}

	/**
	 * 在串口初始化后，向串口中写入字符串
	 * 
	 * @param message
	 *            需要写入的字符串
	 * @throws IOException
	 */
	public void write(String message) throws IOException {
		if (isInited) {
			int length = message.length();
			byte[] bytes = new byte[length / 2];
			for (int i = 0; i < length; i++) {
				bytes[i / 2] = (byte) (((Character.digit(message.charAt(i), 16) << 4)
						+ Character.digit(message.charAt(++i), 16)));
			}
			outputStream.write(bytes);
		} else {
			throw new RuntimeException("写串口器尚未初始化，请执行init()方法后重试");
		}
	}
}
