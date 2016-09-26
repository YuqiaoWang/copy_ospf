package edu.bupt.actn.ospf.jinterface; /**
 * Created by template on 16-9-15.
 */

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;

public class TupleLinks {
    String srcRouterId;
    String dstRouterId;
    int srcInterfaceIndex;
    int dstInterfaceIndex;
    int wavelength;
    int bandwidthPerWave;
    int cost;

    public TupleLinks (OtpErlangTuple sRId,
                      OtpErlangTuple dRId,
                      OtpErlangTuple sIfIndex,
                      OtpErlangTuple dIfIndex,
                      OtpErlangTuple wl,
                      OtpErlangTuple bdwPerWave,
                      OtpErlangTuple cst) throws Exception {
        //System.out.println("A TupleLinks object has been generated.");
        srcRouterId = ((OtpErlangString)sRId.elementAt(1)).stringValue();
        dstRouterId = ((OtpErlangString)dRId.elementAt(1)).stringValue();
        srcInterfaceIndex = ((OtpErlangLong)sIfIndex.elementAt(1)).intValue();
        dstInterfaceIndex = ((OtpErlangLong)dIfIndex.elementAt(1)).intValue();
        wavelength = ((OtpErlangLong)wl.elementAt(1)).intValue();
        bandwidthPerWave = ((OtpErlangLong)bdwPerWave.elementAt(1)).intValue();
        cost = ((OtpErlangLong)cst.elementAt(1)).intValue();

        //System.out.println("bandwidthPerWave:"+bandwidthPerWave);


    }

}
