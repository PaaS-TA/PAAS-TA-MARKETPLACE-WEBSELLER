package org.openpaas.paasta.marketplace.web.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 요금 계산 Controller
 *
 * @author hrjin
 * @version 1.0
 * @since 2019-08-02
 */
@Slf4j
@Controller
@RequestMapping(value = "/priceCalculation")
@RequiredArgsConstructor
public class PriceCalculationController {

    /**
     * 요금 계산 페이지 이동
     *
     * @return String
     */
    @GetMapping
    public String getPriceCalculation() {
        return "contents/priceCalculation";
    }
}
