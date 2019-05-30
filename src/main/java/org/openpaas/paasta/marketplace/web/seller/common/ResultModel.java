package org.openpaas.paasta.marketplace.web.seller.model;

import lombok.Data;

/**
 * Result 공통 모델
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-05-22
 */
@Data
public class ResultModel {
    private String resultCode;
    private String resultMessage;
}
