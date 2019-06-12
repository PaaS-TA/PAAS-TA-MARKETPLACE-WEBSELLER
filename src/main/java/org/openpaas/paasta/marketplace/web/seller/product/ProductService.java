package org.openpaas.paasta.marketplace.web.seller.product;

import java.util.ArrayList;
import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.RestTemplateService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.openpaas.paasta.marketplace.web.seller.util.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	public final String SEPARATOR = "/";

	@Value("${local.uploadPath}")
    private String uploadPath;

	@Autowired
    private RestTemplateService marketApiRest;
	
	public Product createProduct(RequestProduct request) {
		Product product = new Product();
		BeanUtils.copyProperties(request, product);
		log.info("product={}", product);
		
		// 등록자ID, 수정자ID
		product.setCreateId(request.getSellerId());
		product.setUpdateId(request.getSellerId());

		// 파일 경로
    	String filePath = uploadPath + SEPARATOR + product.getProductName() + SEPARATOR + product.getVersionInfo() + SEPARATOR;
    	log.info("File Path: " + filePath);
    	product.setFilePath(filePath);

    	try {
    		// 환경파일
    		MultipartFile envFile = request.getEnvFile();
    		if (envFile.isEmpty()) {
    			product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
    			product.setResultMessage("ENV FILE ERROR");
    			return product;
    		}
    		String envFileName = FileUtils.fileUpload(filePath, request.getEnvFile());
    		product.setEnvFileName(envFileName);

    		// 상품파일
    		MultipartFile productFile = request.getProductFile();
    		if (productFile.isEmpty()) {
    			product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
    			product.setResultMessage("PRODUCT FILE ERROR");
    			return product;
    		}
    		String productFileName = FileUtils.fileUpload(filePath, request.getProductFile());
	    	product.setProductFileName(productFileName);

	    	// 아이콘파일
	    	MultipartFile iconFile = request.getIconFile();
	    	if (iconFile.isEmpty() || !iconFile.getContentType().contains("image")) {
	    		product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
	    		product.setResultMessage("ICON FILE ERROR");
	    		return product;
	    	}
	    	String iconFileName = FileUtils.fileUpload(filePath, request.getIconFile());
	    	product.setIconFileName(iconFileName);

	    	// 스크린샷파일
			List<MultipartFile> screenshotFiles = request.getScreenshotFiles();
			if (screenshotFiles.isEmpty()) {
				product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
				product.setResultMessage("SCREENSHOT FILE ERROR");
				return product;
			}
			List<String> screenshots = new ArrayList<String>();
			for (MultipartFile screenshotFile : screenshotFiles) {
				if (!screenshotFile.getContentType().contains("image")) {
					product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
					product.setResultMessage("SCREENSHOT FILE ERROR: Not a Image");
					return product;
				} else {
					String screenshotFileName = FileUtils.fileUpload(filePath, screenshotFile);
					screenshots.add(screenshotFileName);
				}
			}
			product.setScreenshotFileNames(screenshots);
    	} catch(Exception e) {
    		product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
    		product.setResultMessage(e.getMessage());
    		return product;
    	}

    	return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PRODUCT, null, HttpMethod.POST, product, Product.class);
    }

}
