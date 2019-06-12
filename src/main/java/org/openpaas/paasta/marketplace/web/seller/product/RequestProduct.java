package org.openpaas.paasta.marketplace.web.seller.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RequestProduct {
	// 카테고리ID
    private Long categoryId;

    // 판매자ID
    private String sellerId;

    // 스크린샷 파일 목록
    private List<MultipartFile> screenshotFiles;

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

    // 아이콘 파일
    private MultipartFile iconFile;

    // 상품 파일
    private MultipartFile productFile;
    
    // 환경 파일
    private MultipartFile envFile;
    
    // 미터링 유형
    private String meteringType;
    
    // 미터링 금액
    private int unitPrice;

    // 전시여부
    private String displayYn;
}
