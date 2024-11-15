package uk.tw.energy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.service.WeeklyBillService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private WeeklyBillService weeklyBillService;

    @GetMapping("weekly/{meterId}")
    public ResponseEntity<BigDecimal> weeklyBill(@PathVariable String meterId){
        return new ResponseEntity<>(weeklyBillService.calculateWeeklyBill(meterId), HttpStatus.OK);
    }
}
