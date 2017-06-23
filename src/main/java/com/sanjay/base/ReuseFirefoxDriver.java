package com.sanjay.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.ExtensionConnection;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.internal.SocketLock;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;

public class ReuseFirefoxDriver extends FirefoxDriver {

	private HttpCommandExecutor httpClient;

	public ReuseFirefoxDriver(FirefoxProfile firefoxProfile) {
		super(firefoxProfile);
	}

	public ReuseFirefoxDriver() {
		super();
	}

	@Override
	protected void startSession(Capabilities desiredCapabilities) {
		if (this.localServerURL != null) {
			this.httpClient = new HttpCommandExecutor(this.localServerURL);
		}
		super.startSession(desiredCapabilities);
	}

	@Override
	protected ExtensionConnection connectTo(FirefoxBinary binary, FirefoxProfile profile, String host) {
		this.localServerURL = getURLofExistingLocalServer();
		if (this.localServerURL != null) {
			return new ExtensionConnection() {

				public boolean isConnected() {
					try {
						ReuseFirefoxDriver.this.httpClient.getAddressOfRemoteServer().openConnection().connect();
						return true;
					} catch (IOException e) {
						return false;
					}
				}

				public void quit() {
					// TODO Auto-generated method stub

				}

				public void start() throws IOException {
					// TODO Auto-generated method stub

				}

				public Response execute(Command arg0) throws IOException {
					return ReuseFirefoxDriver.this.httpClient.execute(arg0);
				}

				public void setLocalLogs(LocalLogs arg0) {
					// TODO Auto-generated method stub

				}

				public URI getAddressOfRemoteServer() {
					// TODO Auto-generated method stub
					return null;
				}

			};
		}
		return super.connectTo(binary, profile, host);
	}

	private URL localServerURL = null;

	private URL getURLofExistingLocalServer() {
		Socket socket = new Socket();
		try {
			socket.bind(new InetSocketAddress("localhost", SocketLock.DEFAULT_PORT));
			return null; // Able to connect on default port (Assuming that default FF driver is not running)
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		try {
			return new URL("http", new NetworkUtils().obtainLoopbackIp4Address(), SocketLock.DEFAULT_PORT, "/hub");
		} catch (MalformedURLException e) {
			throw new WebDriverException(e);
		}
	}

	public static void main(String... a) throws Exception {
		WebDriver driver = new ReuseFirefoxDriver();
		driver.get("http://www.google.com");
		System.out.println("Start second driver (Press enter)?");
		System.in.read();
		// The below code could be elsewhere in a different JVM
		driver = new ReuseFirefoxDriver();
		driver.get("http://www.bing.com");
	}
}
