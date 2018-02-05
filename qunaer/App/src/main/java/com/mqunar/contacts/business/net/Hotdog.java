package com.mqunar.contacts.business.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.contacts.basis.upload.IUpload;
import com.mqunar.contacts.basis.upload.MutiPartUploadListener;
import com.mqunar.contacts.basis.upload.UploadListener;
import com.mqunar.contacts.basis.utils.CrashUtils;
import com.mqunar.contacts.business.net.param.UploadParam;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.ChiefGuard;
import com.mqunar.libtask.HotdogConductor;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.libtask.Ticket.RequestFeature;
import com.mqunar.patch.model.response.BaseResult;
import com.mqunar.patch.util.UCUtils;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.log.QLog;
import java.lang.reflect.Type;
import java.util.List;

public class Hotdog implements IUpload {
    private static final int MAX = 500;
    private Context mContext;

    public class ContactsTaskCallback implements TaskCallback {
        private List<Contact> contacts;
        private UploadListener listener;

        public ContactsTaskCallback(List<Contact> list, UploadListener uploadListener) {
            this.listener = uploadListener;
            this.contacts = list;
        }

        public void onMsgRequest(AbsConductor absConductor, boolean z) {
        }

        public void onMsgResult(AbsConductor absConductor, boolean z) {
            if (this.listener != null) {
                int i;
                try {
                    Object result = absConductor.getResult();
                    if (result instanceof byte[]) {
                        BaseResult baseResult = (BaseResult) JSON.parseObject((byte[]) result, (Type) BaseResult.class, new Feature[0]);
                        if (baseResult == null || baseResult.bstatus.code != 0) {
                            i = 0;
                        } else {
                            i = 1;
                        }
                    } else {
                        i = 0;
                    }
                } catch (Throwable th) {
                    QLog.e(th);
                    QLog.d("Contacts", "serious error... Mock sccuess!!!", new Object[0]);
                    i = 1;
                }
                if (i != 0) {
                    callSuccess();
                } else {
                    callError();
                }
            }
        }

        public void onMsgError(AbsConductor absConductor, boolean z) {
            callError();
        }

        public void onMsgProgress(AbsConductor absConductor, boolean z) {
        }

        public void onMsgStart(AbsConductor absConductor, boolean z) {
        }

        public void onMsgEnd(AbsConductor absConductor, boolean z) {
        }

        public void onMsgCancel(AbsConductor absConductor, boolean z) {
        }

        public void onMsgCacheHit(AbsConductor absConductor, boolean z) {
        }

        void callError() {
            if (this.listener != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (ContactsTaskCallback.this.listener instanceof MutiPartUploadListener) {
                            MutiPartUploadListener mutiPartUploadListener = (MutiPartUploadListener) ContactsTaskCallback.this.listener;
                            ContactsTaskCallback.this.listener.onFailure(String.format("Task[%d]-Total[%d]", new Object[]{Integer.valueOf(mutiPartUploadListener.curIndex), Integer.valueOf(mutiPartUploadListener.total)}));
                            return;
                        }
                        ContactsTaskCallback.this.listener.onFailure(null);
                    }
                });
            }
        }

        void callSuccess() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    if (ContactsTaskCallback.this.listener instanceof MutiPartUploadListener) {
                        MutiPartUploadListener mutiPartUploadListener = (MutiPartUploadListener) ContactsTaskCallback.this.listener;
                        ContactsTaskCallback.this.listener.onSuccess(ContactsTaskCallback.this.contacts, String.format("Task[%d]-Total[%d]", new Object[]{Integer.valueOf(mutiPartUploadListener.curIndex), Integer.valueOf(mutiPartUploadListener.total)}));
                        return;
                    }
                    ContactsTaskCallback.this.listener.onSuccess(ContactsTaskCallback.this.contacts, null);
                }
            });
        }
    }

    public Hotdog(Context context) {
        this.mContext = context;
    }

    public void uploadContacts(List<Contact> list, String str, UploadListener uploadListener) {
        try {
            if (CheckUtils.isEmpty(list)) {
                if (uploadListener != null) {
                    uploadListener.onFailure(null);
                }
            } else if (list.size() <= 500) {
                request(list, uploadListener);
            } else {
                int size = list.size();
                int i = size / 500;
                if (i * 500 != size) {
                    size = i + 1;
                } else {
                    size = i;
                }
                if (size > 4) {
                    request(list.subList(0, 500), uploadListener);
                    return;
                }
                QLog.d("Contacts", "upload HOTDOG , need count : " + size, new Object[0]);
                for (i = 0; i < size; i++) {
                    request(list.subList(i * 500, getMax(list, i)), new MutiPartUploadListener(i + 1, size, uploadListener));
                }
            }
        } catch (Throwable e) {
            QLog.e("Contacts", CrashUtils.getStackTraceString(e), new Object[0]);
        }
    }

    private int getMax(List<Contact> list, int i) {
        return Math.min(list.size(), (i + 1) * 500);
    }

    public void request(List<Contact> list, UploadListener uploadListener) {
        AbsConductor hotdogConductor = new HotdogConductor(new ContactsTaskCallback(list, uploadListener));
        UploadParam uploadParam = new UploadParam();
        uploadParam.phoneContacts = list;
        uploadParam.uuid = UCUtils.getInstance().getUuid();
        hotdogConductor.setParams(GlobalEnv.getInstance().getHotDogUrl(), "p_addPhoneContact", JSON.toJSONString(uploadParam));
        ChiefGuard.getInstance().addTask(this.mContext, hotdogConductor, RequestFeature.ADD_ONORDER);
    }
}
