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
package edu.bupt.actn.ospf.protocol.controller;

/**
 * Represents an OSPF LSA.
 */
public interface OspfLsa {

    /**
     * Gets the type of OSPF LSA.
     *
     * @return OSPF LSA type instance
     */
    OspfLsaType getOspfLsaType();

    /**
     * Gets the age of LSA.
     *
     * @return age of LSA
     */
    int age();

    /**
     * Gets the LSA header instance.
     *
     * @return this instance
     */
    public OspfLsa lsaHeader();
}