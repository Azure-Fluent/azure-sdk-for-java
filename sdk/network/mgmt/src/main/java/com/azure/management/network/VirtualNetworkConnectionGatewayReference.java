// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.management.network;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The VirtualNetworkConnectionGatewayReference model. */
@Fluent
public final class VirtualNetworkConnectionGatewayReference {
    /*
     * The ID of VirtualNetworkGateway or LocalNetworkGateway resource.
     */
    @JsonProperty(value = "id", required = true)
    private String id;

    /**
     * Get the id property: The ID of VirtualNetworkGateway or LocalNetworkGateway resource.
     *
     * @return the id value.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set the id property: The ID of VirtualNetworkGateway or LocalNetworkGateway resource.
     *
     * @param id the id value to set.
     * @return the VirtualNetworkConnectionGatewayReference object itself.
     */
    public VirtualNetworkConnectionGatewayReference withId(String id) {
        this.id = id;
        return this;
    }
}
