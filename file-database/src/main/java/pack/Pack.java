package pack;

import io.DataInputX;
import io.DataOutputX;

public interface Pack {

    PackType getPackType();

    DataOutputX write(DataOutputX out);
    Pack read(DataInputX in);
}
