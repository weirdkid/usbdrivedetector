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

package com.weirdkid.usbdrivedetector.events;

/**
 * Interface to implement by the classes who want to receive notifications when 
 * there are devices Connected or Removed of the computer.
 * 
 * @author samuelcampos
 */
@FunctionalInterface
public interface IUSBDriveListener {
    
    void usbDriveEvent(USBStorageEvent event);
}
