package com.amcbridge.camshaft.service.profile.central;

import com.amcbridge.camshaft.model.FollowerType;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CentralProfileCalculatorsContainerTest {

    private CentralProfileCalculatorsContainer calculatorsContainer;

    @Before
    public void before() {
        calculatorsContainer = new CentralProfileCalculatorsContainer();
    }

    @Test
    public void testGetCalculator() {
        CentralProfileCalculator calculator = mock(CentralProfileCalculator.class);
        calculatorsContainer.addCalculator(FollowerType.TRANSLATING, calculator);
        calculatorsContainer.addCalculator(FollowerType.ROTATING, null);
        assertSame(calculator, calculatorsContainer.getCalculator(FollowerType.TRANSLATING));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetCalculatorIfThereIsNoCalculatorForSpecifiedFollowerType() {
        CentralProfileCalculator calculator = mock(CentralProfileCalculator.class);
        calculatorsContainer.addCalculator(FollowerType.TRANSLATING, calculator);
        calculatorsContainer.getCalculator(FollowerType.ROTATING);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetCalculatorWithNullFollowerType() {
        CentralProfileCalculator calculator = mock(CentralProfileCalculator.class);
        calculatorsContainer.addCalculator(FollowerType.TRANSLATING, calculator);
        calculatorsContainer.getCalculator(null);
    }

}