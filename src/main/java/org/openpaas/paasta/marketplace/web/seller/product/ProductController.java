//package org.openpaas.paasta.marketplace.web.seller.product;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
//import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
//import org.openpaas.paasta.marketplace.web.seller.util.SecurityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@Slf4j
//public class ProductController {
//
//	@Autowired
//    private CommonService commonService;
//
//	@Autowired
//	private ProductService productService;
//
//	/**
//     * 프로필 상세 조회 화면
//     *
//     * @param httpServletRequest the httpServletRequest
//     * @return ModelAndView
//     */
//    @GetMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_DETAIL)
//    public ModelAndView getProductPage(HttpServletRequest httpServletRequest, @PathVariable(value = "id") Long id) {
//    	// 화면 변수 처리
//    	return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PRODUCT + "/getProduct", new ModelAndView());
//    }
//
//    /**
//     * 프로필 상세 조회 DB
//     *
//     * @param id the id
//     * @return SellerProfile
//     */
//    @GetMapping(value = SellerConstants.URI_DB_SELLER_PRODUCT_DETAIL)
//    private Product getProduct(@PathVariable Long id) {
//    	return productService.getProduct(id);
//    }
//
//    /**
//     * 상품 등록 페이지 이동
//     * @param httpServletRequest
//     * @return
//     */
//	@GetMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_CREATE)
//    public ModelAndView createProductPage(HttpServletRequest httpServletRequest){
//    	log.info("create view");
//    	return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PRODUCT + "/createProduct", new ModelAndView());
//    }
//
//	/**
//	 * 상품 등록
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_CREATE)
//    private Product createProduct(@ModelAttribute RequestProduct request) {
//    	String userId = SecurityUtils.getUserId();
//    	request.setSellerId(userId);
//    	request.setCreateId(userId);
//    	request.setUpdateId(userId);
//
//    	return productService.createProduct(request);
//    }
//
//	/**
//	 * 상품 수정 페이지 이동
//	 * @param httpServletRequest
//	 * @param id
//	 * @return
//	 */
//	@GetMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_UPDATE)
//    public ModelAndView getProductUpdatePage(HttpServletRequest httpServletRequest, @PathVariable Long id) {
//        return commonService.setPathVariables(httpServletRequest, SellerConstants.URI_VIEW_PRODUCT + "/updateProduct", new ModelAndView());
//    }
//
//	/**
//	 * 상품 수정
//	 * @param id
//	 * @param product
//	 * @return Product
//	 */
//	@PutMapping(value = SellerConstants.URI_WEB_SELLER_PRODUCT_UPDATE)
//    private Product updateProduct(@PathVariable Long id, @ModelAttribute RequestProduct request) {
//        String userId = SecurityUtils.getUserId();
//        request.setUpdateId(userId);
//
//        return productService.updateProduct(id, request);
//    }
//
//}
