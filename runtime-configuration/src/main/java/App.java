import conf.Configure;
import util.ConfigValueUtil;
import util.ThreadUtil;

public class App {

    public static void main(String[] args) {
        for(int i=0; i<10; i++){
            ThreadUtil.sleep(1000);
            System.out.println(Configure.getInstance().debug_test);
            System.out.println(Configure.getInstance().user_name);
        }

        if(Configure.getInstance().debug_test){
            // debug logging
        }
    }
}
