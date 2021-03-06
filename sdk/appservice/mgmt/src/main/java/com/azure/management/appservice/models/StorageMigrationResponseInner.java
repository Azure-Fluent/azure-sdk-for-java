// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.management.appservice.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.annotation.JsonFlatten;
import com.azure.management.appservice.ProxyOnlyResource;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The StorageMigrationResponse model. */
@JsonFlatten
@Immutable
public class StorageMigrationResponseInner extends ProxyOnlyResource {
    /*
     * When server starts the migration process, it will return an operation ID
     * identifying that particular migration operation.
     */
    @JsonProperty(value = "properties.operationId", access = JsonProperty.Access.WRITE_ONLY)
    private String operationId;

    /**
     * Get the operationId property: When server starts the migration process, it will return an operation ID
     * identifying that particular migration operation.
     *
     * @return the operationId value.
     */
    public String operationId() {
        return this.operationId;
    }
}
