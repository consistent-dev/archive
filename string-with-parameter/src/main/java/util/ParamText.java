package util;

import ref.OBJECT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamText {

    protected String startBrace;
    protected String endBrace;
    protected List<Object> tokenList = new ArrayList<>(); // 토큰 리스트. 평문은 String, 파라미터는 OBJECT 타입으로 add한다.
    protected List<String> paramList = new ArrayList<>(); // 파라미터 리스트

    public ParamText(String plainText){
        this(plainText, "${", "}");
    }

    private ParamText(String plainText, String startBrace, String endBrace){
        this.startBrace = startBrace;
        this.endBrace = endBrace;
        while(plainText.length() > 0){
            int pos = plainText.indexOf(startBrace);
            if(pos < 0){ // 파라미터가 없는 경우
                this.tokenList.add(plainText);
                return;
            }else if(pos > 0){ // 파라미터가 중간에 있는 경우
                this.tokenList.add(plainText.substring(0, pos));
                plainText = plainText.substring(pos);
            }else{ // 파라미터가 제일 앞에 있는 경우
                pos += startBrace.length();
                int nextPos = plainText.indexOf(endBrace, pos);
                if(nextPos < 0){
                    break;
                }
                String paramName = plainText.substring(pos, nextPos).trim();
                this.paramList.add(paramName);
                this.tokenList.add(new OBJECT(paramName));
                plainText = plainText.substring(nextPos + endBrace.length());
            }
        }
    }

    /**
     * 모든 파라미터를 하나의 값으로 대체한다.
     *
     * @param arg 모든 파라미터를 대체할 값
     * @return
     */
    public String replaceAll(Object arg){
        StringBuffer buffer = new StringBuffer();
        for(Object o : tokenList){
            if(o instanceof OBJECT){ // 파라미터인 경우 대체
                buffer.append(arg);
            }else{
                buffer.append(o);
            }
        }
        return buffer.toString();
    }

    /**
     * 파라미터를 대체할 Map을 전달한다.
     *
     * @param arg 파라미터를 대체할 값
     * @return
     */
    public String replaceAllMap(Map<Object, ?> arg){
        StringBuffer buffer = new StringBuffer();
        for(Object o : tokenList){
            if(o instanceof OBJECT){ // 파라미터인 경우 대체
                OBJECT<String> p = (OBJECT<String>) o;
                if(arg.containsKey(p.value)){
                    buffer.append(arg.get(p.value));
                }else{
                    buffer.append(o);
                }
            }else{
                buffer.append(o);
            }
        }
        return buffer.toString();
    }
}
