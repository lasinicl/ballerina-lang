/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.ballerinalang.plugins.idea.run.configuration.file;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import org.ballerinalang.plugins.idea.BallerinaConstants;
import org.ballerinalang.plugins.idea.BallerinaIcons;
import org.ballerinalang.plugins.idea.run.configuration.BallerinaConfigurationFactoryBase;
import org.jetbrains.annotations.NotNull;

public class BallerinaRunServiceFileConfigurationType extends ConfigurationTypeBase {

    public BallerinaRunServiceFileConfigurationType() {
        super("BallerinaRunFileConfiguration", "Ballerina File", "Ballerina File Run Configuration",
                BallerinaIcons.APPLICATION_RUN);

        addFactory(new BallerinaConfigurationFactoryBase(this) {

            @Override
            @NotNull
            public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                return new BallerinaRunFileConfiguration(project, BallerinaConstants.BALLERINA, getInstance());
            }
        });
    }

    @NotNull
    public static BallerinaRunServiceFileConfigurationType getInstance() {
        return Extensions.findExtension(CONFIGURATION_TYPE_EP, BallerinaRunServiceFileConfigurationType.class);
    }
}
