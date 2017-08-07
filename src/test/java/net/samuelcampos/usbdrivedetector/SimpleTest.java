package net.samuelcampos.usbdrivedetector;

import com.weirdkid.usbdrivedetector.USBDeviceDetectorManager;
import com.weirdkid.usbdrivedetector.USBStorageDevice;
import com.weirdkid.usbdrivedetector.events.IUSBDriveListener;
import com.weirdkid.usbdrivedetector.events.USBStorageEvent;

/**
 *
 * @author samuelcampos
 */
public class SimpleTest implements IUSBDriveListener{
	public static void main(String[] args) {
		System.out.println("Start Test");
		USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();

		for(USBStorageDevice device : driveDetector.getRemovableDevices()){
			System.out.println(device);
		}

		SimpleTest sTest = new SimpleTest();

		driveDetector.addDriveListener(sTest);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Test finished");
	}

	private SimpleTest () {

	}

	@Override
	public void usbDriveEvent(USBStorageEvent event) {
		System.out.println(event);
	}
}
