package util;

public class StopWatch {

    private long stime;
    private long etime;

    public StopWatch(){
        this.stime = System.currentTimeMillis();
        this.etime = 0;
    }

    public void start(){
        this.stime = System.currentTimeMillis();
        this.etime = 0;
    }

    public long stop(){
        if(this.etime == 0){
            this.etime = System.currentTimeMillis();
        }
        return etime - stime;
    }

}
