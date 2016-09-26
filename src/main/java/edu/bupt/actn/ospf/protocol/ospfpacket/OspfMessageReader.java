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

package edu.bupt.actn.ospf.protocol.ospfpacket;

import edu.bupt.actn.ospf.exceptions.OspfErrorType;
import edu.bupt.actn.ospf.exceptions.OspfParseException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.onlab.packet.Ip4Address;
import edu.bupt.actn.ospf.protocol.controller.OspfMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.bupt.actn.ospf.protocol.ospfpacket.types.*;
import edu.bupt.actn.ospf.protocol.util.OspfParameters;
import edu.bupt.actn.ospf.protocol.util.OspfUtil;

/**
 * A message reader which reads OSPF messages from ChannelBuffer and converts to OspfMessage instances.
 */
public class OspfMessageReader {
    private static final Logger log = LoggerFactory.getLogger(OspfMessageReader.class);

    /**
     * Reads and Converts the channel buffer to OspfMessage instance.
     *
     * @param channelBuffer channel buffer instance.
     * @return OSPF message instance.
     * @throws Exception might throws exception while parsing buffer
     */
    public OspfMessage readFromBuffer(ChannelBuffer channelBuffer)
            throws Exception {

        try {
            OspfPacketHeader ospfHeader = getOspfHeader(channelBuffer);
            OspfMessage ospfMessage = null;
            switch (ospfHeader.ospfType()) {
                case OspfParameters.HELLO:
                    ospfMessage = new HelloPacket(ospfHeader);
                    break;
                case OspfParameters.DD:
                    ospfMessage = new DdPacket(ospfHeader);
                    break;
                case OspfParameters.LSREQUEST:
                    ospfMessage = new LsRequest(ospfHeader);
                    break;
                case OspfParameters.LSUPDATE:
                    ospfMessage = new LsUpdate(ospfHeader);
                    break;
                case OspfParameters.LSACK:
                    ospfMessage = new LsAcknowledge(ospfHeader);
                    break;
                default:
                    log.debug("Message Reader[Decoder] - Unknown LSA type..!!!");
                    break;
            }

            if (ospfMessage != null) {
                try {
                    log.debug("{} Received::Message Length :: {} ", ospfMessage.ospfMessageType(),
                              ospfHeader.ospfPacLength());
                    ospfMessage.readFrom(channelBuffer.readBytes(ospfHeader.ospfPacLength() -
                                                                         OspfUtil.OSPF_HEADER_LENGTH));
                } catch (Exception e) {
                    throw new OspfParseException(OspfErrorType.OSPF_MESSAGE_ERROR,
                                                 OspfErrorType.BAD_MESSAGE);
                }

            }

            return ospfMessage;
        } catch (Exception e) {
            throw new OspfParseException(OspfErrorType.OSPF_MESSAGE_ERROR,
                                         OspfErrorType.BAD_MESSAGE);
        }
    }

    /**
     * Gets the OSPF packet Header.
     *
     * @param channelBuffer channel buffer instance.
     * @return Ospf Header instance.
     */
    private OspfPacketHeader getOspfHeader(ChannelBuffer channelBuffer) throws Exception {
        OspfPacketHeader ospfPacketHeader = new OspfPacketHeader();

        // Determine OSPF version & Packet Type
        int version = channelBuffer.readByte(); //byte 1 is ospf version
        int packetType = channelBuffer.readByte(); //byte 2 is ospf packet type

        // byte 3 & 4 combine is packet length.
        int packetLength = channelBuffer.readShort();

        byte[] tempByteArray = new byte[OspfUtil.FOUR_BYTES];
        channelBuffer.readBytes(tempByteArray, 0, OspfUtil.FOUR_BYTES);
        Ip4Address routerId = Ip4Address.valueOf(tempByteArray);

        tempByteArray = new byte[OspfUtil.FOUR_BYTES];
        channelBuffer.readBytes(tempByteArray, 0, OspfUtil.FOUR_BYTES);
        Ip4Address areaId = Ip4Address.valueOf(tempByteArray);

        int checkSum = channelBuffer.readUnsignedShort();
        int auType = channelBuffer.readUnsignedShort();
        int authentication = (int) channelBuffer.readLong();

        ospfPacketHeader.setOspfVer(version);
        ospfPacketHeader.setOspftype(packetType);
        ospfPacketHeader.setOspfPacLength(packetLength);
        ospfPacketHeader.setRouterId(routerId);
        ospfPacketHeader.setAreaId(areaId);
        ospfPacketHeader.setChecksum(checkSum);
        ospfPacketHeader.setAuthType(auType);
        ospfPacketHeader.setAuthentication(authentication);

        return ospfPacketHeader;
    }
}