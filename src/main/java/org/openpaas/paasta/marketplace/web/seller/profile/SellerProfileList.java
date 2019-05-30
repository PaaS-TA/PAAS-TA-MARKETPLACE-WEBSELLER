package org.openpaas.paasta.marketplace.web.seller.profile;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SellerProfileList extends BaseModel {

	private List<SellerProfile> items;
}
