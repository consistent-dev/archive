package pack;

import io.DataInputX;
import io.DataOutputX;

import java.util.HashMap;
import java.util.Map;

public class LoggingPack extends AbstractPack {

    private String category;
    private long line;
    private String content;
    private Map<String, String> tags = new HashMap<>();
    private Map<String, String> fields;

    public LoggingPack(){
        super(0,0);
    }

    public LoggingPack(long time, long id, String category, long line, String content) {
        super(time, id);
        this.category = category;
        this.line = line;
        this.content = content;
    }

    @Override
    public PackType getPackType() {
        return PackType.LOGGING;
    }

    @Override
    public DataOutputX write(DataOutputX out) {
        super.write(out);
        byte version = 0;
        out.writeByte(version);
        out.writeText(this.content);
        out.writeLong(this.line);
        out.writeMap(this.tags);
        out.writeMapNullable(this.fields);
        return out;
    }

    @Override
    public Pack read(DataInputX in) {
        super.read(in);
        byte version = in.readByte();
        this.category = in.readText();
        this.line = in.readLong();
        this.tags = in.readMap();
        this.fields = in.readMapNullable();
        return this;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append(", category=").append(category);
        sb.append(", line=").append(line);
        sb.append(", content=").append(content);
        sb.append(", tags=").append(tags);
        if(fields != null){
            sb.append(", fields=").append(fields);
        }
        return sb.toString();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getLine() {
        return line;
    }

    public void setLine(long line) {
        this.line = line;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void addTag(String key, String value){
        tags.put(key,value);
    }

    public Map<String, String> getFields() {
        return fields != null ? fields : null;
    }

    public void addField(String key, String value){
        if(fields == null){
            fields = new HashMap<>();
        }
        fields.put(key, value);
    }
}
