package com.bank.account.infrastructure;

import com.bank.account.domain.IbanGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IbanGeneratorImpl implements IbanGenerator {

    @Override
    public String generate() {
        return "SE" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
}
