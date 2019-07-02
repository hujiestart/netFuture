package com.tuling.agent;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcInvocation;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/24
 **/
public class DubboProvideAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("dubbo provide 拦截");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"com/alibaba/dubbo/rpc/filter/ClassLoaderFilter".equals(className)) {
                    return null;
                }
                try {
                    return buildBytes(loader, className.replaceAll("/", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static byte[] buildBytes(ClassLoader loader, String target) throws Exception {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(target);
        CtMethod method = ctClass.getDeclaredMethod("invoke");
        CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
        method.setName(method.getName() + "$agent");
        copyMethod.setBody("{\n" +
                "               Object trace= com.tuling.agent.DubboProvideAgent.begin($args);\n" +
                "                try {\n" +
                "                     return " + copyMethod.getName() + "$agent($$);\n" +
                "                } finally {\n" +
                "                   com.tuling.agent.DubboProvideAgent.end(trace);\n" +
                "                }\n" +
                "            }");

        ctClass.addMethod(copyMethod);
        return ctClass.toBytecode();
    }

    public static Object begin(Object[] args) {
        Invoker i = (Invoker) args[0];
        RpcInvocation rpcInvocation = (RpcInvocation) args[1];
        String traceId = rpcInvocation.getAttachment("_traceId");
        String parentId = rpcInvocation.getAttachment("_parentId");
        System.out.println("服务接收 traceId=" + traceId);
        // 开启会话
        TraceSession session = new TraceSession(traceId, parentId);
        return new Object();
    }

    public static void end(Object arg) {
        // 关闭会话
        TraceSession.getCurrentSession().close();
    }
}