package com.mqunar.cock.network;

import com.mqunar.cock.model.BaseResult;
import com.mqunar.cock.model.YaccaListResult;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.log.QLog;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class i extends GenericNetworkTaskCallback<YaccaListResult> {
    final /* synthetic */ OneKeyCremation a;

    i(OneKeyCremation oneKeyCremation, Class cls, RemoteSvcProxy remoteSvcProxy) {
        this.a = oneKeyCremation;
        super(cls, remoteSvcProxy);
    }

    protected /* synthetic */ void onDataArrive(BaseResult baseResult) {
        a((YaccaListResult) baseResult);
    }

    protected void onNetError(AbsConductor absConductor) {
        QLog.d("OneKeyCremation loadYacaAsync failure", new Object[0]);
        this.a.setState(this.a.yaccaListUnavailableState);
    }

    protected void a(YaccaListResult yaccaListResult) {
        QLog.d("OneKeyCremation loadYacaAsync onDataArrive", new Object[0]);
        if (yaccaListResult == null || yaccaListResult.data == null) {
            this.a.setState(this.a.yaccaListUnavailableState);
            return;
        }
        Collection collection = yaccaListResult.data.servers;
        if (yaccaListResult.data.delay != 0) {
            this.a.c.compareAndSet(false, true);
            this.a.setState(this.a.yaccaListUnavailableState);
            this.a.i.postDelayed(new j(this), (long) (yaccaListResult.data.delay * 1000));
        } else if (ArrayUtils.isEmpty(collection)) {
            this.a.setState(this.a.yaccaListUnavailableState);
        } else {
            this.a.h = Collections.synchronizedList(collection);
            this.a.a((List) collection);
            this.a.setState(this.a.yaccaConnectingState);
        }
    }

    protected boolean handleBizError(AbsConductor absConductor, BaseResult baseResult) {
        QLog.d("OneKeyCremation loadYacaAsync handlebizerror", new Object[0]);
        this.a.setState(this.a.yaccaListUnavailableState);
        return super.handleBizError(absConductor, baseResult);
    }
}
