package com.idig8.agent.test;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class ClassByteUtil {
    public static void main(String[] args) {
        String path = "/" + UserServiceImpl.class.getName().replaceAll("[.]", "/") + ".class";
        InputStream stream = ClassByteUtil.class.getResourceAsStream(path);
        // 读取 ClASS
        ClassReader reader = null;
        try {
            reader = new ClassReader(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 访问者模式
        reader.accept(new TraceClassVisitor(new PrintWriter(System.out)), ClassReader.SKIP_FRAMES);
    }
}
