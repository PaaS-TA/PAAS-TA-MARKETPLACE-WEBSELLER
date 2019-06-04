package org.openpaas.paasta.marketplace.web.seller.category;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Category Controller
 *
 * @author peter
 * @version 1.0
 * @since 2019-05-29
 */
@RestController
@Slf4j
public class CategoryController {

	@Autowired
    private CategoryService categoryService;

    /**
     * 카테고리 목록 조회
     *
     * @return CategoryList
     */
    @GetMapping(value = SellerConstants.URI_DB_CATEGORY_LIST)
    public List<Category> getCategoryList(){
    	log.info("category...");
        return categoryService.getCategoryListByDeleteYn();
    }
    
    /**
     * 카테고리 상세 조회
     * 
     * @param id
     * @return
     */
    @GetMapping(value = SellerConstants.URI_DB_CATEGORY_DETAIL)
    private Category getCategory(@PathVariable Long id) {
    	return categoryService.getCategory(id);
    }

}
