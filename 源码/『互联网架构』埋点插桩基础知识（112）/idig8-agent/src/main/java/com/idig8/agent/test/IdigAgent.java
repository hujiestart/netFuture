package com.idig8.agent.test;

import java.lang.instrument.Instrumentation;

public class IdigAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("premain：" + args
        );
    }
}
