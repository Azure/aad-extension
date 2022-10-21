package com.hivemq.extensions.helloworld;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput;
import com.hivemq.extension.sdk.api.client.parameter.ClientTlsInformation;
import com.hivemq.extension.sdk.api.client.parameter.ConnectionInformation;
import com.hivemq.extension.sdk.api.packets.connect.ConnectPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Optional;

public class MyAuthenticator implements SimpleAuthenticator {
    private static final @NotNull Logger log = LoggerFactory.getLogger(MyAuthenticator.class);

    @Override
    public void onConnect(@NotNull SimpleAuthInput simpleAuthInput, @NotNull SimpleAuthOutput simpleAuthOutput) {

        ConnectPacket packet = simpleAuthInput.getConnectPacket();
        log.info("Username: " + packet.getUserName());
        log.info("Password: " + packet.getPassword());

        var connectInfo = simpleAuthInput.getConnectionInformation();
        var tlsInfo = connectInfo.getClientTlsInformation().get();

        try {

            var clientCert = tlsInfo.getClientCertificate().get();
            var subAltNames = clientCert.getSubjectAlternativeNames();
            var hostNameList = new ArrayList<String>();
            
            if (subAltNames != null) {
                for(var altName : subAltNames) {
                    if(altName.size()< 2) continue;
                    switch((Integer)altName.get(0)) {
                        case 2:
                        case 7:
                            Object data = altName.get(1);
                            if (data instanceof String) {
                                hostNameList.add(((String)data));
                            }
                            break;
                        default:
                    }
                }
            }
            System.out.println("Parsed hostNames: " + String.join(", ", hostNameList));
        } catch (CertificateParsingException e) {
            throw new RuntimeException(e);
        }
        simpleAuthOutput.authenticateSuccessfully();

    }
}
