package com.tuling.agent;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/22
 **/
public class TraceSession {
    // 本地线程
    static ThreadLocal<TraceSession> session = new ThreadLocal<>();
    private String traceId;
    private String parentId;
    private int currentEventId;

    // 开启会话了
    public TraceSession(String traceId, String parentId) {
        this.traceId = traceId;
        this.parentId = parentId;
        session.set(this);
    }

    // 当前事件ID
    public int getNextEventId() {
        return ++currentEventId;
    }

    //  获取会话
    public static TraceSession getCurrentSession() {
        return session.get();
    }

    // 关闭会话
    public static void close() {
        session.remove();
    }

    public static ThreadLocal<TraceSession> getSession() {
        return session;
    }

    public static void setSession(ThreadLocal<TraceSession> session) {
        TraceSession.session = session;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getCurrentEventId() {
        return currentEventId;
    }

    public void setCurrentEventId(int currentEventId) {
        this.currentEventId = currentEventId;
    }
}
