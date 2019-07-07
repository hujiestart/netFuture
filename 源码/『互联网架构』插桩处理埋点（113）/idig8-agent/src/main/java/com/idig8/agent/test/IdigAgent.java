package com.idig8.agent.test;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class IdigAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("premain：" + args);

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"com/idig8/agent/test/UserServiceImpl".equals(className)) {
                    return null;
                }
                // javassist 工具 改造
                try {
                    ClassPool pool = new ClassPool();
                    pool.insertClassPath(new LoaderClassPath(loader));
                    CtClass ctclass = pool.get("com.idig8.agent.test.UserServiceImpl");
                    CtMethod method = ctclass.getDeclaredMethod("hello");
                    method.insertBefore(" System.out.println(System.currentTimeMillis());");
                    method.insertBefore("long begin = System.currentTimeMillis();"
                            +"       System.out.println(begin);");

                    method.insertAfter(" long end = System.currentTimeMillis();\n" +
                            "        System.out.println(end - begin);");
                    return ctclass.toBytecode();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (CannotCompileException e) {
                    e.printStackTrace();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }
}
