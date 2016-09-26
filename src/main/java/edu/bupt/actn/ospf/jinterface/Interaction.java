package edu.bupt.actn.ospf.jinterface; /**
 * Created by yuqia_000 on 2016/9/11.
 */
import com.ericsson.otp.erlang.*;

import java.io.IOException;


public class Interaction {
    OtpErlangTuple t;
    OtpErlangPid from;
    OtpErlangTuple subt;
    TupleAreas areas;

    /*public static void erlInteraction(String[] args) throws IOException {
        if ((args.length != 3)&&(args.length != 0)) {
            System.out.println("wrong number of arguments");
            System.out.println("expected: nodeName mailboxName cookie");
            return;
        }

        if(args.length == 0) {
            Interaction ex = new Interaction();
            System.out.println("JavaNode is now working!");
            ex.doInteraction();
        }


        if(args.length == 3) {
            Interaction ex = new Interaction(args[0], args[1], args[2]);
            System.out.println("JavaNode is now working!");
            ex.doInteraction();
        }

    }*/

    private OtpNode node;
    private OtpMbox mbox;

    public Interaction (String nodeName,
                        String mboxName,
                        String cookie)
            throws Exception{
        super();
        node = new OtpNode(nodeName,cookie);
        mbox = node.createMbox(mboxName);
    }
    public Interaction () throws IOException {
        super();
        node = new OtpNode("javaNode", "linc");
        mbox = node.createMbox("theMailbox");
    }

    public TupleAreas doInteraction() {
        //while (true) {
            try {
                OtpErlangObject msg = mbox.receive();
                System.out.println("Receive a message from erlang already!");
                t = (OtpErlangTuple) msg;
                from = (OtpErlangPid) t.elementAt(0);
                subt = (OtpErlangTuple)t.elementAt(1);

                String recognition = ((OtpErlangAtom)subt.elementAt(0))
                                                         .atomValue();
                //System.out.println(recognition);
                switch (recognition) {
                    case "areas" :
                        System.out.println(recognition);
                        tupleParseAreas(subt);

                        break;
                }



                //String OstMsg;
                OtpErlangString repmsg;
                repmsg = new OtpErlangString("OK!");

                OtpErlangTuple outMsg =
                        new OtpErlangTuple(new OtpErlangObject[]{mbox.self(),repmsg});
                mbox.send(from,outMsg);
            }catch (Exception e) {
                System.out.println("caught error: "+e);
            }
        //}

        return areas;

    }

    public void tupleParseAreas(OtpErlangTuple tuple) {
        //System.out.println("Function:tupleParseAreas is right working.");
        OtpErlangList subtuple_list =
                (OtpErlangList)tuple.elementAt(1);
        OtpErlangTuple subtuple_list_areaId =
                (OtpErlangTuple)subtuple_list.elementAt(0);
        OtpErlangTuple subtuple_list_nodes =
                (OtpErlangTuple)subtuple_list.elementAt(1);
        OtpErlangTuple subtuple_list_links =
                (OtpErlangTuple)subtuple_list.elementAt(2);
        try{
            areas = new TupleAreas(subtuple_list_areaId,
                                              subtuple_list_nodes,
                                              subtuple_list_links);
        }catch (Exception e) {e.printStackTrace();}

    }
}
