package ref;

/**
 * 단순히 객체를 감싸기 위한 클래스.
 * if(o instance of OBJECT) 등의 목적으로 사용한다.
 */
public class OBJECT<V> {
    public V value;

    public OBJECT(){}

    public OBJECT(V value){
        this.value = value;
    }

    public V getValue(){
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  OBJECT){
            OBJECT other = (OBJECT) o;
            if(this.value == null){
                return this.value == other.value;
            }else{
                return this.value.equals(other.value);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }
}
