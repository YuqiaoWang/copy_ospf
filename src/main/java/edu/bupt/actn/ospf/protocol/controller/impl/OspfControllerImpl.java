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

package edu.bupt.actn.ospf.protocol.controller.impl;

import com.ericsson.otp.erlang.OtpErlangTuple;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import edu.bupt.actn.ospf.jinterface.Interaction;
import edu.bupt.actn.ospf.jinterface.TupleAreas;
import edu.bupt.actn.ospf.onos.DriverService;
import edu.bupt.actn.ospf.protocol.controller.*;
import edu.bupt.actn.ospf.protocol.controller.area.OspfClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.bupt.actn.ospf.provider.OspfTopologyProvider;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representation of an OSPF controller implementation.
 * Serves as a one stop shop for obtaining OSPF devices and (un)register listeners on OSPF events
 */
//@Component(immediate = true)
//@Service
public class OspfControllerImpl implements OspfController {

    protected static final Logger log = LoggerFactory.getLogger(OspfControllerImpl.class);
    //This is a static object which means that it can be assigned only once.
    private static OspfControllerImpl ospfController;
    private final Controller ctrl = new Controller();
//    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DriverService driverService;
    protected Set<OspfRouterListener> ospfRouterListener = new HashSet<>();
    protected Set<OspfLinkListener> ospfLinkListener = Sets.newHashSet();
    protected OspfAgent agent = new InternalDeviceConfig();


//    @Activate
    public void activate() {
        log.info("OSPFControllerImpl activate...!!!");
        ctrl.start(agent, driverService);
        log.info("Started");
    }

//    @Deactivate
    public void deactivate() {
        ctrl.stop();
        log.info("Stopped");
    }

    private OspfControllerImpl() {
        // empty
    }

    /**
     * 开启单例模式。
     * @return
     */
    public static OspfControllerImpl getInstance() {
        if (ospfController == null) {
            ospfController = new OspfControllerImpl();
        }
        return ospfController;
    }

    @Override
    public void addRouterListener(OspfRouterListener listener) {
        if (!ospfRouterListener.contains(listener)) {
            this.ospfRouterListener.add(listener);
        }
    }

    @Override
    public void removeRouterListener(OspfRouterListener listener) {
        this.ospfRouterListener.remove(listener);
    }

    @Override
    public void addLinkListener(OspfLinkListener listener) {
        ospfLinkListener.add(listener);

    }

    @Override
    public void removeLinkListener(OspfLinkListener listener) {
        ospfLinkListener.remove(listener);

    }

    @Override
    public Set<OspfRouterListener> listener() {
        return ospfRouterListener;
    }

    @Override
    public Set<OspfLinkListener> linkListener() {
        return ospfLinkListener;
    }

    //
    @Override
    public List<OspfProcess> getAllConfiguredProcesses() {
        List<OspfProcess> processes = ctrl.getAllConfiguredProcesses();
        return processes;
    }

    //
    @Override
    public void updateConfig(JsonNode processesNode) {
        try {
            // 首先，从JSON文本中解析出OSPF进程的消息，
            // 其中包括在该进程中，本节点的接口，进程ID，area ID，router ID等等信息。
            List<OspfProcess> ospfProcesses = OspfConfigUtil.processes(processesNode);
            //if there is interface details then update configuration
            // 但是为毛只检查第一个进程？
            if (ospfProcesses.size() > 0 &&
                    ospfProcesses.get(0).areas() != null && ospfProcesses.get(0).areas().size() > 0 &&
                    ospfProcesses.get(0).areas().get(0) != null &&
                    ospfProcesses.get(0).areas().get(0).ospfInterfaceList().size() > 0) {
                ctrl.updateConfig(ospfProcesses);
            }
        } catch (Exception e) {
            log.debug("Error::updateConfig::{}", e.getMessage());
        }
    }

    @Override
    public void deleteConfig(List<OspfProcess> processes, String attribute) {
    }


    /**
     * 整个工程的入口函数。
     * @param args
     */
    public static void main(String[] args)  throws IOException {
        OspfControllerImpl ospfController = getInstance();
        //起控制器，记初始时间
        ospfController.activate();
        OspfTopologyProvider ospfTopologyProvider = OspfTopologyProvider.getInstance(ospfController);
        ospfTopologyProvider.activate();

        // JInterface API starts
        //Interaction.erlInteraction(args);
        Interaction interaction = new Interaction();
        TupleAreas tupleAreas = interaction.doInteraction();
        List<OspfProcess> ospfProcesses = OspfConfigUtil.processes(tupleAreas);






    }

    /**
     * Notifier for internal OSPF device and link changes.
     */
    //内部类
    private class InternalDeviceConfig implements OspfAgent {

        @Override
        public boolean addConnectedRouter(OspfRouter ospfRouter) {
            for (OspfRouterListener l : listener()) {
                l.routerAdded(ospfRouter);
            }
            return true;
        }

        @Override
        public void removeConnectedRouter(OspfRouter ospfRouter) {
            for (OspfRouterListener l : listener()) {
                l.routerRemoved(ospfRouter);
            }
        }

        @Override
        public void addLink(OspfRouter ospfRouter, OspfLinkTed ospfLinkTed) {
            for (OspfLinkListener l : linkListener()) {
                l.addLink(ospfRouter, ospfLinkTed);
            }

        }

        @Override
        public void deleteLink(OspfRouter ospfRouter, OspfLinkTed ospfLinkTed) {
            for (OspfLinkListener l : linkListener()) {
                l.deleteLink(ospfRouter, ospfLinkTed);
            }
        }
    }
}