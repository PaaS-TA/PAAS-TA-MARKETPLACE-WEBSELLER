package org.openpaas.paasta.marketplace.web.seller.product;

import javax.servlet.http.HttpServletRequest;

import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.openpaas.paasta.marketplace.web.seller.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ProductController {

	@Autowired
    private CommonService commonService;

	@Autowired
	private ProductService productService;
	
	@GetMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_CREATE)
    public ModelAndView createProductPage(HttpServletRequest httpServletRequest){
    	log.info("create view");
    	return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PRODUCT + "/createProduct", new ModelAndView());
    }
	
	@PostMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_CREATE)
    private Product createProduct(@ModelAttribute RequestProduct request) throws Exception {
    	String userId = SecurityUtils.getUserId();
    	request.setSellerId(userId);
    	
    	return productService.createProduct(request);
    }

}
