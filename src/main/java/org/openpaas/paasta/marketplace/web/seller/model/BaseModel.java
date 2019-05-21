package org.openpaas.paasta.marketplace.web.seller.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseModel {

	protected String createId;
    protected String updateId;
    protected LocalDateTime createDate;
    protected LocalDateTime updateDate;

}
