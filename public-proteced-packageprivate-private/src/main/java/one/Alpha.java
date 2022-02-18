package one;

public class Alpha {

    public int publicInt;
    protected int protecedInt;
    int packagePrivateInt;
    private int privateInt;

    void foo(){
        this.publicInt = 1;
        this.protecedInt = 2;
        this.packagePrivateInt = 3;
        this.privateInt = 4;


        this.privateFoo();
    }


    public void publicFoo(){};
    protected void protectedFoo(){};
    void packagePrivateFoo(){};
    private void privateFoo(){};
}
