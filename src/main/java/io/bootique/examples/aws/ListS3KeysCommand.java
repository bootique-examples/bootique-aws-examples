package io.bootique.examples.aws;

import io.bootique.aws2.s3.S3ClientFactory;
import jakarta.inject.Provider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;

public class ListS3KeysCommand extends CommandWithMetadata {

    // While S3ClientFactory is injectable directly, here we are using a Provider for lazy initialization, as Commands
    // are fully resolve when the help command is executed, and we need to postpone AWS stack initialization
    private Provider<S3ClientFactory> s3ClientFactory;

    public ListS3KeysCommand(Provider<S3ClientFactory> s3ClientFactory) {
        super(createMetadata());
        this.s3ClientFactory = s3ClientFactory;
    }

    private static CommandMetadata createMetadata() {
        return CommandMetadata
                .builder(ListS3KeysCommand.class)
                .description("Lists contents of a specified S3 bucket")
                .build();
    }

    @Override
    public CommandOutcome run(Cli cli) {

        String bucket = cli.optionString(App.BUCKET_OPTION);
        if (bucket == null) {
            return CommandOutcome.failed(-1, "No 'bucket' option is specified");
        }

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();

        ListObjectsV2Response response = s3ClientFactory.get().client().listObjectsV2(request);

        for (S3Object s3Object : response.contents()) {
            System.out.println(String.format(".. %s (size: %d)", s3Object.key(), s3Object.size()));
        }

        return CommandOutcome.succeeded();
    }
}
