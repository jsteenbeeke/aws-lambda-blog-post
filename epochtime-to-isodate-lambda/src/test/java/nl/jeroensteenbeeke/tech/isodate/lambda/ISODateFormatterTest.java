package nl.jeroensteenbeeke.tech.isodate.lambda;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class ISODateFormatterTest {

	@Test
	public void handleRequest() {
		ISODateFormatter formatter = new ISODateFormatter();

		assertThat(formatter.handleRequest(123456789L , null), equalTo("1973-11-29T21:33:09"));
	}
}