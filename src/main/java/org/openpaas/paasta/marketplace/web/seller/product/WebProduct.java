package org.openpaas.paasta.marketplace.web.seller.product;

import java.util.List;

import org.openpaas.paasta.marketplace.web.seller.common.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WebProduct extends BaseModel {

	// 카테고리ID
    private Long categoryId;

    // 판매자ID
    private String sellerId;

    // 파일경로
    private String filePath;

    // 스크린샷 파일명 목록
    private List<String> screenshotFileNames;

    // 상품명
    private String productName;
    
    // 버전정보
    private String versionInfo;

    // 상품개요
    private String simpleDescription;

    // 상품상세
    private String detailDescription;

    // 상품유형
    private String productType;

    // 아이콘 파일명
    private String iconFileName;

    // 상품 파일명
    private String productFileName;
    
    // 환경 파일명
    private String envFileName;

    // 미터링 유형
    private String meteringType;
    
    // 미터링 금액
    private int unitPrice;

    // 전시여부
    private String displayYn;

}
