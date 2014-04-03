package ua.edu.sumdu.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import ua.edu.sumdu.util.FileTreeNodeSerializer;
import ua.edu.sumdu.util.Mp3FileSerializer;

import javax.persistence.*;

@Entity
@Table(name = "files")
@JsonSerialize(using = Mp3FileSerializer.class)
public class Mp3File implements Comparable<Mp3File> {

    @Id
    @Column(name = "Id")
    @GeneratedValue
    private int id;

    @Column
    private String filename;

    @Column
    private long duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getShortName() {
        return this.filename.substring(this.filename.lastIndexOf("/") + 1);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mp3File mp3File = (Mp3File) o;

        if (duration != mp3File.duration) return false;
        if (id != mp3File.id) return false;
        if (filename != null ? !filename.equals(mp3File.filename) : mp3File.filename != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        return result;
    }

    @Override
    public int compareTo(Mp3File file) {
        return (equals(file)) ? 0 : Integer.compare(this.id, file.getId());
    }
}
