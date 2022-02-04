package pack;

import io.DataInputX;
import io.DataOutputX;

public abstract class AbstractPack implements Pack {

    protected long time;
    protected long id;

    protected AbstractPack(long time, long id){
        this.time = time;
        this.id = id;
    }

    public abstract PackType getPackType();

    @Override
    public DataOutputX write(DataOutputX out) {
        byte version = 10;
        out.writeByte(version);
        out.writeDecimal(id);
        out.writeLong(time);
        return out;
    }

    @Override
    public Pack read(DataInputX in) {
        byte version = in.readByte();
        this.id = in.readDecimal();
        this.time = in.readLong();
        return this;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("time=").append(time);
        sb.append(", pcode=").append(id);
        return sb.toString();
    }
}
