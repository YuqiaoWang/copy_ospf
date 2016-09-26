package edu.bupt.actn.ospf.jinterface; /**
 * Created by template on 16-9-15.
 */

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;

public class TupleNodes {
    String routerId;
    Boolean isOpaqueEnable;
    Boolean externalRoutingCapability;
    TupleInterfaces interfaces;
    public TupleNodes(OtpErlangTuple rId,
                      OtpErlangTuple isOpqEnable,
                      OtpErlangTuple etnRCapability,
                      OtpErlangTuple ifs) {
        //System.out.println("A TupleNodes object has been generated.");
        routerId = ((OtpErlangString)rId.elementAt(1)).stringValue();
        isOpaqueEnable = ((OtpErlangAtom)isOpqEnable.elementAt(1)).booleanValue();
        externalRoutingCapability = ((OtpErlangAtom)etnRCapability.elementAt(1)).booleanValue();

        //System.out.println("isOpaqueEnable:"+isOpaqueEnable);
        //System.out.println("externalRoutingCapabilities:"+externalRoutingCapability);

        OtpErlangList ifs_list = (OtpErlangList)ifs.elementAt(1);
        OtpErlangTuple ifs_list_interfaceIndex =
                (OtpErlangTuple)ifs_list.elementAt(0);
        OtpErlangTuple ifs_list_hellloIntervalTime =
                (OtpErlangTuple)ifs_list.elementAt(1);
        OtpErlangTuple ifs_list_routerDeadIntervael =
                (OtpErlangTuple)ifs_list.elementAt(2);
        OtpErlangTuple ifs_list_interfaceType =
                (OtpErlangTuple)ifs_list.elementAt(3);
        OtpErlangTuple ifs_list_reTransmitInterval =
                (OtpErlangTuple)ifs_list.elementAt(4);
        OtpErlangTuple ifs_list_interfaceIpv4 =
                (OtpErlangTuple)ifs_list.elementAt(5);

        try{
            interfaces = new TupleInterfaces(
                    ifs_list_interfaceIndex,
                    ifs_list_hellloIntervalTime,
                    ifs_list_routerDeadIntervael,
                    ifs_list_interfaceType,
                    ifs_list_reTransmitInterval,
                    ifs_list_interfaceIpv4);
        }catch (Exception e) {e.printStackTrace();}



    }
}
