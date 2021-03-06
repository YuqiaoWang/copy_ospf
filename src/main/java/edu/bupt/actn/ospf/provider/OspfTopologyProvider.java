/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.bupt.actn.ospf.provider;


import org.onlab.packet.ChassisId;
import org.onlab.packet.Ip4Address;
import org.onosproject.net.*;
import org.onosproject.net.device.*;
import org.onosproject.net.link.*;
import org.onosproject.net.provider.AbstractProvider;
import org.onosproject.net.provider.ProviderId;
import org.slf4j.Logger;
import edu.bupt.actn.ospf.protocol.controller.*;

import java.util.LinkedList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Provider which advertises device descriptions to the core.
 */
//@Component(immediate = true)
public class OspfTopologyProvider extends AbstractProvider implements DeviceProvider, LinkProvider {

    private static OspfTopologyProvider ospfTopologyProvider;
    public static final long PSEUDO_PORT = 0xffffffff;
    private static final Logger log = getLogger(OspfTopologyProvider.class);
    // Default values for tunable parameters
    private static final String UNKNOWN = "unknown";
    final InternalTopologyProvider listener = new InternalTopologyProvider();

    // 下面者四个变量的赋值，需要通过外部的方法实现。
    //@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceProviderRegistry deviceProviderRegistry;
    //@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected LinkProviderRegistry linkProviderRegistry;
    //@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected LinkService linkService;
    //@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected OspfController controller;


    //This Interface that defines how this edu.bupt.actn.ospf.provider can interact with the core.
    private LinkProviderService linkProviderService;
    // The interface that defines how this Provider can interact with the core
    private DeviceProviderService deviceProviderService;

    /**
     * Creates an OSPF device edu.bupt.actn.ospf.provider.
     * public --> private.
     */
    private OspfTopologyProvider() {
        super(new ProviderId("l3", "org.onosproject.edu.bupt.actn.ospf.provider.ospf"));
    }

//    @Activate
    public void activate() {
        deviceProviderService = deviceProviderRegistry.register(this);
        linkProviderService = linkProviderRegistry.register(this);
        controller.addRouterListener(listener);
        controller.addLinkListener(listener);
        log.debug("IsisDeviceProvider::activate...!!!!");
    }

    public void setController(OspfController controller) {
        this.controller = controller;
    }

    /**
     * 保持单例模式
     * @return
     */
    public static OspfTopologyProvider getInstance(OspfController controller) {
        if (ospfTopologyProvider == null) {
            ospfTopologyProvider = new OspfTopologyProvider();
            ospfTopologyProvider.setController(controller);
        }

        return ospfTopologyProvider;
    }

//    @Deactivate
    public void deactivate() {
        log.debug("IsisDeviceProvider::deactivate...!!!!");
        deviceProviderRegistry.unregister(this);
        deviceProviderService = null;
        linkProviderRegistry.unregister(this);
        linkProviderService = null;
        controller.removeRouterListener(listener);
        controller.removeLinkListener(listener);
        log.info("deactivated...!!!");
    }

    @Override
    public boolean isReachable(DeviceId deviceId) {
        return true;
    }

    @Override
    public void changePortState(DeviceId deviceId, PortNumber portNumber, boolean enable) {
        log.info("changePortState on device {}", deviceId);
    }

    @Override
    public void triggerProbe(DeviceId deviceId) {
        log.info("Triggering probe on device {}", deviceId);
    }

    @Override
    public void roleChanged(DeviceId deviceId, MastershipRole newRole) {
        log.info("Accepting mastership role change for device {}", deviceId);
    }

    /**
     * Builds link description.
     *
     * @param ospfRouter  OSPF router instance
     * @param ospfLinkTed OSPF link TED instance
     * @return link description instance
     */
    private LinkDescription buildLinkDes(OspfRouter ospfRouter, OspfLinkTed ospfLinkTed) {
        long srcAddress = 0;
        long dstAddress = 0;
        boolean localPseduo = false;
        //Changing of port numbers
        srcAddress = Ip4Address.valueOf(ospfRouter.routerIp().toString()).toInt();
        dstAddress = Ip4Address.valueOf(ospfRouter.neighborRouterId().toString()).toInt();
        DeviceId srcId = DeviceId.deviceId(OspfRouterId.uri(ospfRouter.routerIp()));
        DeviceId dstId = DeviceId.deviceId(OspfRouterId.uri(ospfRouter.neighborRouterId()));
        if (ospfRouter.isDr()) {
            localPseduo = true;
        }
        if (localPseduo && srcAddress == 0) {
            srcAddress = PSEUDO_PORT;
        }

        ConnectPoint src = new ConnectPoint(srcId, PortNumber.portNumber(srcAddress));
        ConnectPoint dst = new ConnectPoint(dstId, PortNumber.portNumber(dstAddress));

        return new DefaultLinkDescription(src, dst, Link.Type.DIRECT, false);
    }

