package qunar.lego.utils;

import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.Statistics;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.libtask.TaskCode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;

public class PitcherConductor extends AbsConductor {
    private byte[] content = null;
    private List<FormPart> formParts = null;
    private final HttpHeader reqHeader = new HttpHeader();
    private final HttpHeader respHeader = new HttpHeader();
    private String url;

    public PitcherConductor(TaskCallback... taskCallbackArr) {
        super(taskCallbackArr);
    }

    public void setParams(Object... objArr) {
        super.setParams(objArr);
        if (objArr != null) {
            int i = -1;
            while (true) {
                int i2 = i + 1;
                if (objArr.length > i2) {
                    Object obj = objArr[i2];
                    if (i2 == 0) {
                        this.url = (String) obj;
                    } else if (i2 == 1) {
                        if (obj != null && (obj instanceof byte[])) {
                            this.content = (byte[]) obj;
                        } else if (obj != null) {
                            try {
                                if (obj instanceof List) {
                                    this.formParts = (List) obj;
                                }
                            } catch (Exception e) {
                                throw new IllegalArgumentException("input params must be String, byte[]/List, Map ");
                            }
                        } else {
                            continue;
                        }
                    } else if (i2 == 2) {
                        if (obj != null && (obj instanceof Map)) {
                            this.reqHeader.addHeaders((Map) obj);
                        } else if (obj != null && (obj instanceof HttpHeader)) {
                            this.reqHeader.addHeaders((HttpHeader) obj);
                        }
                    }
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    protected void doingTask() {
        if (this.status.get() != TaskCode.TASK_CANCEL) {
            try {
                PitcherResponse request;
                putExtraData(Statistics.KEY_TASK_RUN_BEGIN, Long.valueOf(System.currentTimeMillis()));
                if (this.formParts != null) {
                    request = new Pitcher(this.train.context, this.url, this.formParts, this.reqHeader).request();
                } else {
                    request = new Pitcher(this.train.context, this.url, this.content, this.reqHeader).request();
                }
                putExtraData(Statistics.KEY_TASK_RUN_END, Long.valueOf(System.currentTimeMillis()));
                this.respHeader.addHeaders(request.respHeader);
                if (this.status.get() != TaskCode.TASK_CANCEL) {
                    if (request.e != null) {
                        this.error = -2;
                    } else if (request.respcode < HttpStatus.SC_BAD_REQUEST) {
                        this.status.set(TaskCode.TASK_RESULT);
                        try {
                            this.result = request.content;
                            this.resultTotal = (long) request.content.length;
                            this.currentLength = request.content.length;
                            this.msgd.onMessage(getStatus(), this);
                            return;
                        } catch (Exception e) {
                            this.error = -1;
                        }
                    } else {
                        this.error = -1;
                    }
                    this.status.set(TaskCode.TASK_ERROR);
                    this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                }
            } catch (Exception e2) {
                if (this.status.get() != TaskCode.TASK_CANCEL) {
                    this.error = -3;
                    this.status.set(TaskCode.TASK_ERROR);
                    this.msgd.onMessage(TaskCode.TASK_ERROR, this);
                }
            }
        }
    }

    protected int getEmpId() {
        return hashCode();
    }

    protected String getEmpName() {
        return this.url;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PitcherConductor)) {
            return false;
        }
        PitcherConductor pitcherConductor = (PitcherConductor) obj;
        if (!Arrays.equals(this.content, pitcherConductor.content)) {
            return false;
        }
        if (this.formParts == null ? pitcherConductor.formParts != null : !this.formParts.equals(pitcherConductor.formParts)) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(pitcherConductor.url)) {
                return true;
            }
        } else if (pitcherConductor.url == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        if (this.url != null) {
            hashCode = this.url.hashCode();
        } else {
            hashCode = 0;
        }
        int i2 = hashCode * 31;
        if (this.content != null) {
            hashCode = Arrays.hashCode(this.content);
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode + i2) * 31;
        if (this.formParts != null) {
            i = this.formParts.hashCode();
        }
        return hashCode + i;
    }

    public <T extends AbsConductor> boolean sameAs(T t) {
        boolean z = true;
        if (!(t instanceof PitcherConductor)) {
            return false;
        }
        String str = ((PitcherConductor) t).url;
        if (this.url == null) {
            return str == null;
        } else if (!this.url.equals(str)) {
            return false;
        } else {
            if (Arrays.equals(this.content, ((PitcherConductor) t).content)) {
                z = false;
            }
            return z;
        }
    }

    public Object findCache(boolean z) {
        return null;
    }

    public byte[] getResult() {
        return (byte[]) super.getResult();
    }

    public HttpHeader getRespHeader() {
        return this.respHeader;
    }
}
