package io.tyloo.api;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *
 * 事务上下文
 *
 * @Author:Zh1Cheung 945503088@qq.com
 * @Date: 12:01 2019/12/4
 *
 */
public class TransactionContext implements Serializable {

    private static final long serialVersionUID = -8199390103169700387L;
    /**
     * 事务编号
     */
    private TransactionXid xid;

    /**
     * 事务状态
     */
    private int status;
    /**
     * 附加属性.
     */
    private Map<String, String> attachments = new ConcurrentHashMap<String, String>();

    public TransactionContext() {

    }

    /**
     * 构建事务上下文对像.
     *
     * @param xid
     * @param status
     */
    public TransactionContext(TransactionXid xid, int status) {
        this.xid = xid;
        this.status = status;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public TransactionXid getXid() {
        return xid.clone();
    }

    public void setAttachments(Map<String, String> attachments) {
        if (attachments != null && !attachments.isEmpty()) {
            this.attachments.putAll(attachments);
        }
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }


}