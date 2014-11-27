package com.ebridge.vas.services.impl;

import com.ebridge.vas.dao.UserDao;
import com.ebridge.vas.dto.UserAuthenticationResponse;
import com.ebridge.vas.dto.WebAccessCommand;
import com.ebridge.vas.model.*;
import com.ebridge.vas.services.SecurityTokenSender;
import com.ebridge.vas.services.WebAccessCommandProcessor;
import com.ebridge.vas.services.impl.util.ActivationMessageQueue;
import com.ebridge.vas.util.DatabaseException;
import com.ebridge.vas.util.HttpResponseWriter;
import com.ebridge.vas.util.PasswordGenerator;
import com.ebridge.vas.util.UserNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author david@ebridgevas.com
 *
 */
public class ValidateUserIdCommandProcessor implements WebAccessCommandProcessor {

    private SecurityTokenSender securityTokenSender;

    public ValidateUserIdCommandProcessor( SecurityTokenSender securityTokenSender) {
        this.securityTokenSender = securityTokenSender;
    }

    @Override
    public void process( Map<String, String[]> parameters,
                         WebAccessCommand webAccessCommand,
                         Map<String, UserSession> userSessions,
                         HttpServletResponse httpServletResponse) throws IOException {

        String mobileNumber = parameters.get("mobile-number")[0];
        String additionalServiceCommand = "none";
        if ( parameters.get("additional-service-command") != null ) {
            additionalServiceCommand = parameters.get("additional-service-command")[0];
        }
        User user = null;
        UserAuthenticationResponse response = new UserAuthenticationResponse();

        try {

            user = UserDao.findUser(mobileNumber);

            if ("re-send-activation-code".equalsIgnoreCase(additionalServiceCommand)) {

                sendActivationCodeToDevice(response, user);
            } else if ("enterActivationCode".equals(user.getStatus())) {
                response.setUserAction( UserAction.ENTER_ACTIVATION_CODE );
                primeResponse(response, user);
            } else if ("forcePasswordChange".equals(user.getStatus())) {
                response.setUserAction( UserAction.SET_PASSWORD );
            } else if ("active".equals(user.getStatus())) {
                response.setMobileNumber( mobileNumber );
                response.setUserAction( UserAction.ENTER_PASSWORD );
            } else if ( "inactive".equals(user.getStatus()) ) {

                sendActivationCodeToDevice(response, user);
            } else {
                throw new IOException("Invalid subscription. Please reset password.");
            }

            response.setUserPhotoFilename( user.getUserPhotoFileName() );
        } catch (UserNotFoundException e) {
            response.setUserAction( UserAction.REGISTER_USER );
        } catch (DatabaseException e) {
            throw new IOException( e.getMessage() );
        }

//        UserIdValidationResponse response =
//                new UserIdValidationResponse(userId, idType, userAction);

        HttpResponseWriter.write(response, httpServletResponse);
    }

    private void primeResponse( UserAuthenticationResponse response,
                                User user ) {

        response.setFullName( user.getFirstName() + " " + user.getSurname() );
        response.setFirstName(user.getFirstName());
        response.setMobileNumber( user.getMobileNumber() );
        response.setEmailAddress( user.getEmailAddress() );
        response.setNotificationAgent(user.getNotificationAgent());

        String sessionId = user.getMobileNumber() + new PasswordGenerator().getRandomPassword();
//        userSessions.put(sessionId, new UserSession(user.getMobileNumber() , sessionId));
    }

    private void sendActivationCodeToDevice(UserAuthenticationResponse response, User user)
            throws DatabaseException, UserNotFoundException, IOException {

        ServiceCommandRequest request
                = new ServiceCommandRequest(
                ServiceCommand.GENERATE_ACTIVATION_CODE,
                user.getMobileNumber(),
                null,
                new PasswordGenerator().getRandomPassword());

        UserDao.update( request );
        new ActivationMessageQueue( securityTokenSender ).queue( UserDao.findUser( user.getMobileNumber() ) );

        response.setUserAction( UserAction.ENTER_ACTIVATION_CODE );

        primeResponse(response, user);
    }
}
