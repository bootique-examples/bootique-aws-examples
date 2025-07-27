package io.bootique.examples.aws;

import io.bootique.BQCoreModule;
import io.bootique.BQModule;
import io.bootique.Bootique;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.meta.application.OptionMetadata;
import io.bootique.aws2.s3.S3ClientFactory;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

public class App implements BQModule {

    static final String BUCKET_OPTION = "bucket";

    public static void main(String[] args) {
        Bootique.app(args).autoLoadModules().exec().exit();
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder)
                .addCommand(ListS3KeysCommand.class)
                .addCommand(SendTextToS3Command.class)
                .addOption(OptionMetadata.builder(BUCKET_OPTION)
                        .description("S3 bucket name with an optional folder component. E.g., 'mybucket' or 'mybucket/myfolder'.")
                        .valueRequired("s3bucket").build())
                .setApplicationDescription("A simple cli S3 client.");
    }

    @Provides
    @Singleton
    SendTextToS3Command provideUploadCommand(Provider<S3ClientFactory> s3ClientFactory) {
        return new SendTextToS3Command(s3ClientFactory);
    }

    @Provides
    @Singleton
    ListS3KeysCommand provideListCommand(Provider<S3ClientFactory> s3ClientFactory) {
        return new ListS3KeysCommand(s3ClientFactory);
    }
}
