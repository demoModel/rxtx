package com.linchunsen.rxtx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEventListener;
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
public class SerialReader {

	private SerialContext serialContext;
	private MessageParseAble messageParse;

	/**
	 * 根据传入的串口配置上下文和报文解析器来构建报文读取器
	 * 
	 * @param serialContext
	 *            串口配置上下文
	 * @param messageParse
	 *            报文解析器
	 */
	public SerialReader(SerialContext serialContext, MessageParseAble messageParse) {
		super();
		this.serialContext = serialContext;
		this.messageParse = messageParse;
	}

	/**
	 * 启动串口的读监听器 无返回 无参数
	 * 
	 * @author linchunsen
	 * @throws UnsupportedCommOperationException 
	 * @throws TooManyListenersException 
	 * @throws IOException 
	 * @throws PortInUseException 
	 * @throws NoSuchPortException 
	 */
	public void startReaderListener() throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException {
		SerialPortRxtx serialPortRxtx = new SerialPortRxtx(this.serialContext);
		serialPortRxtx.open();
		SerialPortEventListener listener = new SerialAbstarctListener(serialPortRxtx) {

			private byte[] datas = new byte[1024];

			@Override
			public void readerAndWriter(InputStream inputStream, OutputStream outputStream) {
				try {
					if (inputStream.available() > 0) {
						int size = inputStream.read(datas);
						StringBuilder dataBuilder = new StringBuilder();
						String iString;
						for (int i = 0; i < size; i++) {
							iString = Integer.toHexString(datas[i] & 0xFF);
							if (iString.length() == 1) {
								iString = "0" + iString;
							}
							dataBuilder.append(iString);
						}
						String message = dataBuilder.toString().toUpperCase();
						messageParse.messageParse(message);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		serialPortRxtx.addEventListener(listener);
	}

}
