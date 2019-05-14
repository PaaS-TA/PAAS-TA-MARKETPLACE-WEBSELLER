package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;

@Data
public abstract class CommonEntity {
    protected String createdId;
    protected String updatedId;
    protected String createdDate;
    protected String updatedDate;

}
