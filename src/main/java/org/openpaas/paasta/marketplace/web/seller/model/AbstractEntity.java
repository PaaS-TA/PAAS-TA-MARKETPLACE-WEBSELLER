package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;
import org.openpaas.paasta.marketplace.web.seller.util.SecurityUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Data
public abstract class AbstractEntity {
    protected UseYn useYn;
    protected String createdId;
    protected String updatedId;
    protected Date createdDate;
    protected Date updatedDate;

    @PrePersist
    public void prePersist() {
        useYn = UseYn.Y;
        createdId = SecurityUtils.getUserId();
        createdDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedId = SecurityUtils.getUserId();
        updatedDate = new Date();
    }

    public enum UseYn {
        Y, N, All,
    };

}
