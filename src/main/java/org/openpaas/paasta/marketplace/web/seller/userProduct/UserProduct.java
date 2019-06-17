package org.openpaas.paasta.marketplace.web.seller.userProduct;

import lombok.Data;
import org.openpaas.paasta.marketplace.web.seller.product.Product;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UserProduct {

    private Long id;

	// 사용자ID
	@NotNull
	private String userId;


    private Long productId;

    private Product product;

    // 사용자명
    @NotNull
    private String userName;
    
    // 상품명
    @NotNull
    private String productName;
    
    // 미터링 유형
    @NotNull
    private String meteringType;

    // 요금
    @NotNull
    private int unitPrice;

    // 구매상태
    @NotNull
    private String provisionStatus;
    
    // 사용시작일자
    private LocalDateTime useStartdate;
    
    // 사용종료일자
    private LocalDateTime useEnddate;
    
    // 접속URL
    private String accessUrl;

    private Integer provisionTryCount = 0;

    private Integer deprovisionTryCount = 0;

}
