

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class SocketThread extends Thread {

	Logger logger = Logger.getLogger(SocketThread.class);
	
	private Socket socket = null;
	private PrintWriter printWriter = null;
	private BufferedReader bufferedReader = null;

	private boolean isAlive = true;
	
	public SocketThread(Socket socket) {
		this.socket = socket;
	}

	public boolean sendMessage(String msg) throws IOException {
		if(!socketAlive()){ // 连接无效
			logger.warn("连接无效: 发送失败[" + msg + "]");
			return false;
		}
		if (printWriter == null) {
			printWriter = new PrintWriter(socket.getOutputStream());
		}
		printWriter.print(msg);
		printWriter.flush();
		return true;
	}

	public void run() {
		// 逻辑执行
		try {
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			String str = null;

			while ((str = read()) != null) {

				String xml;
				// 心跳发这个
				if (str.contains("channelId")) {
					System.out.println(str);
					Document document = DocumentHelper.parseText(str);
					Element root = document.getRootElement();
					Node channelIdNode = root.selectSingleNode("//channelId");
					Node checkIdNode = root.selectSingleNode("//checkId");
					Node checkTimeNode = root.selectSingleNode("//checkTime ");
					String checkId = channelIdNode.getText();
					xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><mall><response><state>0</state><checkId>"
							+ checkId + "</checkId></response></mall>";
					xml = "2002"
							+ StringUtils.leftPad(xml.length() + 10 + "", 6,
									"0") + xml;
					printWriter.print(xml);
					printWriter.flush();
				}

				if (socket.isClosed()) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String read() throws IOException {
		String str;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			int b ;
			if((b = bufferedReader.read())!= -1 ){
				sb.append((char)b);
			}else{
				return null;
			}
		}

		int length = Integer.parseInt(sb.toString().substring(4, 10))-10;
		System.out.println(length);
		
		
		StringBuffer sbContent = new StringBuffer();
		for (int i = 0; i < length; i++) {
				//System.out.println(i);
			int b ;
			if((b = bufferedReader.read())!= -1 ){
				sbContent.append((char)b);
			}else{
				return null;
			}
			
		}
		str = sbContent.toString();
		return str;
	}

	public void remove() throws IOException {
		if(!this.socketAlive()){
			if(bufferedReader != null){
				bufferedReader.close();
			}
			if(printWriter != null){
				printWriter.close();
			}
			this.socket.close();
		}
	}

	public boolean socketAlive() {
		return socket.isConnected();
	}

}
