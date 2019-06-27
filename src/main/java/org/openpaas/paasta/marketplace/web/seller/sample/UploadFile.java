package org.openpaas.paasta.marketplace.web.seller.sample;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.BaseModel;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UploadFile extends BaseModel {

	private Long productId;
	private MultipartFile iconFile;
	private List<MultipartFile> screenshots;

}
