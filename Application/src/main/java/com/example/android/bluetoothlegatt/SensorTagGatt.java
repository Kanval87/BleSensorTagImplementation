/**************************************************************************************************
 * Filename:       SensorTagGatt.java
 * Revised:        $Date: 2013-08-30 11:44:31 +0200 (fr, 30 aug 2013) $
 * Revision:       $Revision: 27454 $
 * <p/>
 * Copyright (c) 2013 - 2014 Texas Instruments Incorporated
 * <p/>
 * All rights reserved not granted herein.
 * Limited License.
 * <p/>
 * Texas Instruments Incorporated grants a world-wide, royalty-free,
 * non-exclusive license under copyrights and patents it now or hereafter
 * owns or controls to make, have made, use, import, offer to sell and sell ("Utilize")
 * this software subject to the terms herein.  With respect to the foregoing patent
 * license, such license is granted  solely to the extent that any such patent is necessary
 * to Utilize the software alone.  The patent license shall not apply to any combinations which
 * include this software, other than combinations with devices manufactured by or for TI ('TI Devices').
 * No hardware patent is licensed hereunder.
 * <p/>
 * Redistributions must preserve existing copyright notices and reproduce this license (including the
 * above copyright notice and the disclaimer and (if applicable) source code license limitations below)
 * in the documentation and/or other materials provided with the distribution
 * <p/>
 * Redistribution and use in binary form, without modification, are permitted provided that the following
 * conditions are met:
 * <p/>
 * No reverse engineering, decompilation, or disassembly of this software is permitted with respect to any
 * software provided in binary form.
 * any redistribution and use are licensed by TI for use only with TI Devices.
 * Nothing shall obligate TI to provide you with source code for the software licensed and provided to you in object code.
 * <p/>
 * If software source code is provided to you, modification and redistribution of the source code are permitted
 * provided that the following conditions are met:
 * <p/>
 * any redistribution and use of the source code, including any resulting derivative works, are licensed by
 * TI for use only with TI Devices.
 * any redistribution and use of any object code compiled from the source code and any resulting derivative
 * works, are licensed by TI for use only with TI Devices.
 * <p/>
 * Neither the name of Texas Instruments Incorporated nor the names of its suppliers may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * <p/>
 * DISCLAIMER.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY TI AND TI'S LICENSORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL TI AND TI'S LICENSORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 **************************************************************************************************/
package com.example.android.bluetoothlegatt;

import java.util.HashMap;
import java.util.UUID;

import static java.util.UUID.fromString;

public class SensorTagGatt {

    // Useful maps.
    private static HashMap<String, String> attributes = new HashMap<>();
    private static HashMap<String, String> serverToConfig = new HashMap<>();
    private static HashMap<String, String> serverToData = new HashMap<>();
    private static HashMap<String, String> serverToPeriod = new HashMap<>();


