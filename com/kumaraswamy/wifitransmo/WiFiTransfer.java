package com.kumaraswamy.wifitransmo;

import android.app.Activity;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.YailList;
import com.kumaraswamy.wifitransmo.utils.ActivityListeners;
import com.kumaraswamy.wifitransmo.utils.DataUtil;

import java.util.List;

@UsesPermissions(permissionNames = "android.permission.ACCESS_WIFI_STATE, android.permission.CHANGE_WIFI_STATE, android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE, android.permission.ACCESS_FINE_LOCATION, android.permission.ACCESS_COARSE_LOCATION, android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")
@DesignerComponent(
        versionName = "1.0.0 Transmo",
        version = 1,
        category = ComponentCategory.EXTENSION,
        description = "Extension made to send data from one device to another using Wi-Fi Peer to Peer connection or also Wi-Fi Direct. An open-source extension developed by Kumaraswamy B.G",
        nonVisible = true,
        iconName = "aiwebres/icon.png")

@SimpleObject(external = true)

public class WiFiTransfer extends AndroidNonvisibleComponent implements ActivityListeners.DeviceListUpdateListener,
        ActivityListeners.DiscoveryStartedListener, ActivityListeners.DiscoveryFailedListener,
        ActivityListeners.ConnectionListener, ActivityListeners.ReceivedNewMessageListener, ActivityListeners.DisconnectListener,
        ActivityListeners.SendDataListener, ActivityListeners.RequestStateChangedListener {

    public static Activity activity;
    public static WIFIActivity wifiActivity;

    public static int connectionPort;
    public static int connectionTimeout;

    private static final int DEFAULT_CONNECTION_PORT = 8888;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 750;

    public WiFiTransfer(ComponentContainer container) {
        super(container.$form());

        activity = container.$context();
        wifiActivity = new WIFIActivity(activity, this, this, this, this, this, this, this, this);

        connectionPort = DEFAULT_CONNECTION_PORT;
        connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = DEFAULT_CONNECTION_PORT + "")
    @SimpleProperty
    public void Port(int port) {
        connectionPort = port;
    }

    @SimpleProperty
    public int Port() {
        return connectionPort;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = DEFAULT_CONNECTION_TIMEOUT + "")
    @SimpleProperty
    public void ConnectionTimeout(int timeout) {
        connectionTimeout = timeout;
    }

    @SimpleProperty
    public int ConnectionTimeout() {
        return connectionTimeout;
    }

    @SimpleFunction(description = "Search for devices nearby")
    public void StartDiscovery() {
        if(wifiActivity != null) wifiActivity.startDeviceDiscovery();
    }

    @SimpleFunction(description = "Stop searching for devices")
    public void StopDiscovery() {
        if(wifiActivity != null) wifiActivity.stopDiscovery();
    }

    @SimpleFunction(description = "Disconnect from the connected device")
    public void Disconnect() {
        if(wifiActivity != null) wifiActivity.disconnect();
    }

    @SimpleFunction(description = "Check if device is connected")
    public boolean Connected() {
        return wifiActivity == null ? false : wifiActivity.connected();
    }

    @SimpleFunction(description = "Check if this device is the host")
    public boolean IsHost() {
        return wifiActivity == null ? false : wifiActivity.isHost();
    }

    @SimpleFunction
    public int BytesAvailableToReceive() {
        return wifiActivity == null ? 0 : wifiActivity.bytesAvailableToReceive();
    }

    @SimpleFunction(description = "Read text from the given number of bytes")
    public void ReadText(int bytes) {
        if(wifiActivity != null) wifiActivity.readText(bytes == -1 ? BytesAvailableToReceive() : bytes);
    }

    @SimpleFunction(description = "Start file receiver")
    public void ReceiveFile(String savePath, int bytes) {
        if(wifiActivity != null) wifiActivity.readFile(savePath, bytes);
    }

    @SimpleFunction(description = "Merges files into one")
    public void MergeFiles(YailList fileNames, String saveTo) {
        DataUtil.mergeFiles(fileNames.toStringArray(), saveTo);
    }

    @SimpleFunction(description = "Connect to device using the device address")
    public void Connect(int index) {
        wifiActivity.connectDevice(index - 1);
    }

    @SimpleFunction(description = "Start message to the connected device")
    public void SendMessage(String message) {
        if(wifiActivity != null) wifiActivity.sendBytes(message.getBytes());
    }

    @SimpleFunction(description = "Send file to the connected device")
    public void SendFile(String fileName) {
        if(wifiActivity != null) {
            byte[] bytes = DataUtil.readFromFile(fileName);
            wifiActivity.sendBytes(bytes);
        }
    }

    @SimpleFunction(description = "Send list of bytes to the connected device")
    public void SendBytes(YailList list) {
        if(wifiActivity != null) {
            byte[] bytes = DataUtil.makeByteArray(list.toArray());
            wifiActivity.sendBytes(bytes);
        }
    }

    @SimpleEvent(description = "Event fired when device list updated")
    public void DevicesAvailable(List names, List addresses) {
        EventDispatcher.dispatchEvent(this, "DevicesAvailable", names, addresses);
    }

    @SimpleEvent(description = "Event fired when discovery started")
    public void StartedDiscovery() {
        EventDispatcher.dispatchEvent(this, "StartedDiscovery");
    }

    @SimpleEvent(description = "Event fired when discovery failed")
    public void StartDiscoveryFailed(int errorCode) {
        EventDispatcher.dispatchEvent(this, "StartDiscoveryFailed", errorCode);
    }

    @SimpleEvent(description = "Event fired when connection is made")
    public void Connected(boolean isHost) {
        EventDispatcher.dispatchEvent(this, "Connected", isHost);
    }

    @SimpleEvent(description = "Event fired when device disconnected")
    public void Disconnected() {
        EventDispatcher.dispatchEvent(this, "Disconnected");
    }

    @SimpleEvent(description = "Event fired when received new message")
    public void DataReceived(String data, boolean isFile) {
        EventDispatcher.dispatchEvent(this, "DataReceived", data, isFile);
    }

    @SimpleEvent(description = "Event fires when data is sent")
    public void SentData(int length) {
        EventDispatcher.dispatchEvent(this, "SentData", length);
    }

    @SimpleEvent(description = "Raised when sent connection request to another device or it's failed")
    public void ConnectionRequestResult(int code) {
        EventDispatcher.dispatchEvent(this, "ConnectionRequestResult", code);
    }

    @Override
    public void DeviceListUpdated(List<String> deviceNames, List<String> deviceAddresses) {
        DevicesAvailable(deviceNames, deviceAddresses);
    }

    @Override
    public void DiscoveryStarted() {
        StartedDiscovery();
    }

    @Override
    public void DiscoveryFailed(int errorCode) {
        StartDiscoveryFailed(errorCode);
    }

    @Override
    public void DeviceConnected(boolean isHost) {
        StopDiscovery();
        Connected(isHost);
    }

    @Override
    public void NewMessage(String message, boolean isFile) {
        DataReceived(message, isFile);
    }

    @Override
    public void DeviceDisconnected() {
        Disconnected();
    }

    @Override
    public void SendData(int length) {
        SentData(length);
    }

    @Override
    public void RequestStateChanged(int code) {
        ConnectionRequestResult(code);
    }
}
