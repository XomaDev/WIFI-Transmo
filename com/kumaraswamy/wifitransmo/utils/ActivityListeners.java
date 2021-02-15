package com.kumaraswamy.wifitransmo.utils;

import java.util.List;

public class ActivityListeners {
    public interface DiscoveryStartedListener {
        void DiscoveryStarted();
    }

    public interface DiscoveryFailedListener {
        void DiscoveryFailed(int errorCode);
    }

    public interface DeviceListUpdateListener {
        void DeviceListUpdated(List<String> deviceNames, List<String> deviceAddresses);
    }

    public interface ConnectionListener {
        void DeviceConnected(boolean isHost);
    }

    public interface ReceivedNewMessageListener {
        void NewMessage(String message, boolean isFile);
    }

    public interface DisconnectListener {
        void DeviceDisconnected();
    }

    public interface SendDataListener {
        void SendData(int length);
    }

    public interface RequestStateChangedListener {
        void RequestStateChanged(int code);
    }
}
