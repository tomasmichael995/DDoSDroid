package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.AttackConstants;

/*
 * When an attack is created a network type is specified (internet, bluetooth, wifi p2p, etc...).
 * The ddos botnet must be connected to this network type in order to follow that attack.
 * */

public abstract class AttackNetwork {

    public interface OnConnectionListener {
        void onConnected();

        void onDisconnected(CharSequence reason);
    }

    protected Context context;
    protected OnConnectionListener connectionListener;
    protected BroadcastReceiver connectivityReceiver;
    protected IntentFilter connectivityIntentFilter;

    protected AttackNetwork(@NonNull Context context, OnConnectionListener listener) {
        this.context = context;
        this.connectionListener = listener;
        this.initializeConnectivityReceiver();
        this.initializeConnectivityIntentFilter();
    }

    public abstract void connect();

    public abstract void dissconnect();

    public abstract boolean isConnected();

    public final void registerConnectionReceiver() {
        context.registerReceiver(connectivityReceiver, connectivityIntentFilter);
    }

    public final void unregisterConnectionReceiver() {
        context.unregisterReceiver(connectivityReceiver);
    }

    public final void unregisterConnectionListener() {
        connectionListener = null;
    }

    protected abstract void initializeConnectivityReceiver();

    protected abstract void initializeConnectivityIntentFilter();

    public interface AttackNetworkFactory {
        AttackNetwork makeAttackNetwork(Context context, OnConnectionListener connectionListener, int attackNetworkType);
    }

    public static class AttackNetworkFactoryImp implements AttackNetworkFactory {
        private static final String TAG = "AttackNetworkFactoryImp";

        @Override
        public AttackNetwork makeAttackNetwork(Context context, OnConnectionListener connectionListener, int attackNetworkType) {
            switch (attackNetworkType) {
                case AttackConstants.NetworkType.INTERNET:
                    return new Internet(context, connectionListener);
                case AttackConstants.NetworkType.WIFI_P2P:
                    return new WifiP2p(context, connectionListener);
                case AttackConstants.NetworkType.NSD:
                    return new Nsd(context, connectionListener);
                case AttackConstants.NetworkType.BLUETOOTH:
                    return new Bluetooth(context, connectionListener);
                default:
                    throw new UnsupportedOperationException(TAG + ": unknown attack type");
            }
        }
    }
}
