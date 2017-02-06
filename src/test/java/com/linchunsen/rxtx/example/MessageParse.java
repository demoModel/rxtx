package com.linchunsen.rxtx.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.linchunsen.rxtx.MessageParseAble;

/**
 * <hr>
 * <h2>简介</h2> 报文解析接口的实现类，该类能够解析报文并将报文解析的内容提交到数据库
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
public class MessageParse implements MessageParseAble {

	private Connection connection;
	
	public MessageParse(Connection connection) throws ClassNotFoundException, SQLException {
		super();
		this.connection=connection;
	}

	/**
	 * 将接收到的数据添加到数据库Productive Process表
	 * 
	 * @return 返回行数
	 * @throws SQLException
	 */
	public int insertProductiveProcess(String machineId, String testType) throws SQLException {
		String resultFlag = testType.equals("A1") ? "00" : "01";
		String sql = null;
		if (resultFlag.equals("00")) {
			sql = "INSERT INTO A_PRODUCTIVE_PROCESS(METERID,DATAFLAG,TASKID,FINISH_DATE,THREADID,RESULTFLAG) VALUES('73301','"
					+ machineId + "','73301',getdate(),'73301','" + resultFlag + "')";
		} else if (resultFlag.equals("01")) {
			sql = "INSERT INTO A_PRODUCTIVE_PROCESS(METERID,DATAFLAG,TASKID,FINISH_DATE,THREADID,RESULTFLAG,REASON_CODE) VALUES('73301','"
					+ machineId + "','73301',getdate(),'73301','" + resultFlag + "','" + testType + "')";
		} else {
			return 0;
		}
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		int num = preparedStatement.executeUpdate();
		preparedStatement.close();
		if (machineId.equals("02") && resultFlag.equals("00")) {
			insertYieldTotal();
		}
		connection.commit();
		return num;
	}

	/**
	 * 将接收到的数据添加到数据库Yield Total表,只有设备2返回成功时插入
	 * 
	 * @return 返回行数
	 * @throws SQLException
	 */
	public int insertYieldTotal() throws SQLException {
		String sql = "INSERT INTO A_YIELD_TOTAL(TASK_ID,FINISH_DATE,QUANTITY,THREAD_ID) VALUES('73301',getdate(),1,'73301')";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		int num = preparedStatement.executeUpdate();
		preparedStatement.close();
		return num;
	}
	/**
	 * 报文解析接口的抽象方法实现
	 * @param message 接收到的报文
	 * @throws SQLException
	 * */
	@Override
	public void messageParse(String message) throws SQLException {
		if (message.startsWith("68") && message.endsWith("16")) {
			String machineId = message.substring(2, 4);
			String testType = message.substring(4, 6);
			System.out.println("设备号："+machineId+" 状态："+testType);
			insertProductiveProcess(machineId, testType);
		} else {
			throw new RuntimeException("报文格式错误，非法的头部和尾部");
		}
	}
}
