package org.openpaas.paasta.marketplace.web.seller.category;

import org.openpaas.paasta.marketplace.web.seller.common.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseModel {

	private Long id;

	// 카테고리명
    private String categoryName;

    private String deleteYn;

}
