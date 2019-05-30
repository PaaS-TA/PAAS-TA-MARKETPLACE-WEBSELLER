package org.openpaas.paasta.marketplace.web.seller.category;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryList extends BaseModel {

	private List<Category> items;

}
