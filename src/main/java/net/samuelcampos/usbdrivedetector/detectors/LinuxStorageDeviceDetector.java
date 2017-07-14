/*
 * Copyright 2014 samuelcampos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.samuelcampos.usbdrivedetector.detectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.samuelcampos.usbdrivedetector.USBStorageDevice;
import net.samuelcampos.usbdrivedetector.process.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tested on Linux Ubuntu 13.10
 *
 * @author samuelcampos
 */
public class LinuxStorageDeviceDetector extends AbstractStorageDeviceDetector {

    private static final Logger logger = LoggerFactory.getLogger(LinuxStorageDeviceDetector.class);

    private static final String CMD_DF = "df -l";
    private static final Pattern command1Pattern = Pattern.compile("^(\\/[^ ]+)[^%]+%[ ]+(.+)$");

    private static final String CMD_CHECK_USB = "udevadm info -q property -n ";
    private static final String strDeviceVerifier = "ID_USB_DRIVER=usb-storage";
    
    private static final String INFO_BUS = "ID_BUS";
    private static final String INFO_USB = "usb";
    private static final String INFO_NAME = "ID_FS_LABEL";

    private static final String DISK_PREFIX = "/dev/";

    protected LinuxStorageDeviceDetector() {
        super();
    }


    private void readDiskInfo(DiskInfo disk) {
	
	String command = CMD_CHECK_USB + disk.device;

	try (CommandExecutor commandExecutor = new CommandExecutor(command)) {
		
		commandExecutor.processOutput(outputLine -> {
			
			String[] parts = outputLine.split("="); 

			if(parts.length > 1){
			    if(INFO_BUS.equals(parts[0].trim())){
				disk.isUSB = INFO_USB.equals(parts[1].trim());
			    }
			    else if(INFO_NAME.equals(parts[0].trim())){
				disk.name = parts[1].trim();
			    }
			}
		   
		    });

	    } catch (IOException e) {
	    logger.error(e.getMessage(), e);
	}
	
    }
    

    @Override
    public List<USBStorageDevice> getStorageDevicesDevices() {
        final ArrayList<USBStorageDevice> listDevices = new ArrayList<>();

        try (CommandExecutor commandExecutor = new CommandExecutor(CMD_DF)){
            commandExecutor.processOutput((String outputLine) -> {
                final Matcher matcher = command1Pattern.matcher(outputLine);

                if (matcher.matches()) {

		    // device name, like /dev/sdh1
                    final String device = matcher.group(1);

		    // mount point, like /media/usb
                    final String rootPath = matcher.group(2);

		    if(device.startsWith(DISK_PREFIX)){
			DiskInfo disk = new DiskInfo(device);
			disk.mountPoint = rootPath;
			readDiskInfo(disk);
				      
			if(disk.isUSB){
			    listDevices.add(new USBStorageDevice(new File(disk.mountPoint), disk.name));
			}
		    }
                }
            });

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return listDevices;
    }

    private class DiskInfo{
	
	public DiskInfo(String device){
	    this.device = device;
	    mountPoint = "";
	    name = "";
	    isUSB = false;
	}
	
	String device;
	String mountPoint;
	String name;
	boolean isUSB;
	
    }

}
