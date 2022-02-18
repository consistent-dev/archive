package one;

import two.AlphaSub;

public class Beta extends Alpha {

    void foo(){
        // 같은 패키지라서 this 3개 가능
        this.publicInt = 1;
        this.protecedInt = 2;
        this.packagePrivateInt = 3;

        // 같은 패키지라서 this 3개 가능
        this.publicFoo();
        this.protectedFoo();
        this.packagePrivateFoo();

        // Alpha와 Beta가 같은 패키지라서 3개 가능
        Alpha alpha = new Alpha();
        alpha.publicFoo();
        alpha.protectedFoo();
        alpha.packagePrivateFoo();

        // AlphaSub를 사용하면
        // packagePrivateFoo는 two에 존재하는 메소드
        // 따라서 two와 one이 패키지가 달라서 호출될 수 없다.
        AlphaSub alphaSub = new AlphaSub();
        alphaSub.publicFoo();
        alphaSub.protectedFoo();

        // alphaSub에는 packagePrivateFoo가 없다.
//        alphaSub.packagePrivateFoo();

        // AlphaSub과 Beta는 패키지가 달라서
        // AlphaSub의 멤버는 1개만 가능
        alphaSub.publicSubFoo();

    }
}
