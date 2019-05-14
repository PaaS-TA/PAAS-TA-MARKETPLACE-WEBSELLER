package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;

/**
 * Custom Code 모델
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-08
 */
@Data
public class CustomCode extends CommonEntity {

    private Long id;

    // ex) BC
    private String groupCode;

    // ex) 비즈니스 타입 코드
    private String groupCodeName;

    // ex) GOVERNMENT, COMPANY, PERSON, ETC
    private String codeUnit;

    // ex) 공공기관, 기업, 개인, 기타
    private String codeUnitName;
}
