package com.mapquest.android.maps;

import android.os.Handler;
import android.os.Message;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class EventDispatcher {
    private static final int ERROR = 70;
    private static final int MAP = 0;
    public static final int MAP_LOADED = 1;
    public static final int MAP_LONG_TOUCH = 4;
    public static final int MAP_PROVIDER_CHANGE = 2;
    public static final int MAP_TOUCH = 3;
    private static final int MOVE = 20;
    public static final int MOVE_END = 23;
    public static final int MOVE_START = 21;
    public static final int MOVING = 22;
    private static final int NETWORK = 60;
    public static final int NETWORK_CONNECTED = 61;
    public static final int NETWORK_DISCONNECTED = 62;
    private static final int OVERLAY = 40;
    public static final int OVERLAY_ADDED = 41;
    public static final int OVERLAY_REMOVED = 42;
    public static final int RESIZED = 5;
    public static final int ROTATING = 32;
    private static final int ROTATION = 30;
    public static final int ROTATION_END = 33;
    public static final int ROTATION_START = 31;
    public static final int TRACKBALL_ZOOM_TOGGLED = 6;
    private static final int TRAFFIC = 50;
    public static final int TRAFFIC_DISABLED = 52;
    public static final int TRAFFIC_ENABLED = 51;
    private static final int ZOOM = 10;
    public static final int ZOOM_END = 12;
    public static final int ZOOM_START = 11;
    public static final EventDispatcher instance = new EventDispatcher();
    public List<Handler> handlers = new CopyOnWriteArrayList();

    private EventDispatcher() {
    }

    public static void registerHandler(Handler handler) {
        if (handler != null) {
            instance.handlers.add(handler);
        }
    }

    public static void removeHandler(Handler handler) {
        if (handler != null) {
            instance.handlers.remove(handler);
        }
    }

    public static void removeAllHandlers() {
        instance.handlers.clear();
    }

    public static void sendEmptyMessage(int i) {
        for (Handler handler : instance.handlers) {
            if (Util.checkIfSameThread(handler)) {
                Message obtain = Message.obtain();
                obtain.what = i;
                handler.dispatchMessage(obtain);
            } else {
                handler.sendEmptyMessage(i);
            }
        }
    }

    public static void sendEmptyMessageDelayed(int i, long j) {
        for (Handler sendEmptyMessageDelayed : instance.handlers) {
            sendEmptyMessageDelayed.sendEmptyMessageDelayed(i, j);
        }
    }

    public static void sendMessage(Message message) {
        for (Handler handler : instance.handlers) {
            if (Util.checkIfSameThread(handler)) {
                handler.dispatchMessage(Message.obtain(message));
            } else {
                handler.sendMessage(Message.obtain(message));
            }
        }
    }

    public static void removeMessages(int i) {
        for (Handler removeMessages : instance.handlers) {
            removeMessages.removeMessages(i);
        }
    }
}
