package ua.edu.sumdu.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tracks")
public class Track{

    @Id
    @Column(name = "Id")
    @GeneratedValue
    private Integer id;

    @Column
    private int fileId;
    
    @Column
    private Date date;
    
    @Column
    private Integer timePart;

    @Column
    private Integer position;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTimePart() {
        return timePart;
    }

    public void setTimePart(Integer timePart) {
        this.timePart = timePart;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
