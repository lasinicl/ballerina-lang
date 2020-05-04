/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.test.query;

import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.test.util.BCompileUtil;
import org.ballerinalang.test.util.BRunUtil;
import org.ballerinalang.test.util.CompileResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This contains methods to test on clause in query expression.
 *
 * @since 1.3.0
 */
public class OnClauseTest {
    private CompileResult result;

    @BeforeClass
    public void setup() {
        result = BCompileUtil.compile("test-src/query/on-clause.bal");
    }

    @Test(description = "Test on clause")
    public void testSimpleOnClause() {
        BValue[] returnValues = BRunUtil.invoke(result, "testSimpleOnClause");
        Assert.assertNotNull(returnValues);

        Assert.assertEquals(returnValues.length, 6, "Expected events are not received");

        BMap person1 = (BMap) returnValues[0];
        BMap person2 = (BMap) returnValues[1];
        BMap person3 = (BMap) returnValues[2];
        BMap person4 = (BMap) returnValues[3];
        BMap person5 = (BMap) returnValues[4];
        BMap person6 = (BMap) returnValues[5];

        Assert.assertEquals(person1.stringValue(), "{firstName:\"Alex\", lastName:\"George\", age:23, deptId:1}");
        Assert.assertEquals(person2.stringValue(), "{firstName:\"Alex\", lastName:\"George\", age:23, deptId:1}");
        Assert.assertEquals(person3.stringValue(), "{firstName:\"Ranjan\", lastName:\"Fonseka\", age:30, deptId:2}");
        Assert.assertEquals(person4.stringValue(), "{firstName:\"Ranjan\", lastName:\"Fonseka\", age:30, deptId:2}");
        Assert.assertEquals(person5.stringValue(), "{firstName:\"John\", lastName:\"David\", age:33, deptId:1}");
        Assert.assertEquals(person6.stringValue(), "{firstName:\"John\", lastName:\"David\", age:33, deptId:1}");
    }
}
