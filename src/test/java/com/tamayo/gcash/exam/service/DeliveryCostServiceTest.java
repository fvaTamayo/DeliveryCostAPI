package com.tamayo.gcash.exam.service;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import com.tamayo.gcash.exam.service.rules.utils.ParcelType;
import net.bytebuddy.utility.nullability.AlwaysNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryCostServiceTest {

    @Autowired
    DeliveryCostService deliveryCostService;

    @AlwaysNull

    @Autowired
    private ParcelConfig parcelConfig;

    private final MathContext mc = new MathContext(3);

    private final Map<String, Double> voucherMap = Map.of("MYNT", 12.25, "GFI", 7.5, "skdlks", 0.0);

    @Test
    void calculate_max_weight() {
        BigDecimal weight = new BigDecimal(51); //Weight exceeds 50kg
        BigDecimal height = new BigDecimal(1);
        BigDecimal width = new BigDecimal(1);
        BigDecimal length = new BigDecimal(1);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);
        System.out.println(String.format("Has Error: %s | Message: %s", response.isHasError(), response.getMessage()));
        assertTrue(response.isHasError());
    }

    @Test
    void calculate_max_height() {
        BigDecimal weight = new BigDecimal(1);
        BigDecimal height = new BigDecimal(40); //Since the config for max height is not set, there should be no error
        BigDecimal width = new BigDecimal(1);
        BigDecimal length = new BigDecimal(1);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);
        System.out.println(String.format("Has Error: %s | Message: %s", response.isHasError(), response.getMessage()));
        assertTrue(!response.isHasError());
    }

    @Test
    void calculate_heavy_parcel() {
        String parcelType = ParcelType.heavy.name();
        BigDecimal weight = new BigDecimal(11); //Weight is more than 10kg
        BigDecimal height = new BigDecimal(1);
        BigDecimal width = new BigDecimal(1);
        BigDecimal length = new BigDecimal(1);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);
        assert parcelConfig != null;

        BigDecimal cost = response.getCost();
        BigDecimal testCost = weight.multiply(parcelConfig.getMultiplier().get(ParcelType.heavy.name()));
        System.out.println("Cost is " + cost);
        System.out.println("Test Calculation is  " + testCost);
        assertTrue(testCost.compareTo(cost) == 0);


        String message = response.getMessage();
        System.out.println(message);
        assertTrue(message.contains(parcelType));
    }

    @Test
    void calculate_small_parcel() {
        BigDecimal weight = new BigDecimal(5);
        BigDecimal height = new BigDecimal(10);
        BigDecimal width = new BigDecimal(2);
        BigDecimal length = new BigDecimal(50);

        //Volume is less than 1500
        BigDecimal volume = height.multiply(width).multiply(length).setScale(3);
        System.out.println("Volume is : " + volume);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);
        assert parcelConfig != null;

        BigDecimal cost = response.getCost();
        BigDecimal testCost = volume.multiply(parcelConfig.getMultiplier().get(ParcelType.small.name()));
        System.out.println("Cost is " + cost);
        System.out.println("Test Calculation is  " + testCost);
        assertTrue(testCost.compareTo(cost) == 0);
    }

    @Test
    void calculate_medium_parcel() {
        BigDecimal weight = new BigDecimal(5);
        BigDecimal height = new BigDecimal(15);
        BigDecimal width = new BigDecimal(2);
        BigDecimal length = new BigDecimal(55);

        //Volume is less than 2500
        BigDecimal volume = height.multiply(width).multiply(length).setScale(3);
        System.out.println("Volume is : " + volume);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);

        assert parcelConfig != null;

        BigDecimal cost = response.getCost();
        BigDecimal testCost = volume.multiply(parcelConfig.getMultiplier().get(ParcelType.medium.name()));
        System.out.println("Cost is " + cost);
        System.out.println("Test Calculation is  " + testCost);
        assertTrue(testCost.compareTo(cost) == 0);
    }

    @Test
    void calculate_large_parcel() {
        BigDecimal weight = new BigDecimal(5);
        BigDecimal height = new BigDecimal(15);
        BigDecimal width = new BigDecimal(2);
        BigDecimal length = new BigDecimal(100);

        //Volume is more than or equal 2500
        BigDecimal volume = height.multiply(width).multiply(length).setScale(3);
        System.out.println("Volume is : " + volume);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);

        assert parcelConfig != null;

        BigDecimal cost = response.getCost();
        BigDecimal testCost = volume.multiply(parcelConfig.getMultiplier().get(ParcelType.large.name()));
        System.out.println("Cost is " + cost);
        System.out.println("Test Calculation is  " + testCost);
        assertTrue(testCost.compareTo(cost) == 0);
    }

    @Test
    void calculate_small_parcel_with_valid_voucher() {
        BigDecimal weight = new BigDecimal(5);
        BigDecimal height = new BigDecimal(2);
        BigDecimal width = new BigDecimal(2);
        BigDecimal length = new BigDecimal(1);

        String voucher = "MYNT";
        BigDecimal discount = BigDecimal.valueOf(voucherMap.get(voucher));
        BigDecimal volume = height.multiply(width).multiply(length).setScale(3);

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length)
                .voucher(voucher).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);

        assert parcelConfig != null;

        BigDecimal discountedCost = response.getCost();

        BigDecimal originalCost = volume.multiply(parcelConfig.getMultiplier().get(ParcelType.large.name()));
        System.out.println("Original cost is " + originalCost);
        System.out.println("Discount is : " + discount);
        System.out.println("Discounted cost is : " + discountedCost);
        assertTrue(BigDecimal.ZERO.compareTo(discountedCost) == 0);
    }

    @Test
    void calculate_large_parcel_with_valid_voucher() {
        BigDecimal weight = new BigDecimal(5);
        BigDecimal height = new BigDecimal(15);
        BigDecimal width = new BigDecimal(2);
        BigDecimal length = new BigDecimal(100);
        BigDecimal volume = height.multiply(width).multiply(length).setScale(3);
        System.out.println("Volume is : " + volume);

        String voucher = "MYNT";
        BigDecimal discount = BigDecimal.valueOf(voucherMap.get(voucher));

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length)
                .voucher(voucher).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);

        assert parcelConfig != null;

        BigDecimal discountedCost = response.getCost();
        BigDecimal originalCost = volume.multiply(parcelConfig.getMultiplier().get(ParcelType.large.name()));
        BigDecimal testCost = originalCost.subtract(discount);
        System.out.println("Original cost is " + originalCost);
        System.out.println("Discount is : " + discount);
        System.out.println("Discounted cost is : " + discountedCost);
        System.out.println("Test Calculation is  " + testCost);

        assertTrue((testCost).compareTo(discountedCost) == 0);
    }

    @Test
    void calculate_with_invalid_voucher() {
        BigDecimal weight = new BigDecimal(5);
        BigDecimal height = new BigDecimal(15);
        BigDecimal width = new BigDecimal(2);
        BigDecimal length = new BigDecimal(100);

        BigDecimal volume = weight.multiply(height).multiply(width).multiply(length);

        String voucher = "skdlks";
        BigDecimal discount = BigDecimal.valueOf(voucherMap.get(voucher));

        DeliveryCostRequestDTO request = DeliveryCostRequestDTO.builder()
                .weight(weight).height(height).width(width).length(length)
                .voucher(voucher).build();

        DeliveryCostResponseDTO response = deliveryCostService.calculateCost(request);

        assert parcelConfig != null;

        BigDecimal cost = response.getCost();
        BigDecimal originalCost = volume.multiply(parcelConfig.getMultiplier().get(ParcelType.large.name()));
        System.out.println("Original cost is " + originalCost);
        System.out.println("Discount : " + response.getDiscount());
        System.out.println("Discounted cost is : " + cost);
        assertTrue(response.getDiscount().contains("Invalid code"));
    }
}