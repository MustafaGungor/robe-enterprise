package com.mebitech.robe.persistence.jpa.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by kamilbukum on 06/03/2017.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(length = 32)
    private String oid;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    private Date updateDate;
    @Version
    private Long version;


    public BaseEntity(){
        this.oid =  UUID.randomUUID().toString().replace("-","");
    }

    public BaseEntity(String oid) {
        this.oid = oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @PrePersist
    public void onPrePersist() {
        setCreateDate(new Date());
    }

    @PreUpdate
    public void onPreUpdate() {
        setUpdateDate(new Date());
    }


    @Override
    public String toString() {
        return "BaseEntity{" +
                "oid='" + oid + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        if (getOid() != null ? !getOid().equals(that.getOid()) : that.getOid() != null) return false;
        if (getCreateDate() != null ? !getCreateDate().equals(that.getCreateDate()) : that.getCreateDate() != null)
            return false;
        if (getUpdateDate() != null ? !getUpdateDate().equals(that.getUpdateDate()) : that.getUpdateDate() != null)
            return false;
        return getVersion() != null ? getVersion().equals(that.getVersion()) : that.getVersion() == null;
    }

    @Override
    public int hashCode() {
        int result = getOid() != null ? getOid().hashCode() : 0;
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getUpdateDate() != null ? getUpdateDate().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }
}
