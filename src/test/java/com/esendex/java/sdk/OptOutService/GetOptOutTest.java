package com.esendex.java.sdk.OptOutService;

import com.esendex.java.sdk.BaseTest;
import com.esendex.java.sdk.TestServer;
import com.pyruby.stubserver.StubMethod;
import esendex.sdk.java.EsendexException;
import esendex.sdk.java.model.domain.response.OptOutResponse;
import esendex.sdk.java.service.OptOutService;
import esendex.sdk.java.service.auth.BasicAuthenticator;
import esendex.sdk.java.service.auth.UserPassword;
import esendex.sdk.java.service.impl.OptOutServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class GetOptOutTest extends BaseTest {
    private OptOutResponse result;
    private Date expectedReceivedAt;

    @Before
    public void before() throws EsendexException {
        String responseBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<optout id=\"6d2b745d-c214-4154-bca9-b0ff9a6b94c1\" xmlns=\"http://api.esendex.com/ns/\">\n" +
                "    <accountreference>EX0012345</accountreference>\n" +
                "    <receivedat>2010-12-31T23:59:59.997Z</receivedat>\n" +
                "    <from><phonenumber>447000047075</phonenumber></from>\n" +
                "    <link rel=\"self\" href=\"https://api.esendex.com/v1.0/optouts/6d2b745d-c214-4154-bca9-b0ff9a6b94c1\" />\n" +
                "</optout>";

        Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        expectedReceivedAt = cal.getTime();

        TestServer server = new TestServer(44041);
        server.start();
        try {
            server.expect(StubMethod.get("/v1.0/optouts/THEID")).thenReturn(200, "application/xml", responseBody);

            UserPassword userPassword = new UserPassword("YourUsername", "YourPassword");
            OptOutService optOutService = new OptOutServiceImpl(new BasicAuthenticator(userPassword));

            result = optOutService.getOptOut("THEID");

            server.verify();
        } finally {
            server.stop();
        }
    }

    @Test
    public void thenTheOptOutPropertiesAreSet() {
        assertEquals("EX0012345", result.getAccountReference());
        assertEquals(expectedReceivedAt, result.getReceivedAt());
        assertEquals("447000047075", result.getFrom().getPhoneNumber());
    }
}

