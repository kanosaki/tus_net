package udpchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {
	public static String system(String command) throws IOException, InterruptedException {
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(command);
		p.waitFor();
		StringBuffer sbuf = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while((line = br.readLine()) != null){
			sbuf.append(line);
		}
		br.close();
		return sbuf.toString();
	}
}
