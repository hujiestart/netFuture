package com.tuling.agent;

import javassist.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/22
 **/
public class WebAgent {


    // 拦截目标：javax.servlet.http.HttpServlet.service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("拦截servlet ");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"javax/servlet/http/HttpServlet".equals(className)) {
                    return null;
                }
                try {
                    return build(loader, className.replaceAll("/", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static byte[] build(ClassLoader loader, String name) throws Exception {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(name);
        CtMethod method = ctClass.getDeclaredMethod("service",
                pool.get(new String[]{"javax.servlet.http.HttpServletRequest",
                        "javax.servlet.http.HttpServletResponse"}));
        CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
        method.setName(method.getName() + "$agent");
        copyMethod.setBody("{\n" +
                "               Object trace= com.tuling.agent.WebAgent.begin($args);\n" +
                "                try {\n" +
                "                     " + copyMethod.getName() + "$agent($$);\n" +
                "                } finally {\n" +
                "                    com.tuling.agent.WebAgent.end(trace);\n" +
                "                }\n" +
                "            }");

        ctClass.addMethod(copyMethod);
        return ctClass.toBytecode();
    }


    // 插入到 service 方法的第一行
    //service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    public static Object begin(Object args[]) {
        HttpServletRequest request = (HttpServletRequest) args[0];
        HttpServletResponse response = (HttpServletResponse) args[1];
        WebTraceInfo trace = new WebTraceInfo();
        trace.setParams(request.getParameterMap());
        trace.setCookie(request.getCookies());
        trace.setUrl(request.getRequestURI());
        trace.setBegin(System.currentTimeMillis());
        String traceId = UUID.randomUUID().toString().replaceAll("-", "");
        TraceSession session = new TraceSession(traceId, "0");
        trace.traceId = traceId;
        trace.eventId = session.getParentId() + "." + session.getNextEventId();
        return trace;
    }

    // 插入到 service 方法的最后一行
    public static void end(Object webTraceInfo) {
        WebTraceInfo trace = (WebTraceInfo) webTraceInfo;
        trace.setUseTime(System.currentTimeMillis() - trace.getBegin());
        System.out.println(trace);
        TraceSession.getCurrentSession().close();
    }

    public static class WebTraceInfo {
        private String traceId;
        private String eventId;
        private Long begin;
        private String url;
        private Map<String, String[]> params;
        private Cookie[] cookie;
        private String handler;
        private Long useTime;

        public Long getBegin() {
            return begin;
        }

        public void setBegin(Long begin) {
            this.begin = begin;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String[]> getParams() {
            return params;
        }

        public void setParams(Map<String, String[]> params) {
            this.params = params;
        }

        public Cookie[] getCookie() {
            return cookie;
        }

        public void setCookie(Cookie[] cookie) {
            this.cookie = cookie;
        }

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        public Long getUseTime() {
            return useTime;
        }

        public void setUseTime(Long useTime) {
            this.useTime = useTime;
        }

        @Override
        public String toString() {
            return "WebTraceInfo{" +
                    "traceId='" + traceId + '\'' +
                    ", eventId='" + eventId + '\'' +
                    ", begin=" + begin +
                    ", url='" + url + '\'' +
                    ", params=" + params +
                    ", cookie=" + Arrays.toString(cookie) +
                    ", handler='" + handler + '\'' +
                    ", useTime=" + useTime +
                    '}';
        }
    }
}