    /**
     * Internal topology Provider implementation.
     */
    protected class InternalTopologyProvider implements OspfRouterListener, OspfLinkListener {

        @Override
        public void routerAdded(OspfRouter ospfRouter) {
            String routerId = ospfRouter.routerIp().toString();
            log.info("Added device {}", routerId);
            DeviceId deviceId = DeviceId.deviceId(OspfRouterId.uri(ospfRouter.routerIp()));
            Device.Type deviceType = Device.Type.ROUTER;
            //If our routerType is Dr or Bdr type is PSEUDO
            if (ospfRouter.isDr()) {
                deviceType = Device.Type.ROUTER;
            } else {
                deviceType = Device.Type.VIRTUAL;
            }
            //deviceId = DeviceId.deviceId(routerDetails);
            ChassisId cId = new ChassisId();
            DefaultAnnotations.Builder newBuilder = DefaultAnnotations.builder();

            newBuilder.set(AnnotationKeys.TYPE, "l3");
            newBuilder.set("routerId", routerId);
            DeviceDescription description =
                    new DefaultDeviceDescription(OspfRouterId.uri(ospfRouter.routerIp()),
                            deviceType, UNKNOWN, UNKNOWN, UNKNOWN,
                            UNKNOWN, cId, newBuilder.build());
            deviceProviderService.deviceConnected(deviceId, description);
        }

        @Override
        public void routerRemoved(OspfRouter ospfRouter) {
            String routerId = ospfRouter.routerIp().toString();
            log.info("Delete device {}", routerId);
            DeviceId deviceId = DeviceId.deviceId(OspfRouterId.uri(ospfRouter.routerIp()));
            if (deviceProviderService == null) {
                return;
            }
            deviceProviderService.deviceDisconnected(deviceId);
            log.info("delete device {}", routerId);
        }

        @Override
        public void addLink(OspfRouter ospfRouter, OspfLinkTed ospfLinkTed) {
            log.debug("Addlink {}", ospfRouter.routerIp());
            LinkDescription linkDes = buildLinkDes(ospfRouter, ospfLinkTed);
            //If already link exists, return
            if (linkService.getLink(linkDes.src(), linkDes.dst()) != null || linkProviderService == null) {
                return;
            }
            //Updating ports of the link
            List<PortDescription> srcPortDescriptions = new LinkedList<>();
            srcPortDescriptions.add(new DefaultPortDescription(linkDes.src().port(), true));
            deviceProviderService.updatePorts(linkDes.src().deviceId(), srcPortDescriptions);

            List<PortDescription> dstPortDescriptions = new LinkedList<>();
            dstPortDescriptions.add(new DefaultPortDescription(linkDes.dst().port(), true));
            deviceProviderService.updatePorts(linkDes.dst().deviceId(), dstPortDescriptions);
            linkProviderService.linkDetected(linkDes);
        }

        @Override
        public void deleteLink(OspfRouter ospfRouter, OspfLinkTed ospfLinkTed) {
            log.debug("Delete link {}", ospfRouter.routerIp().toString());
            if (linkProviderService == null) {
                return;
            }
            LinkDescription linkDes = buildLinkDes(ospfRouter, ospfLinkTed);
            //Updating ports of the link
            List<PortDescription> srcPortDescriptions = new LinkedList<>();
            srcPortDescriptions.add(new DefaultPortDescription(linkDes.src().port(), true));
            deviceProviderService.updatePorts(linkDes.src().deviceId(), srcPortDescriptions);

            List<PortDescription> dstPortDescriptions = new LinkedList<>();
            dstPortDescriptions.add(new DefaultPortDescription(linkDes.dst().port(), true));
            deviceProviderService.updatePorts(linkDes.dst().deviceId(), dstPortDescriptions);
            linkProviderService.linkVanished(linkDes);
        }

        @Override
        public void routerChanged(OspfRouter ospfRouter) {
            log.info("Router changed is not supported currently");
        }
    }
}