/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.test.automation.framework.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class OrderedRunner extends BlockJUnit4ClassRunner {
    public OrderedRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected List computeTestMethods() {
        List list = super.computeTestMethods();
        List copy = new ArrayList(list);
        Collections.sort(copy, new Comparator() {
            public int compare(Object o1, Object o2) {
                FrameworkMethod f1 = (FrameworkMethod) o1;
                FrameworkMethod f2 = (FrameworkMethod) o2;
                Order order1 = f1.getMethod().getAnnotation(Order.class);
                Order order2 = f2.getMethod().getAnnotation(Order.class);
                if (order1.order() == order2.order()) {
                    return 0;
                } else if (order1.order() > order2.order()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return copy;
    }
}
