/*
 * Copyright (c) 2012 kanosaki
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */


package udpchat;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class App implements MessageArrivedListener, Runnable {
	private MulticastChat _client;

	public App() {
		_client = new MulticastChat();
		this.initClient();

		// Application shutdown hook.
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				_client.close();
			}
		}));
	}

	private void initClient() {
		_client.setMessageArrivedListener(this);
	}

	public void send(String msg) {
		try {
			_client.send(msg);
		} catch (Exception e) {
			this.showError(e);
		}
	}

	public MulticastChat getClient() {
		return _client;
	}

	public static App create(String[] args) {
		if (args.length >= 1 && args[0].equals("-c")) {
			return new Console();
		} else {
			return new Swing();
		}
	}

	public void run() {
		try {
			this.runApp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void showError(Exception e);

	public abstract void log(String s);

	protected void runApp() {
		this.getClient().start();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(App.create(args));
	}

	public static class Swing extends App {
		ChatFrame _frame;

		public Swing() {
			_frame = new ChatFrame(this);
		}

		@Override
		public void messageArrived(ChatMessage msg) {
			String message = msg.toString();
			_frame.getMessages().add(message);
		}

		@Override
		protected void showError(Exception e) {
			e.printStackTrace();
		}

		@Override
		protected void runApp() {
			_frame.setVisible(true);
			super.runApp();
		}

		@Override
		public void log(String s) {
			_frame.log(s);
		}

	}

	public static class Console extends App {

		@Override
		public void messageArrived(ChatMessage msg) {
			System.out.println(msg.toString().trim());
		}

		@Override
		protected void showError(Exception e) {
			System.out.println("ERROR DETECTED:");
			e.printStackTrace();
		}

		@Override
		public void log(String s) {
			System.out.println(s);
		}

		@Override
		protected void runApp() {
			System.out.printf("===== Joined: %s =====\n", this.getClient().getMulticastGroupAddress());
			super.runApp();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				while (true) {
					System.out.print("> ");
					String str = reader.readLine();
					if (str == null || str.equals("")) {
						break;
					}
					this.send(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
