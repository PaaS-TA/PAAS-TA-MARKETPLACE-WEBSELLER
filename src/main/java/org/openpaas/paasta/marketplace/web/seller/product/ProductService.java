package org.openpaas.paasta.marketplace.web.seller.product;

import java.util.ArrayList;
import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.RestTemplateService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.openpaas.paasta.marketplace.web.seller.util.DateUtils;
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

	/**
	 * 상품 상세
	 * @param id
	 * @return
	 */
	public Product getProduct(Long id) {
		Product product = marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PRODUCT + "/" + id, null, HttpMethod.GET, null, Product.class);
        String createdDate = DateUtils.getConvertDate(product.getCreateDate(), DateUtils.FORMAT_1);
        String updatedDate = DateUtils.getConvertDate(product.getCreateDate(), DateUtils.FORMAT_1);
		log.info("createdDate: " + createdDate);
        log.info("updatedDate: " + updatedDate);
        product.setStrCreateDate(createdDate);
        product.setStrUpdateDate(updatedDate);
        return product;
	}
	
	/**
	 * 상품 등록
	 * @param request
	 * @return
	 */
	public Product createProduct(RequestProduct request) {
		Product product = new Product();
		WebProduct webProduct = new WebProduct();
		BeanUtils.copyProperties(request, webProduct);
		log.info("product={}", webProduct);
		
		// 등록자ID, 수정자ID
		webProduct.setCreateId(request.getSellerId());
		webProduct.setUpdateId(request.getSellerId());

		// 파일 경로
    	String filePath = uploadPath + SEPARATOR + webProduct.getProductName() + SEPARATOR + webProduct.getVersionInfo() + SEPARATOR;
    	log.info("File Path: " + filePath);
    	webProduct.setFilePath(filePath);

    	try {
    		// 환경파일
    		MultipartFile envFile = request.getEnvFile();
    		if (envFile.isEmpty()) {
    			product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
    			product.setResultMessage("ENV FILE ERROR");
    			return product;
    		}
    		String envFileName = FileUtils.fileUpload(filePath, request.getEnvFile());
    		webProduct.setEnvFileName(envFileName);

    		// 상품파일
    		MultipartFile productFile = request.getProductFile();
    		if (productFile.isEmpty()) {
    			product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
    			product.setResultMessage("PRODUCT FILE ERROR");
    			return product;
    		}
    		String productFileName = FileUtils.fileUpload(filePath, request.getProductFile());
    		webProduct.setProductFileName(productFileName);

	    	// 아이콘파일
	    	MultipartFile iconFile = request.getIconFile();
	    	if (iconFile.isEmpty() || !iconFile.getContentType().contains("image")) {
	    		product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
	    		product.setResultMessage("ICON FILE ERROR");
	    		return product;
	    	}
	    	String iconFileName = FileUtils.fileUpload(filePath, request.getIconFile());
	    	webProduct.setIconFileName(iconFileName);

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
			webProduct.setScreenshotFileNames(screenshots);
    	} catch(Exception e) {
    		product.setResultCode(SellerConstants.RESULT_STATUS_FAIL);
    		product.setResultMessage(e.getMessage());
    		return product;
    	}

    	return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PRODUCT, null, HttpMethod.POST, webProduct, Product.class);
    }
	
	/**
	 * 상품 수정
	 * @param id
	 * @param product
	 * @return Product
	 */
	public Product updateProduct(Long id, Product updProduct) {
		Product product = marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_SELLER_PRODUCT + "/" + id, null, HttpMethod.PUT, updProduct, Product.class);
		String createdDate = DateUtils.getConvertDate(product.getCreateDate(), DateUtils.FORMAT_1);
		log.info("createdDate: " + createdDate);
        product.setStrCreateDate(createdDate);
        return product;
    }

}
