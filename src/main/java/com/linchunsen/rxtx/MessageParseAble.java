package com.linchunsen.rxtx;

/**
 * <hr>
 * <h2>简介</h2> 报文解析接口,只有实现了该接口的类才能被串口读取器所调用以解析报文
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
public interface MessageParseAble {
	/**
	 * 解析执行报文
	 * 
	 * @param message
	 *            报文
	 * @throws Exception
	 */
	void messageParse(String message) throws Exception;
}
