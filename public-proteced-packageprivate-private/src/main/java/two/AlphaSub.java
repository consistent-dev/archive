package two;

import one.Alpha;

public class AlphaSub extends Alpha {

    public void publicSubFoo(){};
    protected void protectedSubFoo(){};
    void packagePrivateSubFoo(){};
    private void privateSubFoo(){};

    void foo(){
        this.publicInt = 1;
        this.protecedInt = 2;








        // AlphaSub은 패키지가 달라서 packagePriateFoo() 불가
        // protected가 호출되려면 같은 패키지이거나, 서브 클래스여야 하는데
        // 서브 클래스가 아니라서 호출이 불가능
        Alpha alpha = new Alpha();
        alpha.publicFoo();


        // protected는 패키지가 달라도
        // 서브 클래스라면 호출할 수 있다.
        AlphaSub alphaSub = new AlphaSub();
        alphaSub.publicFoo();
        alphaSub.protectedFoo();
    }
}
