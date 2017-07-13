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

    private static final String CMD_DF = "df";
    private static final Pattern command1Pattern = Pattern.compile("^(\\/[^ ]+)[^%]+%[ ]+(.+)$");

    private static final String CMD_CHECK_USB = "udevadm info -q property -n ";
    private static final String strDeviceVerifier = "ID_USB_DRIVER=usb-storage";

    protected LinuxStorageDeviceDetector() {
        super();
    }

    private boolean isUSBStorage(final String device) {
        final String verifyCommand = CMD_CHECK_USB + device;

        try (CommandExecutor commandExecutor = new CommandExecutor(verifyCommand)) {
            return commandExecutor.checkOutput(strDeviceVerifier::equals);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    @Override
    public List<USBStorageDevice> getStorageDevicesDevices() {
        final ArrayList<USBStorageDevice> listDevices = new ArrayList<>();

        try (CommandExecutor commandExecutor = new CommandExecutor(CMD_DF)){
            commandExecutor.processOutput((String outputLine) -> {
                final Matcher matcher = command1Pattern.matcher(outputLine);

                if (matcher.matches()) {
                    final String device = matcher.group(1);
                    final String rootPath = matcher.group(2);

                    if (isUSBStorage(device)) {
                        listDevices.add(getUSBDevice(rootPath));
                    }
                }
            });

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return listDevices;
    }
}
