package org.openpaas.paasta.marketplace.web.seller.category;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.RestTemplateService;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
    private RestTemplateService marketApiRest;

    /**
     * 카테고리 목록 조회
     *
     * @return CategoryList
     */
    public List<Category> getCategoryListByDeleteYn() {
    	CategoryList categoryList = marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_CATEGORY, HttpMethod.GET, null, CategoryList.class);
    	return categoryList.getItems();
    }
    
    /**
     * 카테고리 상세 조회
     * 
     * @param id
     * @return
     */
    public Category getCategory(Long id) {
        return marketApiRest.send(SellerConstants.TARGET_API_MARKET, SellerConstants.URI_API_CATEGORY + "/" + id, HttpMethod.GET, null, Category.class);
    }

}
