package io.siz.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    public static final String SPRING_PROFILE_FIXTURES = "fixtures";
    public static final String SPRING_PROFILE_MIGRATION = "migration";

    public static final String SYSTEM_ACCOUNT = "system";

    /**
     * profile indiquant que l'on veut se servir de sqs
     */
    public final static String SQS = "sqs";

    private Constants() {
    }
}
