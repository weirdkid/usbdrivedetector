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

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author samuelcampos
 */
public class WindowsStorageDeviceDetector extends AbstractStorageDeviceDetector {

    private static final Logger logger = LoggerFactory.getLogger(WindowsStorageDeviceDetector.class);

    /**
     * wmic logicaldisk where drivetype=2 get description,deviceid,volumename
     */
    private static final String CMD_WMI_USB = "wmic logicaldisk where drivetype=2 get deviceid";

    protected WindowsStorageDeviceDetector() {
        super();
    }

    @Override
    public List<USBStorageDevice> getStorageDevicesDevices() {
        final ArrayList<USBStorageDevice> listDevices = new ArrayList<USBStorageDevice>();

        CommandExecutor commandExecutor = null;
        
        try{
        	commandExecutor = new CommandExecutor(CMD_WMI_USB);
        	String outputLine = null;
    		while((outputLine = commandExecutor.readOutputLine()) != null){
    			if (!outputLine.isEmpty() && !"DeviceID".equals(outputLine)) {
                    final String rootPath = outputLine + File.separatorChar;
                    listDevices.add(getUSBDevice(rootPath, getDeviceName(rootPath)));
                }
    		}
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        
        closeCommand(commandExecutor);

        return listDevices;
    }

    private String getDeviceName(final String rootPath) {
        final File f = new File(rootPath);
        final FileSystemView v = FileSystemView.getFileSystemView();
        String name = v.getSystemDisplayName(f);

        if (name != null) {
            int idx = name.lastIndexOf('(');
            if (idx != -1) {
                name = name.substring(0, idx);
            }

            name = name.trim();
            if (name.isEmpty()) {
                name = null;
            }
        }
        return name;
    }

}
