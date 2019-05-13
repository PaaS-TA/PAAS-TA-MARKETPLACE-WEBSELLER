package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;

@Data
public abstract class AbstractEntity {
    protected UseYn useYn;
    protected String createdId;
    protected String updatedId;
    protected String createdDate;
    protected String updatedDate;

    public enum UseYn {
        Y, N, All,
    };

}
