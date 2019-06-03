package org.openpaas.paasta.marketplace.web.seller.category;

import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.web.seller.common.SellerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = SellerConstants.URI_WEB_CATEGORY_LIST)
    public CategoryList getCategoryList(){
    	log.info("category...");
        return categoryService.getCategoryListByDeleteYn();
    }

}
