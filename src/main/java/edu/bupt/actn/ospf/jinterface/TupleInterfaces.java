package edu.bupt.actn.ospf.jinterface; /**
 * Created by template on 16-9-15.
 */

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;

public class TupleInterfaces {
    int interfaceIndex;
    int helloIntervalTime;
    int routerDeadIntervalTime;
    int interfaceType;
    int reTransmitInterval;
    String interfaceIPv4;

    public TupleInterfaces(OtpErlangTuple ifIndex,
                           OtpErlangTuple hlItvTime,
                           OtpErlangTuple rDItvTime,
                           OtpErlangTuple ifType,
                           OtpErlangTuple rTsmtItv,
                           OtpErlangTuple ifIpv4) throws Exception{
        //System.out.println("A TupleInterfaces object has been generated.");
        interfaceIndex = ((OtpErlangLong)ifIndex.elementAt(1)).intValue();
        helloIntervalTime = ((OtpErlangLong)hlItvTime.elementAt(1)).intValue();
        routerDeadIntervalTime = ((OtpErlangLong)rDItvTime.elementAt(1)).intValue();
        interfaceType = ((OtpErlangLong)ifType.elementAt(1)).intValue();
        reTransmitInterval = ((OtpErlangLong)rTsmtItv.elementAt(1)).intValue();
        interfaceIPv4 = ((OtpErlangString)ifIpv4.elementAt(1)).stringValue();
        //System.out.println("interfaceType:"+interfaceType);
        //System.out.println("interfaceIPv4:"+interfaceIPv4);

    }


}
