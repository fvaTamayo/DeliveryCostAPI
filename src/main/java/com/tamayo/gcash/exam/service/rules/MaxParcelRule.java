package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import com.tamayo.gcash.exam.service.rules.utils.ParcelType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:messages.properties")
@Component
@Order(1)
public class MaxParcelRule extends AbstractParcelRule {

    @Value("${exceeded.limit.message}")
    private String exceededLimitErrMsg;

    public MaxParcelRule(ParcelConfig parcelConfig) {
        super(parcelConfig);
        this.parcelType = ParcelType.max.name();
    }

    @Override
    public boolean passed(DeliveryCostRequestDTO requestDto) {
        Class c = requestDto.getClass();
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        Field field = fields.stream().filter(t -> exceededLimit(t, c, requestDto)).findAny().orElse(null);
        boolean exceeded = field != null;
        this.errorMessage = exceeded ? String.format("%s %s", field.getName(), exceededLimitErrMsg).toUpperCase() : null;
        return exceeded;
    }

    @Override
    public DeliveryCostResponseDTO getCost(DeliveryCostRequestDTO requestDto) {
        return DeliveryCostResponseDTO.builder()
                .cost(BigDecimal.ZERO)
                .message(errorMessage)
                .hasError(errorMessage != null)
                .build();
    }

    public boolean exceededLimit(Field fld, Class c, DeliveryCostRequestDTO requestDto){
        String fldName = fld.getName();
        Field fldToCheck;
        BigDecimal fldVal;
        Map<String,BigDecimal> maxMapConf = getMax();
        boolean result = false;

        try {
            BigDecimal max = maxMapConf.get(fldName);
            if (max != null) {
                fldToCheck = c.getDeclaredField(fldName);
                fldToCheck.setAccessible(true);
                fldVal = fldToCheck.get(requestDto) != null ? (BigDecimal) fldToCheck.get(requestDto) : BigDecimal.ZERO;
                if (fldVal.compareTo(max) == 1) {
                    return true;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return result;
    }
}
