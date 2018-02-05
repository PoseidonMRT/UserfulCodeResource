package com.mqunar.core.basectx.launcherfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class QFragmentManager {
    private static int currentLauncherFragmentActivityIndex = 0;
    private static LinkedList<FragmentItem> fragmentList = new LinkedList();
    private static int id = 0;
    private static LinkedList<Class> launcherActivitys = new LinkedList();

    public class FragmentItem {
        public Class<? extends LauncherFragmentActivityBase> ActivityClass;
        public Class<? extends Fragment> fragmentClass;
        public int id;
        public int state;
    }

    static {
        launcherActivitys.add(LauncherFragmentActivity1.class);
        launcherActivitys.add(LauncherFragmentActivity2.class);
        launcherActivitys.add(LauncherFragmentActivity3.class);
        launcherActivitys.add(LauncherFragmentActivity4.class);
        launcherActivitys.add(LauncherFragmentActivity5.class);
        launcherActivitys.add(LauncherFragmentActivity6.class);
        launcherActivitys.add(LauncherFragmentActivity7.class);
        launcherActivitys.add(LauncherFragmentActivity8.class);
        launcherActivitys.add(LauncherFragmentActivity9.class);
        launcherActivitys.add(LauncherFragmentActivity10.class);
        launcherActivitys.add(LauncherFragmentActivity11.class);
        launcherActivitys.add(LauncherFragmentActivity12.class);
        launcherActivitys.add(LauncherFragmentActivity13.class);
        launcherActivitys.add(LauncherFragmentActivity14.class);
        launcherActivitys.add(LauncherFragmentActivity15.class);
        launcherActivitys.add(LauncherFragmentActivity16.class);
        launcherActivitys.add(LauncherFragmentActivity17.class);
        launcherActivitys.add(LauncherFragmentActivity18.class);
        launcherActivitys.add(LauncherFragmentActivity19.class);
        launcherActivitys.add(LauncherFragmentActivity20.class);
        launcherActivitys.add(LauncherFragmentActivity21.class);
        launcherActivitys.add(LauncherFragmentActivity22.class);
        launcherActivitys.add(LauncherFragmentActivity23.class);
        launcherActivitys.add(LauncherFragmentActivity24.class);
        launcherActivitys.add(LauncherFragmentActivity25.class);
        launcherActivitys.add(LauncherFragmentActivity26.class);
        launcherActivitys.add(LauncherFragmentActivity27.class);
        launcherActivitys.add(LauncherFragmentActivity28.class);
        launcherActivitys.add(LauncherFragmentActivity29.class);
        launcherActivitys.add(LauncherFragmentActivity30.class);
    }

    public static Class getFreeLauncherFragmentActivity(Class<? extends Fragment> cls) {
        try {
            synchronized (fragmentList) {
                Iterator it = fragmentList.iterator();
                while (it.hasNext()) {
                    FragmentItem fragmentItem = (FragmentItem) it.next();
                    if (fragmentItem.fragmentClass == cls) {
                        Class cls2 = fragmentItem.ActivityClass;
                        return cls2;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cleanTopFragmentActivity(Class<? extends Fragment> cls) {
        try {
            synchronized (fragmentList) {
                Collection linkedList = new LinkedList();
                Iterator it = fragmentList.iterator();
                while (it.hasNext()) {
                    FragmentItem fragmentItem = (FragmentItem) it.next();
                    if (fragmentItem.fragmentClass == cls) {
                        break;
                    }
                    linkedList.add(fragmentItem);
                }
                fragmentList.removeAll(linkedList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printStack(String str) {
        synchronized (fragmentList) {
            Iterator it = fragmentList.iterator();
            while (it.hasNext()) {
                FragmentItem fragmentItem = (FragmentItem) it.next();
            }
        }
    }

    public static Class getLauncherFragmentActivity(Class<? extends Fragment> cls) {
        Class cls2;
        synchronized (fragmentList) {
            int size = currentLauncherFragmentActivityIndex % launcherActivitys.size();
            currentLauncherFragmentActivityIndex = size;
            currentLauncherFragmentActivityIndex++;
            id++;
            cls2 = (Class) launcherActivitys.get(size);
            if (fragmentList.size() < launcherActivitys.size()) {
                Class cls3 = cls2;
                while (true) {
                    Object obj;
                    Iterator it = fragmentList.iterator();
                    while (it.hasNext()) {
                        if (((FragmentItem) it.next()).ActivityClass == cls3) {
                            obj = null;
                            break;
                        }
                    }
                    size = 1;
                    if (obj != null) {
                        break;
                    }
                    size = currentLauncherFragmentActivityIndex % launcherActivitys.size();
                    currentLauncherFragmentActivityIndex = size;
                    currentLauncherFragmentActivityIndex++;
                    id++;
                    cls3 = (Class) launcherActivitys.get(size);
                }
                cls2 = cls3;
            }
            FragmentItem fragmentItem = new FragmentItem();
            fragmentItem.fragmentClass = cls;
            fragmentItem.ActivityClass = cls2;
            fragmentItem.id = getId();
            fragmentList.add(0, fragmentItem);
        }
        return cls2;
    }

    public static int getId() {
        return id;
    }

    public static void finishLauncherFragmentActivity(LauncherFragmentActivityBase launcherFragmentActivityBase) {
        synchronized (fragmentList) {
            Iterator it = fragmentList.iterator();
            while (it.hasNext()) {
                if (((FragmentItem) it.next()).id == launcherFragmentActivityBase._id) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public static void addSingleTopFragmentActivity(LauncherFragmentActivityBase launcherFragmentActivityBase, Class<? extends Fragment> cls) {
        synchronized (fragmentList) {
            Iterator it = fragmentList.iterator();
            if (it.hasNext() && ((FragmentItem) it.next()).fragmentClass != cls) {
                FragmentItem fragmentItem = new FragmentItem();
                fragmentItem.fragmentClass = cls;
                fragmentItem.ActivityClass = launcherFragmentActivityBase.getClass();
                currentLauncherFragmentActivityIndex++;
                id++;
                fragmentItem.id = id;
                launcherFragmentActivityBase._id = id;
                fragmentList.add(0, fragmentItem);
            }
        }
    }

    public static void onPauseLauncherFragmentActivity(LauncherFragmentActivityBase launcherFragmentActivityBase) {
        synchronized (fragmentList) {
            Iterator it = fragmentList.iterator();
            while (it.hasNext()) {
                FragmentItem fragmentItem = (FragmentItem) it.next();
                if (fragmentItem.id == launcherFragmentActivityBase._id) {
                    fragmentItem.state = 1;
                    break;
                }
            }
        }
    }

    public static void onResumeLauncherFragmentActivity(LauncherFragmentActivityBase launcherFragmentActivityBase) {
        synchronized (fragmentList) {
            Iterator it = fragmentList.iterator();
            while (it.hasNext()) {
                FragmentItem fragmentItem = (FragmentItem) it.next();
                if (fragmentItem.id == launcherFragmentActivityBase._id) {
                    fragmentItem.state = 0;
                }
            }
        }
    }

    static void addFragment(FragmentActivity fragmentActivity, Fragment fragment, String str) {
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (!fragmentActivity.isFinishing()) {
            beginTransaction.add(16908292, fragment, str);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    static Fragment getCurrFragment(FragmentActivity fragmentActivity) {
        return fragmentActivity.getSupportFragmentManager().findFragmentById(16908292);
    }

    static void replaceFragment(FragmentActivity fragmentActivity, Fragment fragment, String str) {
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (!fragmentActivity.isFinishing()) {
            beginTransaction.replace(16908292, fragment, str);
            beginTransaction.addToBackStack(str);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    static void replaceFragmentNotStack(FragmentActivity fragmentActivity, Fragment fragment, String str) {
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (!fragmentActivity.isFinishing()) {
            beginTransaction.replace(16908292, fragment, str);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    public static Fragment findFragmentByTag(FragmentActivity fragmentActivity, String str) {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag(str);
    }

    public static void hideFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (!fragmentActivity.isFinishing()) {
            beginTransaction.hide(fragment);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    public static void showFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (!fragmentActivity.isFinishing()) {
            beginTransaction.show(fragment);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    public static void removeFragment(FragmentActivity fragmentActivity, Fragment fragment, String str) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        if (!fragmentActivity.isFinishing()) {
            beginTransaction.remove(fragment);
            beginTransaction.commitAllowingStateLoss();
            supportFragmentManager.popBackStack(str, 1);
        }
    }

    public static boolean isHaveFragments(FragmentActivity fragmentActivity) {
        return fragmentActivity.getSupportFragmentManager().getBackStackEntryCount() <= 0;
    }
}
