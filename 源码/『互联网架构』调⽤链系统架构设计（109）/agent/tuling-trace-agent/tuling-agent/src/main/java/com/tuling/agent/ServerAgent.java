package com.tuling.agent;

import javassist.*;
import javassist.bytecode.AccessFlag;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/22
 **/
public class ServerAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("server 拦截");
        // 确定采集目标
        // 通配符
        //com.tuling.server.*Server&com.tuling.server.*Service
        args=args==null||args.trim().equals("")?"com.tuling.server.*Server":args;
        final WildcardMatcher matcher = new WildcardMatcher(args);
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className == null || loader == null) {
                    return null;
                }
                if (!matcher.matches(className.replaceAll("/", "."))) {
                    return null;
                }
                try {
                    return buildMonitorBytes(loader, className.replaceAll("/", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    // 改造一个 类已实现 参数的获取 和执行时间的获取
    private static byte[] buildMonitorBytes(ClassLoader loader, String className) throws Exception {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(className);
        //当前类所有的public方法 并且是非抽像，非静态，非本地（native）
        for (CtMethod method : ctClass.getDeclaredMethods()) {
            if (!AccessFlag.isPublic(method.getModifiers())) {
                continue;
            }
            if ((method.getModifiers() & AccessFlag.ABSTRACT) != 0) {
                continue;
            }
            if ((method.getModifiers() & AccessFlag.STATIC) != 0) {
                continue;
            }
            if ((method.getModifiers() & AccessFlag.NATIVE) != 0) {
                continue;
            }
            // 复制新的方法
            CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
            method.setName(method.getName() + "$agent");
            // 原方法 改为私有方法 否则在dubbo进行二次转换出现异常
            method.setModifiers(AccessFlag.setPrivate(method.getModifiers()));
            if (copyMethod.getReturnType().getName().equals("void")) {
                copyMethod.setBody("{\n" +
                        "               Object trace= com.tuling.agent.ServerAgent.begin($args);\n" +
                        "                try {\n" +
                        "                     " + copyMethod.getName() + "$agent($$);\n" +
                        "                } finally {\n" +
                        "                    com.tuling.agent.ServerAgent.end(trace);\n" +
                        "                }\n" +
                        "            }");
            } else {
                copyMethod.setBody("{\n" +
                        "               Object trace= com.tuling.agent.ServerAgent.begin($args);\n" +
                        "                try {\n" +
                        "                    return " + copyMethod.getName() + "$agent($$);\n" +
                        "                } finally {\n" +
                        "                    com.tuling.agent.ServerAgent.end(trace);\n" +
                        "                }\n" +
                        "            }");
            }
            ctClass.addMethod(copyMethod);
        }
        return ctClass.toBytecode();
    }

    public static Object begin(Object[] args) {
        TraceInfo t = new TraceInfo(System.currentTimeMillis(), args);
        if (TraceSession.getCurrentSession() != null) {
            t.traceId = TraceSession.getCurrentSession().getTraceId();
            t.eventId = TraceSession.getCurrentSession().getParentId() + "." +
                    TraceSession.getCurrentSession().getNextEventId();
        }
        return t;
    }

    public static void end(Object trace) {
        TraceInfo traceInfo = (TraceInfo) trace;
        System.out.println(traceInfo);
    }

    public static class TraceInfo {

        private String traceId;
        private String eventId;

        Long begin;
        Object args[];

        public TraceInfo() {
        }

        public TraceInfo(Long begin, Object[] args) {
            this.begin = begin;
            this.args = args;
        }

        public Long getBegin() {
            return begin;
        }

        public void setBegin(Long begin) {
            this.begin = begin;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }


        @Override
        public String toString() {
            return "TraceInfo{" +
                    "traceId='" + traceId + '\'' +
                    ", eventId='" + eventId + '\'' +
                    ", begin=" + begin +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
