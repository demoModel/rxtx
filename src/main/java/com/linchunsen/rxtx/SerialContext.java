package com.linchunsen.rxtx;

import java.util.Properties;

import gnu.io.SerialPort;

/**
 * <hr>
 * <h2>简介</h2> 串口通信的上下文，包含了串口的配置信息
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
public class SerialContext {
	/**
	 * 配置文件中的key的基础部分（key前缀），即完整的key=key前缀+"."+"端口名"+"."+key
	 */
	private final static String SERIAL_RXTX_BASE_KEY = "serial.rxtx.";
	/**
	 * 延时等待端口数据准备的时间，端口读入数据事件触发后,等待n毫秒后再读取,以便让数据一次性读完
	 */
	public static final String SERIAL_DELAY_KEY = "delayRead";
	/**
	 * 延时等待端口数据准备的时间的配置值
	 */
	private int delay = 1;

	/**
	 * 超时时间
	 */
	public static final String SERIAL_TIMEOUT_KEY = "timeout";
	/**
	 * 超时时间的配置值
	 */
	private int timeout = 3000;

	/**
	 * 端口名称
	 */
	public static final String SERIAL_PORT_KEY = "port";
	/**
	 * 端口名称的配置值
	 */
	private String port = "COM3";

	/**
	 * 数据位
	 */
	public static final String SERIAL_DATABITS_KEY = "dataBits";
	/**
	 * 数据位的配置值
	 */
	private int dataBits = SerialPort.DATABITS_8;

	/**
	 * 停止位
	 */
	public static final String SERIAL_STOPBITS_KEY = "stopBits";
	/**
	 * 停止位的配置值
	 */
	private int stopBits = SerialPort.STOPBITS_1;

	/**
	 * 奇偶校验
	 */
	public static final String SERIAL_PARITY_KEY = "parity";
	/**
	 * 奇偶校验的配置值
	 */
	private int parity = SerialPort.PARITY_NONE;

	/**
	 * 波特率
	 */
	public static final String SERIAL_RATE_KEY = "rate";
	/**
	 * 波特率的配置值
	 */
	private int rate = 115200;
	
	/**
	 * 应用名
	 * */
	public static final String SERIAL_APP_KEY = "appName";
	/**
	 * 应用名的默认名为SerialRXTX
	 */
	private String appName = "SerialRXTX";

	/**
	 * 串口上下文的构造方法，采用默认配置 无返回 无参数
	 * 
	 * @author linchunsen
	 */
	public SerialContext() {

	}

	/**
	 * 串口上下文的构造方法，根据传入的配置和key前缀加载配置 无返回
	 * 
	 * @param properties
	 *            传入的配置
	 * @param keyBase
	 *            key前缀
	 * @author linchunsen
	 */
	public SerialContext(Properties properties, String portName) {
		this.port=portName.toUpperCase();
		loadProperties(properties, SerialContext.SERIAL_RXTX_BASE_KEY +portName);
	}

	/**
	 * 根据系统配置和key前缀加载配置 无返回
	 * 
	 * @param properties
	 *            传入的配置
	 * @param keyBase
	 *            key前缀
	 * @author linchunsen
	 */
	public void loadProperties(Properties properties, String keyBase) {
		String delay = properties.getProperty(keyBase+"."+ SERIAL_DELAY_KEY, "" + this.delay);
		String timeout = properties.getProperty(keyBase+"."+ SERIAL_TIMEOUT_KEY, "" + this.timeout);
		String port = properties.getProperty(keyBase+"."+ SERIAL_PORT_KEY, "" + this.port);
		String dataBits = properties.getProperty(keyBase+"."+ SERIAL_DATABITS_KEY, "" + this.dataBits);
		String stopBits = properties.getProperty(keyBase+"."+ SERIAL_STOPBITS_KEY, "" + this.stopBits);
		String parity = properties.getProperty(keyBase+"."+ SERIAL_PARITY_KEY, "" + this.parity);
		String rate = properties.getProperty(keyBase+"."+ SERIAL_RATE_KEY, "" + this.rate);

		this.delay = Integer.parseInt(delay);
		this.timeout = Integer.parseInt(timeout);
		this.port = port;
		this.dataBits = Integer.parseInt(dataBits);
		//设置数据位
		if (this.dataBits==5) {
			this.dataBits=SerialPort.DATABITS_5;
		} else if (this.dataBits==6) {
			this.dataBits=SerialPort.DATABITS_6;
		} else if (this.dataBits==7) {
			this.dataBits=SerialPort.DATABITS_7;
		} else {
			this.dataBits=SerialPort.DATABITS_8;
		}
		float floatStopBits = Float.parseFloat(stopBits);
		//设置停止位
		if (floatStopBits==2) {
			this.stopBits=SerialPort.STOPBITS_2;
		} else if (floatStopBits==1.5) {
			this.stopBits=SerialPort.STOPBITS_1_5;
		} else {
			this.stopBits=SerialPort.STOPBITS_1;
		}
		this.parity = Integer.parseInt(parity);
		//设置校验
		if (this.parity==1) {//奇校验
			this.parity=SerialPort.PARITY_ODD;
		} else if(this.parity==2) {//偶校验
			this.parity=SerialPort.PARITY_EVEN;
		}else {//无校验
			this.parity=SerialPort.PARITY_NONE;
		}
		this.rate = Integer.parseInt(rate);
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
