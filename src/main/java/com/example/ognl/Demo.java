package com.example.ognl;

import com.taobao.arthas.core.advisor.Advice;
import com.taobao.arthas.core.advisor.ArthasMethod;
import com.taobao.arthas.core.command.express.Express;
import com.taobao.arthas.core.command.express.ExpressException;
import com.taobao.arthas.core.command.express.ExpressFactory;
import com.taobao.arthas.core.util.ThreadLocalWatch;
import com.taobao.arthas.core.view.ObjectView;

/**
 * 
 * @author hengyunabc 2021-03-19
 *
 */
public class Demo {
    protected static final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        // 演示 watch在调用 TestService 具体是怎么工作的
        // 条件表达式， 结果表达式是怎样的
        TestService testService = new TestService();

        int i = 1000;
        String str = "hello";
        Student student = new Student(1, "tom");

        ClassLoader loader = testService.getClass().getClassLoader();
        Class<?> clazz = testService.getClass();
        ArthasMethod method = new ArthasMethod(clazz, "test", "");
        Object target = testService; // this
        Object[] params = new Object[3];
        params[0] = i;
        params[1] = str;
        params[2] = student;

        try {
            atBefore(loader, clazz, method, target, params);
            Student returnObj = testService.test(i, str, student);
            atExit(loader, clazz, method, target, params, returnObj);
        } catch (Throwable throwExp) {
            atExceptionExit(loader, clazz, method, target, params, throwExp);
        }

    }

    /**
     * 
     * <pre>
     * watch com.example.ognl.TestService test "{target, params}" "params[0] > 1" -b -x 3
     * </pre>
     */
    public static void atBefore(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target,
            Object[] params) {
        threadLocalWatch.start();

        Advice advice = Advice.newForBefore(loader, clazz, method, target, params);
        Express express = ExpressFactory.threadLocalExpress(advice);

        String watchExpress = "{target, params}";
        String conditionExpress = "params[0] > 1";

        try {
            boolean conditionResult = express.is(conditionExpress);
            System.out.println(
                    "AtEnter, conditionExpress: " + conditionExpress + ", conditionResult: " + conditionResult);
            if (conditionResult) {
                Object object = express.get(watchExpress);
                ObjectView objectView = new ObjectView(object, 3);
                String draw = objectView.draw();
                System.out.println(draw);
            }
        } catch (ExpressException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * <pre>
     * watch com.example.ognl.TestService test "{target, params, returnObj, #cost}" "params[0] > 1 && #cost > 0.1" -x 3
     * </pre>
     */
    public static void atExit(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] params,
            Object returnObj) {
        double cost = threadLocalWatch.costInMillis();

        Advice advice = Advice.newForAfterRetuning(loader, clazz, method, target, params, returnObj);
        Express express = ExpressFactory.threadLocalExpress(advice).bind("cost", cost);

        String watchExpress = "{target, params, returnObj, #cost}";
        String conditionExpress = "params[0] > 1 && #cost > 0.1";

        try {
            boolean conditionResult = express.is(conditionExpress);
            System.out
                    .println("AtExit, conditionExpress: " + conditionExpress + ", conditionResult: " + conditionResult);
            if (conditionResult) {
                Object object = express.get(watchExpress);
                ObjectView objectView = new ObjectView(object, 3);
                String draw = objectView.draw();
                System.out.println(draw);
            }
        } catch (ExpressException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * <pre>
     * watch com.example.ognl.TestService test "{target, params, throwExp}" "params[0] > 1" -e -x 2
     * </pre>
     */
    public static void atExceptionExit(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target,
            Object[] params, Throwable throwExp) {
        Advice advice = Advice.newForAfterThrowing(loader, clazz, method, target, params, throwExp);
        Express express = ExpressFactory.threadLocalExpress(advice);

        String watchExpress = "{target, params, throwExp}";
        String conditionExpress = "params[0] > 1";

        try {
            boolean conditionResult = express.is(conditionExpress);
            System.out.println(
                    "AtExceptionExit, conditionExpress: " + conditionExpress + ", conditionResult: " + conditionResult);
            if (conditionResult) {
                Object object = express.get(watchExpress);
                ObjectView objectView = new ObjectView(object, 2);
                String draw = objectView.draw();
                System.out.println(draw);
            }
        } catch (ExpressException e) {
            e.printStackTrace();
        }
    }

}
