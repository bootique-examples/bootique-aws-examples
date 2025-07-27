package io.bootique.examples.aws;

import io.bootique.aws2.s3.S3ClientFactory;
import jakarta.inject.Provider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.meta.application.OptionMetadata;

public class SendTextToS3Command extends CommandWithMetadata {

    private static final String TEXT_OPTION = "text";
    private static final String PATH_OPTION = "path";

    // While S3ClientFactory is injectable directly, here we are using a Provider for lazy initialization, as Commands
    // are fully resolve when the help command is executed, and we need to postpone AWS stack initialization
    private Provider<S3ClientFactory> s3ClientFactory;

    private static CommandMetadata createMetadata() {
        return CommandMetadata
                .builder(SendTextToS3Command.class)
                .description("Save a piece of text to Amazon S3")
                .addOption(OptionMetadata.builder(TEXT_OPTION).valueRequired("text_to_save").build())
                .addOption(OptionMetadata.builder(PATH_OPTION).valueRequired("path").description("S3 folder and file path").build())
                .build();
    }

    public SendTextToS3Command(Provider<S3ClientFactory> s3ClientFactory) {
        super(createMetadata());
        this.s3ClientFactory = s3ClientFactory;
    }

    @Override
    public CommandOutcome run(Cli cli) {

        String text = cli.optionString(TEXT_OPTION);
        if (text == null) {
            return CommandOutcome.failed(-1, "No 'text' option is specified");
        }

        String bucket = cli.optionString(App.BUCKET_OPTION);
        if (bucket == null) {
            return CommandOutcome.failed(-1, "No 'bucket' option is specified");
        }

        String path = cli.optionString(PATH_OPTION);
        if (path == null) {
            return CommandOutcome.failed(-1, "No 'path' option is specified");
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();

        s3ClientFactory.get().client().putObject(request, RequestBody.fromString(text));
        return CommandOutcome.succeeded();
    }
}
