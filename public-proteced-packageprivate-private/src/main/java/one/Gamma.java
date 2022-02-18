package one;

public class Gamma {

    void foo(){
        // 같은 패키지라서 3개 가능
        Alpha alpha = new Alpha();
        alpha.publicFoo();
        alpha.protectedFoo();
        alpha.packagePrivateFoo();
    }
}
