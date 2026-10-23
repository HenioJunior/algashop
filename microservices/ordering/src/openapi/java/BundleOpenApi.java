import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

import java.nio.file.Files;
import java.nio.file.Path;

public class BundleOpenApi {

    public static void main(String[] args) throws Exception {

        Path input = Path.of(args[0]);
        Path output = Path.of(args[1]);

        ParseOptions options = new ParseOptions();
        options.setResolve(true);
        options.setResolveFully(true);

        var result = new OpenAPIV3Parser()
                .readLocation(
                        input.toUri().toString(),
                        null,
                        options
                );

        if (result.getOpenAPI() == null ||
                !result.getMessages().isEmpty()) {

            throw new IllegalStateException(
                    "OpenAPI inválido: " + result.getMessages()
            );
        }

        Files.createDirectories(output.getParent());

        Files.writeString(
                output,
                Yaml.pretty(result.getOpenAPI())
        );

        System.out.println("OpenAPI gerado em: " + output);
    }
}