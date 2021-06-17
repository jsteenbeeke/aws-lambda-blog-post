package nl.jeroensteenbeeke.tech.isodate.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ISODateFormatter implements RequestHandler<Long,String> {
	@Override
	public String handleRequest(Long seconds, Context context)
	{
		LocalDateTime instant = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

		return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(instant);
	}
}
