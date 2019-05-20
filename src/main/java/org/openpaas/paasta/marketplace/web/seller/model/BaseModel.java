package org.openpaas.paasta.marketplace.web.seller.model;

import java.util.Date;

import lombok.Data;

@Data
public class BaseModel {

	protected String createId;
    protected String updateId;
    protected Date createDate;
    protected Date updateDate;

}
