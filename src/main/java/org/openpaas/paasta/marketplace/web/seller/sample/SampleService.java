package org.openpaas.paasta.marketplace.web.seller.sample;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.RestTemplateService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.openpaas.paasta.marketplace.web.seller.profile.SellerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SampleService {

	@Autowired
    private RestTemplateService marketApiRest;

//	public Product fileUpload(UploadFile upload) {
//		MultipartFile iconFile = upload.getIconFile();
//		if (!iconFile.getContentType().contains("image")) {
//			upload.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
//			upload.setResultMessage("ICON FILE ERROR: Not a Image");
//			return upload;
//		}
//
//		List<MultipartFile> screenshots = upload.getScreenshots();
//		for (MultipartFile screenshot : screenshots) {
//			if (!screenshot.getContentType().contains("image")) {
//				upload.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
//				upload.setResultMessage("SCREENSHOT FILE ERROR: Not a Image");
//				return upload;
//			}
//		}
//		
//		return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PRODUCT, null, HttpMethod.POST, UploadFile, Product.class);
//	}

}
