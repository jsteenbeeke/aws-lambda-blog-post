package nl.jeroensteenbeeke.tech.isodate.invocation;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ISODateInvoker {
	public static void main(String[] args) {

		Options options = createCommandLineOptions();

		DefaultParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(options, args);

			String key = commandLine.getOptionValue("key");
			String secret = commandLine.getOptionValue("secret");
			String region = commandLine.getOptionValue("region");
			String arn = commandLine.getOptionValue("arn");
			List<String> payloads = Arrays.stream(commandLine.getOptionValue("payload").split(","))
										  .filter(s -> s.matches("^[0-9]+$"))
										  .collect(Collectors.toList());

			AWSLambda client = createClient(key, secret, region);

			for (String payload : payloads) {
				printTimestamp(client, arn, payload);
			}

		} catch (ParseException e) {
			System.err.println(e.getMessage());
			new HelpFormatter().printHelp("ISODateInvoker", options);
		}

	}

	private static void printTimestamp(AWSLambda client, String arn, String payload) {
		ObjectMapper objectMapper = new ObjectMapper();

		InvokeResult invokeResult = client.invoke(new InvokeRequest()
														  .withFunctionName(arn)
														  .withPayload(payload));

		if (invokeResult.getStatusCode() == 200) {
			// Success
			try {
				System.out.printf("%s            : %s%n", payload, objectMapper.readValue(invokeResult
																								  .getPayload()
																								  .array(), String.class));
			} catch (IOException e) {
				System.out.printf("Could not read response: %s%n", e.getMessage());
			}
		} else {
			System.err.printf("Error         : %s%n", invokeResult.getFunctionError());
			System.err.printf("Log result    : %s%n", invokeResult.getLogResult());
		}
	}

	private static AWSLambda createClient(String key, String secret, String region) {
		return AWSLambdaClientBuilder.standard()
									 .withCredentials(new AWSStaticCredentialsProvider(
											 new BasicAWSCredentials(key, secret)
									 ))
									 .withRegion(region)
									 .build();
	}

	private static Options createCommandLineOptions() {
		Options options = new Options();
		options.addOption(Option
								  .builder()
								  .longOpt("key")
								  .hasArg()
								  .argName("key")
								  .type(String.class)
								  .desc("The Amazon client key")
								  .required()
								  .build());
		options.addOption(Option
								  .builder()
								  .longOpt("secret")
								  .hasArg()
								  .argName("secret")
								  .type(String.class)
								  .required()
								  .desc("The Amazon client secret")
								  .build());
		options.addOption(Option
								  .builder()
								  .longOpt("region")
								  .hasArg()
								  .argName("region")
								  .type(String.class)
								  .desc("The region the Lambda and repository are running in")
								  .required()
								  .build());
		options.addOption(Option
								  .builder()
								  .longOpt("arn")
								  .hasArg()
								  .argName("arn")
								  .type(String.class)
								  .desc("The ARN of the Lambda function to invoke")
								  .required()
								  .build());
		options.addOption(Option
								  .builder()
								  .longOpt("payload")
								  .hasArg()
								  .argName("payload")
								  .type(String.class)
								  .desc("A list of comma-separated integers to convert to date timestamps")
								  .required()
								  .build());
		return options;
	}
}
