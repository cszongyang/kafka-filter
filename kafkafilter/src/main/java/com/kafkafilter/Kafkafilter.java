package com.kafkafilter;

import com.kafkafilter.configuration.KafkaConfiguration;
import com.kafkafilter.configuration.ReporterConfiguration;
import com.kafkafilter.filter.Filter;
import com.kafkafilter.filter.FilterFileManager;
import com.kafkafilter.filter.FilterLoader;
import com.kafkafilter.report.Incident;
import com.kafkafilter.report.ReportSender;
import com.kafkafilter.resources.PageView;
import com.kafkafilter.resources.Search;
import com.kafkafilter.resources.UserActivity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.SimpleSource;
import sun.plugin.util.UserProfile;

import java.util.Properties;

/**
 * @author Zongyang Li
 */
public class KafkaFilter {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaFilter.class);

    private ConfigurationRegistry configurationRegistry;

    FilterLoader fl;
    ReportSender sender;

    public void init() {
        configurationRegistry = ConfigurationRegistry.builder()
            .addOptionProvider(new KafkaConfiguration())
            .addOptionProvider(new ReporterConfiguration())
            .addConfigSource(new SimpleSource()
                .add(KafkaConfiguration.SERVER, "localhost:9092")
                .add(KafkaConfiguration.SOURCE_TOPIC, "clicks"))
                .build();

        sender = new ReportSender(configurationRegistry);
//        Incident incident = new Incident("liziongyang");
//        sender.send(incident);

//        String[] directories, String[] classNames, int pollingIntervalSeconds
        String[] directories = new String[]{"/Users/zongyangli/Workspace/javaproject/kafka-filter/kafkafilter/src/main/java/com/kafkafilter/filter/"};

        String[] classNames = new String[]{};

        FilterFileManager.FilterFileManagerConfig config = new FilterFileManager.FilterFileManagerConfig(directories, classNames, 300);
        fl = new FilterLoader();
        try {
            FilterFileManager fm = new FilterFileManager(config, fl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        runStream();
    }



    public void runStream() {

        final Serializer<PageView> pageSerializer = new org.springframework.kafka.support.serializer.JsonSerializer<PageView>();
        final Deserializer<PageView> pageDeserializer = new org.springframework.kafka.support.serializer.JsonDeserializer<>(PageView.class);
        final Serde<PageView> pageMessageSerde = Serdes.serdeFrom(pageSerializer, pageDeserializer);

        final Serializer<Search> searchSerializer = new org.springframework.kafka.support.serializer.JsonSerializer<>();
        final Deserializer<Search> searchDeserializer = new org.springframework.kafka.support.serializer.JsonDeserializer(Search.class);
        final Serde<Search> searchMessageSerde = Serdes.serdeFrom(searchSerializer, searchDeserializer);

        final Serializer<UserProfile> profileSerializer = new org.springframework.kafka.support.serializer.JsonSerializer<>();
        final Deserializer<UserProfile> profileDeserializer = new org.springframework.kafka.support.serializer.JsonDeserializer(UserProfile.class);
        final Serde<UserProfile> profileMessageSerde = Serdes.serdeFrom(profileSerializer, profileDeserializer);

        final StringSerializer stringSerializer = new StringSerializer();
        final StringDeserializer stringDeserializer = new StringDeserializer();
        final Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer,stringDeserializer);


        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "clicks");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, PageView> stream = builder.stream(stringSerde, pageMessageSerde, "pageviews");

        KStream<String, PageView> dest_stream = stream.filterNot((key, page) -> {
            boolean flag = false;
            for (Filter filter : fl.getAllFilters()) {
                if (filter.shouldFilter(page)) {
                    Incident incident = new Incident(filter.filterName());
                    sender.send(incident);
                    flag = true;
                }
            }
            return flag;
        }).through(stringSerde, pageMessageSerde, "output");

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.cleanUp();
        streams.start();
    }
}


