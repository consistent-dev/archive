package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ConfigValueUtil {

    private ConfigValueUtil(){}

    /**
     * 시스템 설정값을 추가한다. 파일 설정에서 파라미터를 사용해서 시스템 설정값을 지정했으면 대체해서 사용한다.
     *
     * ex) my-server.conf에서 user_name=${user.name}으로 지정한 경우, 시스템 설정값에서 user.name을 찾아 user_name에 할당한다.
     *
     * @param temp 파일에서 읽은 설정값
     * @return
     */
    public static Properties replaceSysProp(Properties temp) {
        Properties p = new Properties();
        Map<Object, Object> args = new HashMap<Object, Object>();
        args.putAll(System.getenv());
        args.putAll(System.getProperties());
        p.putAll(args);

        // 파일에서 시스템 설정값을 사용하도록 파라미터를 지정한 경우
        Iterator<Object> itr = temp.keySet().iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            String value = (String) temp.get(key);
            if(key!=null) {
                p.put(key, new ParamText(StringUtil.trim(value)).replaceAllMap(args));
            }
        }
        return p;
    }

}
