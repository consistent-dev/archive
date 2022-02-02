import util.ParamText;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        ParamText paramText = new ParamText("logsink-${category1}-${category2}.conf");
        String category = "testCategoryName";
        System.out.println(paramText.replaceAll(category));

        Map<Object, Object> arg = new HashMap<>();
        arg.put("category1", "testCategoryName_1");
        arg.put("category2", "testCategoryName_2");
        System.out.println(paramText.replaceAllMap(arg));
    }
}
