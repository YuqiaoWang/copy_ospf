package edu.bupt.actn.ospf.jinterface; /**
 * Created by template on 16-9-15.
 */

import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangTuple;

public class TupleAreas {
    int areaId;
    TupleNodes nodes;
    TupleLinks links;
    public TupleAreas(OtpErlangTuple t1,
                      OtpErlangTuple t2,
                      OtpErlangTuple t3) throws Exception{
        //System.out.println("A TupleAreas object has been generated.");
        areaId = ((OtpErlangLong)t1.elementAt(1)).intValue();

        OtpErlangList nodes_list = (OtpErlangList)t2.elementAt(1);
        OtpErlangTuple nodes_list_routerId =
                (OtpErlangTuple)nodes_list.elementAt(0);
        OtpErlangTuple nodes_list_isOpaqueEnable =
                (OtpErlangTuple)nodes_list.elementAt(1);
        OtpErlangTuple nodes_list_externalRoutingCapability =
                (OtpErlangTuple)nodes_list.elementAt(2);
        OtpErlangTuple nodes_list_interfaces =
                (OtpErlangTuple)nodes_list.elementAt(3);
        nodes = new TupleNodes(
                nodes_list_routerId,
                nodes_list_isOpaqueEnable,
                nodes_list_externalRoutingCapability,
                nodes_list_interfaces);

        OtpErlangList links_list = (OtpErlangList)t3.elementAt(1);
        OtpErlangTuple links_list_srcRouterId =
                (OtpErlangTuple)links_list.elementAt(0);
        OtpErlangTuple links_list_dstRouterId =
                (OtpErlangTuple)links_list.elementAt(1);
        OtpErlangTuple links_list_srcInterfaceIndex =
                (OtpErlangTuple)links_list.elementAt(2);
        OtpErlangTuple links_list_dstInterfaceIndex =
                (OtpErlangTuple)links_list.elementAt(3);
        OtpErlangTuple links_list_wavelength =
                (OtpErlangTuple)links_list.elementAt(4);
        OtpErlangTuple links_list_bandwidthPerWave =
                (OtpErlangTuple)links_list.elementAt(5);
        OtpErlangTuple links_list_cost =
                (OtpErlangTuple)links_list.elementAt(6);
        links = new TupleLinks(
                links_list_srcRouterId,
                links_list_dstRouterId,
                links_list_srcInterfaceIndex,
                links_list_dstInterfaceIndex,
                links_list_wavelength,
                links_list_bandwidthPerWave,
                links_list_cost);
    }

}
