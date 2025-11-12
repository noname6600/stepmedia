package com.thanhvh.shopmanagement.modules.statistic.controller;

import com.thanhvh.rest.model.response.BaseResponse;
import com.thanhvh.shopmanagement.modules.statistic.model.CountInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/statistics")
@Tag(name = "Statistics", description = "Statistics Controller")
public interface IStatisticController {
    @Transactional(readOnly = true)
    @GetMapping("/counts")
    ResponseEntity<BaseResponse<CountInfo>> counts();

}
