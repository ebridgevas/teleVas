package com.ebridge.vas.esme;

/**
 * @author david@tekeshe.com
 */

public class HalysSmppBinder implements SmppBinder {

    /*
    private Session session;
    private SmppConfig config;
    private ServerPDUEventListener serverPDUEventListener;

    public static final String SYSTEM_TYPE;
    private static final Byte SMPP_VERSION;
    static {
        SYSTEM_TYPE = "EBridge";
        SMPP_VERSION = (byte) 0x34;
    }

    public SmppBinder(Session session, SmppConfig config, ServerPDUEventListener serverPDUEventListener) {
        this.session = session;
        this.config = config;
        this.serverPDUEventListener = serverPDUEventListener;
    }


    private void bind(SmppConfig ussdConfig, String commandLine, Boolean withPromotion, String channelType,
            BigDecimal postpaidLimit) {

        Long timeout = new Long(20 * 1000);

        TCPIPConnection ussdConnection = new TCPIPConnection(ussdConfig.getSmppIPAdress(), ussdConfig.getSmppPort());
        ussdConnection.setReceiveTimeout( timeout );
        ussdSession = new Session(ussdConnection);

        Map<String, USSDSession> userSessions = new HashMap<String, USSDSession>();
        //Utils.readUserSessionFromFile(ussdConfig.getSmppIPAdress());

        SmppBinder ussdBinder =
                new SmppBinder( ussdSession,
                        ussdConfig,
                        new ServerPDUEventListenerImpl(ussdSession, userSessions, postpaidLimit));
        BindResponse ussdBindResponse = null;
        while (true) {
            try {
                ussdBindResponse = ussdBinder.bind();
                System.out.println("########### ussdBindResponse.debugString() = " + ussdBindResponse.debugString());
                System.out.println(ussdBindResponse != null ? ussdBindResponse.debugString() : "NOT BOUND");
                if ( ussdBindResponse!= null && ussdBindResponse.getCommandStatus() == Data.ESME_ROK) {
                    break;
                } else {
                    // TODO Send notification to system admin.
                    try { Thread.sleep(5000); } catch(Exception e){};
                }
            } catch (PDUException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WrongSessionStateException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

//        if ( ussdBindResponse!= null && ussdBindResponse.getCommandStatus() == Data.ESME_ROK) {
//            new EnquiryLinkGenerator(ussdSession, userSessions, commandLine, ussdConfig.getSmppIPAdress()).start();
//        }
    }

    public BindResponse bind() throws PDUException, IOException, WrongSessionStateException, TimeoutException {

        System.out.println("binding to " + config);
        BindRequest request = new BindTransciever();
        request.setSystemId(config.getSystemId());
        request.setPassword(config.getSystemPassword());
        request.setSystemType(SYSTEM_TYPE);
        request.setInterfaceVersion(SMPP_VERSION);
        request.setAddressRange(SmppParamaters.getAddressRange());
        System.out.println("########### bind request: " + request.debugString());
        return session.bind(request, serverPDUEventListener);
    }

    */
}
