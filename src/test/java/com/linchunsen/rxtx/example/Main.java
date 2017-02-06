package com.linchunsen.rxtx.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TooManyListenersException;

import com.linchunsen.rxtx.JdbcUtil;
import com.linchunsen.rxtx.MessageParseAble;
import com.linchunsen.rxtx.SerialStarter;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

/**
 * <hr>
 * <h2>简介</h2> 项目的启动类,第一个参数为配置文件，并且是必须的参数
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
public class Main {
	public static void main(String[] args) {
		reader(args);
	}
	
	/**
	 * 使用rxtx循环100次向配置文件中的串口写入数据
	 * */
	public static void writer(String[] args) {
		String filePath=args[0];
		try {
			//加载配置文件对象
			FileInputStream	fileInputStream = new FileInputStream(filePath);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			//循环100次向配置文件中的串口写入数据
			SerialStarter serialStarter = new SerialStarter(properties);
			for (int i = 0; i < 100; i++) {
				Thread.sleep(500);
				serialStarter.wirte("6801A10B16");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * 使用rxtx监听读取配置文件中的串口发送来的数据，并将其解析并发送到数据库
	 * */
	public static void reader(String[] args) {
		String filePath=args[0];
		try {
			//加载配置文件对象
			FileInputStream	fileInputStream = new FileInputStream(filePath);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			//根据配置文件实例化JDBC工具
			JdbcUtil myJdbcUtil=new JdbcUtil();
			myJdbcUtil.load(properties);
			Connection connection=myJdbcUtil.getConnection();
			//新建渲染器
			MessageParseAble messageParse = new MessageParse(connection);
			//监听读取配置文件中的串口发送来的数据，并将其解析并发送到数据库
			SerialStarter serialStarter = new SerialStarter(properties);
			serialStarter.startReader(messageParse);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
