package com.cronutils.validator;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

/*
 * Copyright 2015 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class CronValidatorQuartzIntegrationTest {
    private CronValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new CronValidator(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
    }

    /**
     * Issue #27: month range string mapping is valid
     */
    @Test
    public void testMonthRangeMappingIsValid(){
        assertTrue(validator.isValid("0 0 0 * JUL-AUG * *"));
    }

    /**
     * Issue #27: single month string mapping is valid
     */
    @Test
    public void testSingleMonthMappingIsValid(){
        DateTime date = new DateTime(2015, 1, 1, 1, 1);
        for(int j=0;j<12;j++){
            String expression = String.format("0 0 0 * %s * *", date.plusMonths(j).toString("MMM", Locale.US).toUpperCase());
            assertTrue(String.format("We expect '%s' to be valid", expression), validator.isValid(expression));
        }
    }

    /**
     * Issue #27: day of week range string mapping is valid
     */
    @Test
    public void testDayOfWeekRangeMappingIsValid(){
        assertTrue(validator.isValid("0 0 0 * * MON-FRI *"));
    }

    /**
     * Issue #27: single day of week string mapping is valid
     */
    @Test
    public void testDayOfWeekMappingIsValid(){
        for(String dow : new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"}){
            String expression = String.format("0 0 0 * * %s *", dow);
            assertTrue(String.format("We expect '%s' to be valid", expression), validator.isValid(expression));
        }

    }

    /**
     * Issue #31: expressions
     * "0 10,44 14 ? 3 WED" and "0 0 12 ? * SAT-SUN"
     * considered invalid when replacing '?' for '*'
     * Fixed by adding support for question mark character.
     */
    @Test
    public void testQuestionMarkSupport(){
        assertTrue(validator.isValid("0 10,44 14 ? 3 WED"));
        assertTrue(validator.isValid("0 0 12 ? * SAT-SUN"));
    }
}
