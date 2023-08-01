package shop.jitlee.parchment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    private final int CORE_POOL_SIZE = 1;
    private final int MAX_POOL_SIZE = 3;
    private final int QUEUE_CAPACITY = 100_000;

    @Bean(name = "pdfConvertExecutor")
    public Executor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize( CORE_POOL_SIZE );
        taskExecutor.setMaxPoolSize( MAX_POOL_SIZE );
        taskExecutor.setQueueCapacity( QUEUE_CAPACITY );
        taskExecutor.setThreadNamePrefix( "Executor-" );

        return taskExecutor;
    }
}
