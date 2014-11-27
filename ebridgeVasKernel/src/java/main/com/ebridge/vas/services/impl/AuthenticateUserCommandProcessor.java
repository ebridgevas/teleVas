package com.ebridge.vas.services.impl;

import static com.ebridge.vas.util.Utils.error;

import com.ebridge.vas.dao.UserDao;
import com.ebridge.vas.dto.UserAuthenticationResponse;
import com.ebridge.vas.dto.WebAccessCommand;
import com.ebridge.vas.model.User;
import com.ebridge.vas.model.UserAction;
import com.ebridge.vas.model.UserSession;
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
 * david@ebridgevas.com
 */
public class AuthenticateUserCommandProcessor implements WebAccessCommandProcessor {

//    private AccountManager accountManager;
    private SecurityTokenSender securityTokenSender;

    public AuthenticateUserCommandProcessor( /* AccountManager accountManager, */
                                             SecurityTokenSender securityTokenSender) {

//        this.accountManager = accountManager;
        this.securityTokenSender = securityTokenSender;
    }

    @Override
    public void process( Map<String, String[]> parameters,
                         WebAccessCommand webAccessCommand,
                         Map<String, UserSession> userSessions,
                         HttpServletResponse httpServletResponse) throws IOException {

        String mobileNumber = null;
        String password = null;

        try {
            mobileNumber = parameters.get("mobile-number")[0];
            password = parameters.get("password")[0];
        } catch(Exception e) {
            error("A system error occurred and will be fixed. Please try again shortly .", httpServletResponse);
            return;
        }

        UserAuthenticationResponse response = new UserAuthenticationResponse();

        User user = null;

        try {
            user = UserDao.findUser(mobileNumber);

            if ("active".equalsIgnoreCase( user.getStatus())) {

                if (password == null || ! password.equals(user.getPassword())) {

                    response.setUserAction( UserAction.INVALID_PASSWORD );
                } else {

                    response.setUserAction( UserAction.GRANT_ACCESS );
                    String sessionId = mobileNumber + new PasswordGenerator().getRandomPassword();
                    response.setSessionId(sessionId);
                    response.setFullName( user.getFirstName() + " " + user.getSurname() );
                    response.setFirstName ( user.getFirstName() );
                    response.setLastName( user.getSurname() );
                    response.setMobileNumber( user.getMobileNumber() );
                    response.setEmailAddress( user.getEmailAddress() );
                    response.setRole( user.getRole() );
                    response.setSecurityQuestion( user.getSecurityQuestion());
                    response.setSecurityAnswer( user.getSecurityAnswer() );
                    response.setNotificationAgent( user.getNotificationAgent() );
                    response.setPassword( user.getPassword() );
//                    response.setAccountType(
//                            isPostpaid(user.getMobileNumber()) ?
//                                    AccountTye.POSTPAID : AccountTye.PREPAID );

                    userSessions.put(sessionId, new UserSession(mobileNumber, sessionId));
                }
            } else {

                new ActivationMessageQueue( securityTokenSender ).queue( user );

                response.setUserAction( UserAction.ENTER_ACTIVATION_CODE );
                response.setFullName( user.getFirstName() + " " + user.getSurname() );
                response.setFirstName(user.getFirstName());
                response.setMobileNumber( user.getMobileNumber() );
                response.setEmailAddress( user.getEmailAddress() );
                response.setNotificationAgent(user.getNotificationAgent());
            }

            HttpResponseWriter.write(response, httpServletResponse);
        } catch (UserNotFoundException e) {

            response.setUserAction(UserAction.REGISTER_USER);
        } catch (DatabaseException e) {

            error("Something went wrong. Please try again shortly.", httpServletResponse);
            return;
        }
    }

//    public Boolean isPostpaid(String msisdn) {
//
//
//        String cos = accountManager.getClassOfService(msisdn);
//
//        return      cos == null
//                || "STAFF_COS".equalsIgnoreCase(cos)
//                || "POST_COS".equalsIgnoreCase(cos)
//                || "VIP".equalsIgnoreCase(cos);
//    }
}
