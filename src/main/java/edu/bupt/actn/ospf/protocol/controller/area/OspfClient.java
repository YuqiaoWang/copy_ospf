package edu.bupt.actn.ospf.protocol.controller.area;

/**
 * Created by template on 16-9-24.
 */


import edu.bupt.actn.ospf.protocol.controller.OspfController;
import edu.bupt.actn.ospf.protocol.controller.OspfProcess;
import edu.bupt.actn.ospf.protocol.controller.impl.Controller;
import edu.bupt.actn.ospf.protocol.controller.impl.OspfInterfaceChannelHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.ssl.SslContext;
import org.jboss.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.List;
import java.nio.channels.SocketChannel;


import org.onlab.packet.Ip4Address;
import org.onosproject.net.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class OspfClient {
    private static final Logger log = LoggerFactory.getLogger(OspfClient.class);

    Controller controller;
    List<OspfProcess> processes;

    private Channel channel;
    protected String channelId;

    static String HOST = System.getProperty("host", "10.108.48.226");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
    public ChannelFuture f;
    public OspfInterfaceChannelHandler ospfInterfaceChannelHandler;
    //final SslContext sslCtx = SslContext.newClientContext();
    //public Ip4Address remoteAddress;


    public OspfClient(Controller controller, List<OspfProcess> processes) {
        this.controller = controller;
        this.processes = processes;
        ospfInterfaceChannelHandler =
                new OspfInterfaceChannelHandler(controller, processes);
    }

    public void startClient() throws Exception {
        //Cofigure the bootstrap.
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()
                ));
        try{
            bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                @Override
                public ChannelPipeline getPipeline() throws Exception {
                    ChannelPipeline p = Channels.pipeline();
                    p.addLast("echo", ospfInterfaceChannelHandler);
                    return p;
                }
            });
            bootstrap.setOption("tcpNoDelay", true);
            bootstrap.setOption("receiveBufferSize", 1048576);
            bootstrap.setOption("sendBufferSize", 1048576);


            //Start the connetction attempt.
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST, PORT));

            //Wait until the connetction is closed or the connection attempt failed.
//            future.getChannel().getCloseFuture().sync();

        } finally {
            //bootstrap.releaseExternalResources();

        }





    }




        /*
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELEY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                        }
                    })*/


}
