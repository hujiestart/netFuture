package com.tuling.agent;

import com.alibaba.dubbo.rpc.proxy.InvokerInvocationHandler;
import javassist.*;

import java.io.Serializable;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.ProtectionDomain;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Tommy
 * Created by Tommy on 2019/3/24
 **/
public class JdbcAgent {

    // 拦截目标：javax.servlet.http.HttpServlet.service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("拦截servlet ");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"com/mysql/jdbc/NonRegisteringDriver".equals(className)) {
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
        CtMethod method = ctClass.getDeclaredMethod("connect");
        CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
        method.setName(method.getName() + "$agent");
        copyMethod.setBody(" {\n" +
                "            return com.tuling.agent.JdbcAgent.proxyConnection(connect$agent($$));\n" +
                "        }");

        ctClass.addMethod(copyMethod);
        return ctClass.toBytecode();
    }


    public static Object begin(Connection connection, String sql) {
        JdbcStatistics jdbcStat = new JdbcStatistics();
        try {
            jdbcStat.jdbcUrl = connection.getMetaData().getURL();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jdbcStat.begin = System.currentTimeMillis();
        TraceSession session = TraceSession.getCurrentSession();
        if (session != null) {
            jdbcStat.traceId = session.getTraceId();
            jdbcStat.eventId = session.getParentId() + "." + session.getNextEventId();
        }
        jdbcStat.sql = sql;
        return jdbcStat;
    }

    public static void end(Object stat) {
        System.out.println(stat);
    }

    public static Connection proxyConnection(Connection conn) {
        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                new Class[]{Connection.class}, new ProxyConnection(conn));
    }

    public static PreparedStatement proxyStatement(PreparedStatement statement, Object stat) {
        return (PreparedStatement) Proxy.newProxyInstance(statement.getClass().getClassLoader(),
                new Class[]{PreparedStatement.class}, new PreparedStatementHandler(statement, stat));
    }

    public static class ProxyConnection implements InvocationHandler {
        // 动态代理方法
        // 所有的connection 方法执行前都要经过该方法
        Connection target; //原来的那个链接对象

        public ProxyConnection(Connection target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean isTargetMethod = "prepareStatement".equalsIgnoreCase(method.getName());
            Object stat = null;
            if (isTargetMethod) {
                // jdbc 执行事件开端
                stat = begin(target, (String) args[0]);
            }
            Object result = method.invoke(target, args);
            if (result instanceof PreparedStatement) {
                return proxyStatement((PreparedStatement) result, stat);
            }
            return result;
        }
    }


    /**
     * PreparedStatement 代理处理
     */
    public static class PreparedStatementHandler implements InvocationHandler {
        private final PreparedStatement statement;
        private final Object jdbcStat;

        public PreparedStatementHandler(PreparedStatement statement, Object jdbcStat) {
            this.statement = statement;
            this.jdbcStat = jdbcStat;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                result = method.invoke(statement, args);
            } catch (Throwable e) {
                throw e;
            } finally {
                if ("close".equals(method.getName())) {
                    end(jdbcStat);
                }
            }
            return result;
        }
    }

    // 实现 jdbc 数据采集器
    public static class JdbcStatistics implements Serializable {
        private String traceId;
        private String eventId;
        private Long useTime;
        public Long begin;// 时间戳
        // jdbc url
        public String jdbcUrl;
        // sql 语句
        public String sql;
        // 数据库名称
        public String databaseName;

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public Long getBegin() {
            return begin;
        }

        public void setBegin(Long begin) {
            this.begin = begin;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public Long getUseTime() {
            return useTime;
        }

        public void setUseTime(Long useTime) {
            this.useTime = useTime;
        }

        @Override
        public String toString() {
            return "JdbcStatistics{" +
                    "traceId='" + traceId + '\'' +
                    ", eventId='" + eventId + '\'' +
                    ", useTime='" + useTime + '\'' +
                    ", begin=" + begin +
                    ", jdbcUrl='" + jdbcUrl + '\'' +
                    ", sql='" + sql + '\'' +
                    ", databaseName='" + databaseName + '\'' +
                    '}';
        }
    }

}
