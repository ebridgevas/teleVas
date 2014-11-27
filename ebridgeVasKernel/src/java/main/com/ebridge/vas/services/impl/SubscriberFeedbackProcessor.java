package com.ebridge.vas.services.impl;

import com.ebridge.vas.dao.TxnDAO;
import com.ebridge.vas.dto.WebAccessCommand;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.services.WebAccessCommandProcessor;
import com.ebridge.vas.util.HttpResponseWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class SubscriberFeedbackProcessor implements WebAccessCommandProcessor {

    @Override
    public void process( Map<String, String[]> parameters,
            WebAccessCommand webAccessCommand,
            Map<String, UserSession> userSessions,
            HttpServletResponse httpServletResponse ) throws IOException {

        String mobileNumber = parameters.get("mobile-number")[0];
        String feedback = parameters.get("feedback")[0];

        try {

            TxnDAO.persistFeedback(mobileNumber, feedback);

            HttpResponseWriter.write("ACK", httpServletResponse);

        } catch ( Exception e ) {
            // TODO Write to database
            e.printStackTrace();
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().write("A system error occurred. Please try after 15 minutes.");
        }
    }
}