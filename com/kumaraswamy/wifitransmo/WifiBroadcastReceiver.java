package com.kumaraswamy.wifitransmo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

import static android.net.wifi.p2p.WifiP2pManager.*;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private final WIFIActivity wifiActivity;
    private final WifiP2pManager wifiManager;
    private final WifiP2pManager.Channel wifiChannel;
    private boolean isConnected = false;

    public WifiBroadcastReceiver(WIFIActivity wifiActivity, WifiP2pManager wifiManager, WifiP2pManager.Channel wifiChannel) {
        this.wifiActivity = wifiActivity;
        this.wifiManager = wifiManager;
        this.wifiChannel = wifiChannel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case WIFI_P2P_PEERS_CHANGED_ACTION:
                if (wifiManager != null) {
                    wifiManager.requestPeers(wifiChannel, wifiActivity.peerListListener);
                }
            case WIFI_P2P_CONNECTION_CHANGED_ACTION:
                if (wifiManager != null) {
                    final NetworkInfo networkInfo = intent.getParcelableExtra(EXTRA_NETWORK_INFO);

                    if (networkInfo != null) {
                        if (networkInfo.isConnected()) {
                            isConnected = true;
                            wifiManager.requestConnectionInfo(wifiChannel, wifiActivity.connectionInfoListener);
                        } else {
                            if(isConnected) {
                                wifiActivity.disconnectListener.DeviceDisconnected();
                                isConnected = false;
                            }
                        }
                    }
                }
        }
    }
}
