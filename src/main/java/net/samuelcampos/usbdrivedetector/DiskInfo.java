package net.samuelcampos.usbdrivedetector;



/**
 * 
 * A struct for holding disk info while parsing commands
 * 
 * @author weirdkid
 *
 */

public class DiskInfo{
	
	public DiskInfo(String device){
		this.device = device;
		mountPoint = "";
		name = "";
		isUSB = false;
	}
	
	public String device;
	public String mountPoint;
	public String name;
	public boolean isUSB;
	
}
