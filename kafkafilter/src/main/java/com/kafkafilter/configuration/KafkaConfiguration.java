/*-
 * #%L
 * Elastic APM Java agent
 * %%
 * Copyright (C) 2018 the original author or authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.kafkafilter.configuration;

import org.stagemonitor.configuration.ConfigurationOption;
import org.stagemonitor.configuration.ConfigurationOptionProvider;
import org.stagemonitor.configuration.converter.ListValueConverter;

import java.util.Arrays;
import java.util.List;
/**
 * @author Zongyang Li
 */
public class KafkaConfiguration extends ConfigurationOptionProvider {


    public static final String KAFKA_CATEGORY = "kafka";

    public static final String SOURCE_TOPIC = "source_topic";
    public static final String DEST_TOPIC = "dest_topic";
    public static final String SERVER = "server";

    private final ConfigurationOption<String> server = ConfigurationOption.stringOption()
            .key(SERVER)
            .configurationCategory(KAFKA_CATEGORY)
            .description("")
            .dynamic(true)
            .buildWithDefault("192.168.0.1:9200");

    private final ConfigurationOption<String> sourceTopic = ConfigurationOption.stringOption()
        .key(SOURCE_TOPIC)
        .configurationCategory(KAFKA_CATEGORY)
        .description("")
        .dynamic(true)
        .buildWithDefault("source");

    private final ConfigurationOption<String> destTopic = ConfigurationOption.stringOption()
            .key(DEST_TOPIC)
            .configurationCategory(KAFKA_CATEGORY)
            .description("")
            .dynamic(true)
            .buildWithDefault("dest");


    public String getServer() {
        return server.get();
    }

    public String getSourceTopic() {
        return sourceTopic.get();
    }

    public String getDestTopic() {
        return destTopic.get();
    }


}
