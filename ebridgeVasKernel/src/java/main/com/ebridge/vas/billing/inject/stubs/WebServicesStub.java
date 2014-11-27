package com.ebridge.vas.billing.inject.stubs;

import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.*;

import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public class WebServicesStub extends AbstractWebServicesStub {

    @Override
    public QueryAcmBalRetDto queryAcmBal(QueryAcmBalReqDto queryAcmBalReqDto) throws RemoteException {

        System.out.println("############################# POSTPAID ############################# ");

        QueryAcmBalRetDto response = new QueryAcmBalRetDto();
        response.setBalance("199099145.38");
        response.setExpDate("15/11/2014");

        return response;
    }

    @Override
    public QueryUserProfileRetDto queryUserProfile(
                QueryUserProfileReqDto queryUserProfileReqDto) throws RemoteException {

        QueryUserProfileRetDto result = new QueryUserProfileRetDto();
        UserProfileDto profile = new UserProfileDto();
        profile.setState("A");
        result.setUserProfileDto(profile);

        return result;
    }

    @Override
    public void addUserAcctByIndiPricePlanSubs(
                    AddUserAcctByIndiPricePlanSubsReqDto addUserAcctByIndiPricePlanSubsReqDto)
            throws RemoteException {

    }

}
