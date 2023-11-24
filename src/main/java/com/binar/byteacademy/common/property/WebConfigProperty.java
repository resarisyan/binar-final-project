package com.binar.byteacademy.common.property;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
@RequiredArgsConstructor
public class WebConfigProperty {
    private final Cors cors;

    public Cors getCors() {
        return cors;
    }

    public static class Cors {
        private final String[] allowedOrigins;

        private final String[] allowedMethods;

        private final String[] allowedHeaders;

        private final String[] exposedHeaders;

        private final long maxAge;

        public Cors(String[] allowedOrigins, String[] allowedMethods, long maxAge,
                    String[] allowedHeaders, String[] exposedHeaders) {
            this.allowedOrigins = allowedOrigins;
            this.allowedMethods = allowedMethods;
            this.maxAge = maxAge;
            this.allowedHeaders = allowedHeaders;
            this.exposedHeaders = exposedHeaders;
        }

        public String[] getAllowedOrigins() {
            return allowedOrigins;
        }

        public String[] getAllowedMethods() {
            return allowedMethods;
        }

        public long getMaxAge() {
            return maxAge;
        }

        public String[] getAllowedHeaders() {
            return allowedHeaders;
        }

        public String[] getExposedHeaders() {
            return exposedHeaders;
        }
    }
}
