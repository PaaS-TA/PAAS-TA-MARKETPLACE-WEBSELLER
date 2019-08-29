package org.openpaas.paasta.marketplace.web.seller.controller;

import com.sun.org.glassfish.external.statistics.Stats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.marketplace.api.domain.*;
import org.openpaas.paasta.marketplace.web.seller.common.CommonService;
import org.openpaas.paasta.marketplace.web.seller.service.SoftwareService;
import org.openpaas.paasta.marketplace.web.seller.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping(value = "/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final SoftwareService softwareService;
    private final CommonService commonService;

    /**
     * 요금계산 메인페이지
     *
     * @return
     */
    @GetMapping(value = "/list")
    public String getStats() {
        return "contents/priceCalculation";
    }

    /**
     * 요금 목록 조회
     *
     * @param httpServletRequest the httpServletRequest
     * @return CustomPage<Software>
     */
    @GetMapping
    @ResponseBody
    public CustomPage<Stats> getStatsList(HttpServletRequest httpServletRequest){
        return statsService.getStatsList(commonService.setParameters(httpServletRequest));
    }



}
