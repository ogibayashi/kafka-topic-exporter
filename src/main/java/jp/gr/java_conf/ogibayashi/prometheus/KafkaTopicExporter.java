package jp.gr.java_conf.ogibayashi.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.StandardExports;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.IOException;

public class KafkaTopicExporter {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaTopicExporter.class);

    public static void main( String[] args ) throws IOException
    {
        final PropertyConfig pc = new PropertyConfig(args[0]);

        int serverPort = pc.getInt(PropertyConfig.Constants.EXPORTER_PORT.key);
        Server server = new Server(serverPort);
        LOG.info("starting server on port {}", serverPort);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
        
       
        ExecutorService executor = Executors.newSingleThreadExecutor();
        KafkaCollector kc = new KafkaCollector().register();
        
        final Future consumer = executor.submit(new Thread(new ConsumerThread(kc,pc)));

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    LOG.info("Shutting down");
                    consumer.cancel(true);
                    executor.shutdown();
                    try {
                        if(executor.awaitTermination(3000L, TimeUnit.MILLISECONDS)) {
                        } else {
                            LOG.info("Shutdown timed out");
                        }
                    }catch (InterruptedException e) {
                        LOG.info("Shut down interrupted");
                    }
                }
            }));

        
        try {
            server.start();
            server.join();
        }
        catch (Exception e) {
            LOG.error("Error happend in server", e);
        }
        finally{
            consumer.cancel(true);
            executor.shutdown();
        }
                
    }        
    
}
