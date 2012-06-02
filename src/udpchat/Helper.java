package udpchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {
	public static String system(String command) throws IOException {
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(command);
		StringBuffer sbuf = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while(true){
			String line = br.readLine();
			if(line.equals("")) break;
			sbuf.append(line);
		}
		br.close();
		return sbuf.toString();
	}
}
