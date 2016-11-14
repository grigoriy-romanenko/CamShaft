package com.amcbridge.camshaft.service.profile.central;

import com.amcbridge.camshaft.model.FollowerType;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class CentralProfileCalculatorsContainer {

    private static final String NO_SUCH_CALCULATOR = "There are no calculator for specified follower type";

    private Map<FollowerType, CentralProfileCalculator> calculators = new HashMap<>();

    public CentralProfileCalculator getCalculator(FollowerType followerType) {
        CentralProfileCalculator calculator = calculators.get(followerType);
        if (calculator == null) {
            throw new NoSuchElementException(NO_SUCH_CALCULATOR);
        }
        return calculator;
    }

    public void addCalculator(FollowerType followerType, CentralProfileCalculator calculator) {
        calculators.put(followerType, calculator);
    }

}