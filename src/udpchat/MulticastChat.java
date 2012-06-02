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

import java.net.*;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

public class MulticastChat extends Thread {
	private static final String M_ADDRESS = "224.0.0.100";
	private static final int M_PORT = 25000;
	private static final int BUFSIZE = 1024;
	private MulticastSocket _socket;
	private MessageArrivedListener _listener;
	private Queue<ChatMessage> _inbox = new ConcurrentLinkedQueue<ChatMessage>();

	MulticastSocket getSocket() {
		if (_socket == null) {
			_socket = this.createSocket();
		}
		return _socket;
	}

	int getBufferSize() {
		return BUFSIZE;
	}

	public InetAddress getMulticastGroupAddress() {
		try {
			return InetAddress.getByName(M_ADDRESS);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	private int getTTL() {
		return 1;
	}

	private int getPort() {
		return M_PORT;
	}

	private MulticastSocket createSocket() {
		try {
			MulticastSocket sock = new MulticastSocket(M_PORT);
			sock.joinGroup(this.getMulticastGroupAddress());
			sock.setTimeToLive(this.getTTL());
			return sock;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	public void send(String message) throws IOException,
			IllegalArgumentException {
		byte[] data = message.trim().getBytes("utf-8");
		if (data.length > this.getBufferSize()) {
			throw new IllegalArgumentException("Message is too long.");
		}
		DatagramPacket packet = new DatagramPacket(data, data.length,
				this.getMulticastGroupAddress(), this.getPort());
		MulticastSocket sock = this.getSocket();
		sock.send(packet);
	}

	public void close() {
		this.interrupt();
		MulticastSocket sock = this.getSocket();
		try {
			sock.leaveGroup(this.getMulticastGroupAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		sock.close();
	}

	public void setMessageArrivedListener(MessageArrivedListener listener) {
		_listener = listener;
	}

	private void pushInbox(ChatMessage msg) {
		_inbox.add(msg);
		if (_listener != null) {
			_listener.messageArrived(msg);
		}
	}

	public void run() {
		try {
			MulticastSocket sock = this.getSocket();
			byte[] buf = new byte[BUFSIZE];
			while (true) {
				if (Thread.interrupted())
					break;
				DatagramPacket r_packet = new DatagramPacket(buf, BUFSIZE);
				sock.receive(r_packet);
				this.pushInbox(ChatMessage.create(r_packet));
				Arrays.fill(buf, (byte) 0);
			}
		} catch (SocketException e) {
			if(!Thread.interrupted()) e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
