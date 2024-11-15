package uk.tw.energy.service;

import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeeklyBillService {

    private final MeterReadingService meterReadingService;
    private final CostService costService;
    private final AccountService accountService;
    private final List<PricePlan> pricePlans;

    public WeeklyBillService(MeterReadingService meterReadingService, CostService costService, AccountService accountService, List<PricePlan> pricePlans) {
        this.meterReadingService = meterReadingService;
        this.costService = costService;
        this.accountService = accountService;
        this.pricePlans = pricePlans;
    }

    public BigDecimal calculateWeeklyBill(String meterId) {
        Optional<List<ElectricityReading>> optionalElectricityReadings =  meterReadingService.getReadings(meterId);
        List<ElectricityReading> electricityReadings = new ArrayList<>();
        if (optionalElectricityReadings.isPresent()){
            electricityReadings = optionalElectricityReadings.get();
        }else{
            //TODO: exception defined here
        }
        List<ElectricityReading> electricityReadingsPastWeek = electricityReadings.stream()
                .filter(e -> e.time().isAfter(Instant.now().minus(7, ChronoUnit.DAYS))).collect(Collectors.toList());
        String pricePlanId = accountService.getPricePlanIdForSmartMeterId(meterId);
        PricePlan pricePlan = pricePlans.stream().filter(e -> e.getPlanName().equals(pricePlanId)).findFirst().get();
        return costService.calculateCost(electricityReadingsPastWeek, pricePlan);
    }
}
