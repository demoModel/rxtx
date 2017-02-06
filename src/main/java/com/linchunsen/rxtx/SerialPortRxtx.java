package com.linchunsen.rxtx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * <hr>
 * <h2>简介</h2> 根据串口上下文设置串口配置，并提供启动和关闭的方法
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
 * <td>2017年1月4日</td>
 * <td>linchunsen</td>
 * <td>新建文件，并实现基本功能</td>
 * </tr>
 * </tbody>
 * </table>
 */
public class SerialPortRxtx {
	/**
	 * 标识端口是否已被打开
	 */
	private boolean isOpen;
	/**
	 * 属性描述
	 */
	private CommPortIdentifier portId;
	/**
	 * 属性描述
	 */
	private SerialPort serialPort;
	/**
	 * 输入流
	 */
	private InputStream inputStream;
	/**
	 * 输出流
	 */
	private OutputStream outputStream;
	/**
	 * 串口上下文
	 */
	private SerialContext serialContext;

	public SerialPortRxtx(SerialContext serialContext) {
		this.serialContext = serialContext;
	}

	/**
	 * 开启端口 无返回 无参数
	 * 
	 * @author linchunsen
	 */
	public void open() throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException,
			UnsupportedCommOperationException {
		portId = CommPortIdentifier.getPortIdentifier(serialContext.getPort());
		serialPort = (SerialPort) portId.open(serialContext.getAppName(), serialContext.getTimeout());
		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
		isOpen = true;
	}

	/**
	 * 关闭端口 无返回 无参数
	 * 
	 * @author linchunsen
	 */
	public void close() {
		if (isOpen) {
			try {
				serialPort.notifyOnDataAvailable(false);
				serialPort.removeEventListener();
				inputStream.close();
				outputStream.close();
				serialPort.close();
				isOpen = false;
			} catch (IOException ex) {
				// "关闭串口失败";
			}
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public SerialContext getSerialContext() {
		return serialContext;
	}

	public void setSerialContext(SerialContext serialContext) {
		this.serialContext = serialContext;
	}

	public CommPortIdentifier getPortId() {
		return portId;
	}

	public void setPortId(CommPortIdentifier portId) {
		this.portId = portId;
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * 为串口设置事件监听器
	 * 无返回
	 * @param listener 事件监听器
	 * @author linchunsen
	 * @throws TooManyListenersException 
	 * @throws UnsupportedCommOperationException 
	 */
	public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException, UnsupportedCommOperationException {
		if (isOpen) {
			this.serialPort.removeEventListener();
			this.serialPort.addEventListener(listener);
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(
					serialContext.getRate(),
					serialContext.getDataBits(),
					serialContext.getStopBits(),
					serialContext.getParity());
		}else {
			throw new RuntimeException("串口未打开，请检查是否执行了open()方法");
		}
	}

}
