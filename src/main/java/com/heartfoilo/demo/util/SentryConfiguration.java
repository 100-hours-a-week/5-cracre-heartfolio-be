package com.heartfoilo.demo.util;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://af89d01a95816f92b3e3bff777d78108@o4507841311735808.ingest.de.sentry.io/4507986129649744")
@Configuration
class SentryConfiguration {

}