    public final static UUID UUID_DEVINFO_SERV = fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_DEVINFO_FWREV = fromString("00002A26-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_IRT_SERV = fromString("f000aa00-0451-4000-b000-000000000000");
    public final static UUID UUID_IRT_DATA = fromString("f000aa01-0451-4000-b000-000000000000");
    public final static UUID UUID_IRT_CONF = fromString("f000aa02-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_IRT_PERI = fromString("f000aa03-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_ACC_SERV = fromString("f000aa10-0451-4000-b000-000000000000");
    public final static UUID UUID_ACC_DATA = fromString("f000aa11-0451-4000-b000-000000000000");
    public final static UUID UUID_ACC_CONF = fromString("f000aa12-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_ACC_PERI = fromString("f000aa13-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_HUM_SERV = fromString("f000aa20-0451-4000-b000-000000000000");
    public final static UUID UUID_HUM_DATA = fromString("f000aa21-0451-4000-b000-000000000000");
    public final static UUID UUID_HUM_CONF = fromString("f000aa22-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_HUM_PERI = fromString("f000aa23-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_MAG_SERV = fromString("f000aa30-0451-4000-b000-000000000000");
    public final static UUID UUID_MAG_DATA = fromString("f000aa31-0451-4000-b000-000000000000");
    public final static UUID UUID_MAG_CONF = fromString("f000aa32-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_MAG_PERI = fromString("f000aa33-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_OPT_SERV = fromString("f000aa70-0451-4000-b000-000000000000");
    public final static UUID UUID_OPT_DATA = fromString("f000aa71-0451-4000-b000-000000000000");
    public final static UUID UUID_OPT_CONF = fromString("f000aa72-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_OPT_PERI = fromString("f000aa73-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_BAR_SERV = fromString("f000aa40-0451-4000-b000-000000000000");
    public final static UUID UUID_BAR_DATA = fromString("f000aa41-0451-4000-b000-000000000000");
    public final static UUID UUID_BAR_CONF = fromString("f000aa42-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_BAR_CALI = fromString("f000aa43-0451-4000-b000-000000000000"); // Calibration characteristic
    public final static UUID UUID_BAR_PERI = fromString("f000aa44-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_GYR_SERV = fromString("f000aa50-0451-4000-b000-000000000000");
    public final static UUID UUID_GYR_DATA = fromString("f000aa51-0451-4000-b000-000000000000");
    public final static UUID UUID_GYR_CONF = fromString("f000aa52-0451-4000-b000-000000000000"); // 0: disable, bit 0: enable x, bit 1: enable y, bit 2: enable z
    public final static UUID UUID_GYR_PERI = fromString("f000aa53-0451-4000-b000-000000000000"); // Period in tens of milliseconds

    public final static UUID UUID_MOV_SERV = fromString("f000aa80-0451-4000-b000-000000000000");
    public final static UUID UUID_MOV_DATA = fromString("f000aa81-0451-4000-b000-000000000000");
    public final static UUID UUID_MOV_CONF = fromString("f000aa82-0451-4000-b000-000000000000"); // 0: disable, bit 0: enable x, bit 1: enable y, bit 2: enable z
    public final static UUID UUID_MOV_PERI = fromString("f000aa83-0451-4000-b000-000000000000"); // Period in tens of milliseconds


    public final static UUID UUID_KEY_SERV = fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_KEY_DATA = fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    static {
        // Sample Services.
        attributes.put(UUID_DEVINFO_SERV.toString(), "Device Information");
        attributes.put(UUID_IRT_SERV.toString(), "IRT Temperature Sensor");
        attributes.put(UUID_ACC_SERV.toString(), "Accelerometer");
        attributes.put(UUID_HUM_SERV.toString(), "Humidity Sensor");
        attributes.put(UUID_MAG_SERV.toString(), "Magnetometer");
        attributes.put(UUID_BAR_SERV.toString(), "Barometric Pressure Sensor");
        attributes.put(UUID_GYR_SERV.toString(), "Gyroscope");
        attributes.put(UUID_OPT_SERV.toString(), "Luxometer");
        attributes.put(UUID_MOV_SERV.toString(), "Motion Sensor");
        attributes.put(UUID_KEY_SERV.toString(), "Keys");
        // Sample Characteristics.
        attributes.put(UUID_DEVINFO_FWREV.toString(), "Device Revision");
        attributes.put(UUID_IRT_DATA.toString(), "Data");
        attributes.put(UUID_IRT_CONF.toString(), "Configuration");
        attributes.put(UUID_IRT_PERI.toString(), "Period");
        attributes.put(UUID_ACC_DATA.toString(), "Data");
        attributes.put(UUID_ACC_CONF.toString(), "Configuration");
        attributes.put(UUID_ACC_PERI.toString(), "Period");
        attributes.put(UUID_HUM_DATA.toString(), "Data");
        attributes.put(UUID_HUM_CONF.toString(), "Configuration");
        attributes.put(UUID_HUM_PERI.toString(), "Period");
        attributes.put(UUID_MAG_DATA.toString(), "Data");
        attributes.put(UUID_MAG_CONF.toString(), "Configuration");
        attributes.put(UUID_MAG_PERI.toString(), "Period");
        attributes.put(UUID_BAR_DATA.toString(), "Data");
        attributes.put(UUID_BAR_CONF.toString(), "Configuration");
        attributes.put(UUID_BAR_CALI.toString(), "Calibration");
        attributes.put(UUID_BAR_PERI.toString(), "Period");
        attributes.put(UUID_GYR_DATA.toString(), "Data");
        attributes.put(UUID_GYR_CONF.toString(), "Configuration");
        attributes.put(UUID_GYR_PERI.toString(), "Period");
        attributes.put(UUID_OPT_DATA.toString(), "Data");
        attributes.put(UUID_OPT_CONF.toString(), "Configuration");
        attributes.put(UUID_OPT_PERI.toString(), "Period");
        attributes.put(UUID_MOV_DATA.toString(), "Data");
        attributes.put(UUID_MOV_PERI.toString(), "Period");
        attributes.put(UUID_MOV_CONF.toString(), "Configuration");
        attributes.put(UUID_KEY_DATA.toString(), "Data");
        // Service UUID to configuration UUID map.
        serverToConfig.put(UUID_IRT_SERV.toString(), UUID_IRT_CONF.toString());
        serverToConfig.put(UUID_ACC_SERV.toString(), UUID_ACC_CONF.toString());
        serverToConfig.put(UUID_HUM_SERV.toString(), UUID_HUM_CONF.toString());
        serverToConfig.put(UUID_MAG_SERV.toString(), UUID_MAG_CONF.toString());
        serverToConfig.put(UUID_BAR_SERV.toString(), UUID_BAR_CONF.toString());
        serverToConfig.put(UUID_GYR_SERV.toString(), UUID_GYR_CONF.toString());
        serverToConfig.put(UUID_OPT_SERV.toString(), UUID_OPT_CONF.toString());
        serverToConfig.put(UUID_MOV_SERV.toString(), UUID_MOV_CONF.toString());
        // Service UUID to data UUID map.
        serverToData.put(UUID_IRT_SERV.toString(), UUID_IRT_DATA.toString());
        serverToData.put(UUID_ACC_SERV.toString(), UUID_ACC_DATA.toString());
        serverToData.put(UUID_HUM_SERV.toString(), UUID_HUM_DATA.toString());
        serverToData.put(UUID_MAG_SERV.toString(), UUID_MAG_DATA.toString());
        serverToData.put(UUID_BAR_SERV.toString(), UUID_BAR_DATA.toString());
        serverToData.put(UUID_GYR_SERV.toString(), UUID_GYR_DATA.toString());
        serverToData.put(UUID_OPT_SERV.toString(), UUID_OPT_DATA.toString());
        serverToData.put(UUID_MOV_SERV.toString(), UUID_MOV_DATA.toString());
        serverToData.put(UUID_KEY_SERV.toString(), UUID_KEY_DATA.toString());
        // Service UUID to period UUID map.
        serverToPeriod.put(UUID_IRT_SERV.toString(), UUID_IRT_PERI.toString());
        serverToPeriod.put(UUID_ACC_SERV.toString(), UUID_ACC_PERI.toString());
        serverToPeriod.put(UUID_HUM_SERV.toString(), UUID_HUM_PERI.toString());
        serverToPeriod.put(UUID_MAG_SERV.toString(), UUID_MAG_PERI.toString());
        serverToPeriod.put(UUID_BAR_SERV.toString(), UUID_BAR_PERI.toString());
        serverToPeriod.put(UUID_GYR_SERV.toString(), UUID_GYR_PERI.toString());
        serverToPeriod.put(UUID_OPT_SERV.toString(), UUID_OPT_PERI.toString());
        serverToPeriod.put(UUID_MOV_SERV.toString(), UUID_MOV_PERI.toString());
    }

    /**
     * Gives the name of the service corresponding to the input service UUID.
     */
    public static String lookup(String serviceUuid, String defaultName) {
        String name = attributes.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the configure UUID corresponding to the input service UUID (as String).
     */
    public static String configCharacteristicsOfService(String serviceUuid, String defaultName) {
        String name = serverToConfig.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the data UUID corresponding to the input service UUID (as String).
     */
    public static String dataCharacteristicsOfService(String serviceUuid, String defaultName) {
        String name = serverToData.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the period UUID corresponding to the input service UUID (as String).
     */
    public static String periodCharacteristicsOfService(String serviceUuid, String defaultName) {
        String name = serverToPeriod.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the optimal period for the input service.
     */
    public static byte optimalPeriod(String serviceUuid) {
        if (serviceUuid.equals(UUID_IRT_SERV)) return (byte) 1000;
        else if (serviceUuid.equals(UUID_HUM_SERV)) return (byte) 1000;
        else if (serviceUuid.equals(UUID_BAR_SERV)) return (byte) 1000;
        else if (serviceUuid.equals(UUID_OPT_SERV)) return (byte) 1000;
        else if (serviceUuid.equals(UUID_MOV_SERV)) return (byte) 1000;
        else return (byte) 1000;
    }

    /**
     * Checks whether the input UUID corresponds to a service the user can ask for.
     */
    public static boolean validService(String uuid) {
        if (uuid.equals(UUID_IRT_SERV)) return true;
        else if (uuid.equals(UUID_HUM_SERV)) return true;
        else if (uuid.equals(UUID_BAR_SERV)) return true;
        else if (uuid.equals(UUID_OPT_SERV)) return true;
        else if (uuid.equals(UUID_MOV_SERV)) return true;
        return false;
    }
}
