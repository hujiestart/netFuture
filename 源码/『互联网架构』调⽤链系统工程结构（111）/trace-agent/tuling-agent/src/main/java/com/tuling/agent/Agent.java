package com.tuling.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/22
 **/
public class Agent {
    public static void premain(String args, Instrumentation instrumentation) {
        WebAgent.premain(args, instrumentation);
        ServerAgent.premain(args, instrumentation);
        DubboConsumerAgent.premain(args, instrumentation);
        DubboProvideAgent.premain(args, instrumentation);
        JdbcAgent.premain(args, instrumentation);
    }
}
