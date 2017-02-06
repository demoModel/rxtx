package com.linchunsen.rxtx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <hr>
 * <h2>简介</h2> 根据配置文件对象来获取数据库Connection的工具类
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
 * <td>2017年1月6日</td>
 * <td>linchunsen</td>
 * <td>新建文件，并实现基本功能</td>
 * </tr>
 * </tbody>
 * </table>
 */
public class JdbcUtil {

	private boolean isLoaded = false;
	/**
	 * 配置文件的键头
	 */
	public static final String MYJDBC_KEY_BASE = "myJdbcUtil.";
	/**
	 * 配置文件中isEncodedInfo的键
	 */
	public static final String MYJDBC_KEY_ISENCODEDINFO = "isEncodedInfo";
	/**
	 * 配置文件中driverName的键
	 */
	public static final String MYJDBC_KEY_DRIVERNAME = "driverName";
	public String driverName;
	/**
	 * 配置文件中user的键
	 */
	public static final String MYJDBC_KEY_USER = "user";
	public String user;
	/**
	 * 配置文件中password的键
	 */
	public static final String MYJDBC_KEY_PASSWORD = "password";
	public String password;
	/**
	 * 配置文件中url的键
	 */
	public static final String MYJDBC_KEY_URL = "url";
	public String url;

	/**
	 * 根据配置文件对象来加载数据源配置
	 * 
	 * @param properties 要加载的配置文件对象
	 * @throws IOException 
	 */
	public void load(Properties properties) throws IOException {
		
		String isEncodedInfo=properties.getProperty(JdbcUtil.MYJDBC_KEY_BASE+JdbcUtil.MYJDBC_KEY_ISENCODEDINFO, null);
		String driverName = properties.getProperty(JdbcUtil.MYJDBC_KEY_BASE+JdbcUtil.MYJDBC_KEY_DRIVERNAME);
		String user = properties.getProperty(JdbcUtil.MYJDBC_KEY_BASE+JdbcUtil.MYJDBC_KEY_USER);
		String password = properties.getProperty(JdbcUtil.MYJDBC_KEY_BASE+JdbcUtil.MYJDBC_KEY_PASSWORD);
		String url = properties.getProperty(JdbcUtil.MYJDBC_KEY_BASE+JdbcUtil.MYJDBC_KEY_URL);
		
		if (isEncodedInfo!=null) {
			boolean isEncoded=Boolean.parseBoolean(isEncodedInfo);
			if (isEncoded) {//是已经加密过的配置信息
				this.driverName = getFromBase64(driverName);
				this.user = getFromBase64(user);
				this.password = getFromBase64(password);
				this.url = getFromBase64(url);
			} else {//不是已经加密过的配置信息，将在本次加密
				this.driverName=driverName;
				this.user=user;
				this.url=url;
				this.password=password;
				//加密配置信息
				driverName=getBase64(driverName);
				user=getBase64(user);
				url=getBase64(url);
				password=getBase64(password);
				isEncoded=true;
			}
		} else {//配置文件中没有配置项
			this.driverName=driverName;
			this.user=user;
			this.url=url;
			this.password=password;
		}
		
		this.isLoaded = true;
	}

	/**
	 * 在执行了load(Properties properties)之后，才可以调用获取数据库的Connection实例
	 * 
	 * @return Connection实例
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		if (isLoaded) {
			Class.forName(driverName);
			connection = DriverManager.getConnection(this.url, this.user, this.password);
		} else {
			throw new RuntimeException("未执行load(Properties properties)方法，执行该方法后重试");
		}
		return connection;
	}

	/**
	 * 将经过Base64转码的字符串还原
	 * 
	 * @param source
	 *            经过Base64转码的字符串
	 * @return 还原的字符串
	 * @throws IOException
	 */
	public String getFromBase64(String source) throws IOException {
		String result = null;
		byte[] bytes = null;
		if (source != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			bytes = decoder.decodeBuffer(source);
			result = new String(bytes, "utf-8");
		}
		return result;
	}

	/**
	 * 将目标字符串使用Base64进行编码
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String getBase64(String source) throws UnsupportedEncodingException {
		String result = null;
		byte[] bytes = null;
		bytes = source.getBytes("utf-8");
		if (bytes != null) {
			BASE64Encoder encoder = new BASE64Encoder();
			result = encoder.encode(bytes);
		}
		return result;
	}
}
