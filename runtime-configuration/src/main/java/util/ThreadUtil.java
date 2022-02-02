package util;

public class ThreadUtil {

    private ThreadUtil(){}

    public static String getName(Thread t){
        return getName(t.getClass());
    }

    public static String getName(Class clazz){
        String name = clazz.getName();
        if(name.startsWith("io.lucky") == false){
            return name;
        }
        return "Lucky-" + name.substring(name.lastIndexOf('.') + 1);
    }

    public static void sleep(long ms){
        try{
            Thread.sleep(ms);
        }catch (InterruptedException e){
            ExceptionUtil.ignore(e);
        }
    }
}
