// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.compute.generated.models;

import com.azure.core.annotation.Immutable;
import com.azure.resourcemanager.compute.generated.fluent.models.ComputeOperationValueInner;

/** An immutable client-side representation of ComputeOperationValue. */
@Immutable
public interface ComputeOperationValue {
    /**
     * Gets the origin property: The origin of the compute operation.
     *
     * @return the origin value.
     */
    String origin();

    /**
     * Gets the name property: The name of the compute operation.
     *
     * @return the name value.
     */
    String name();

    /**
     * Gets the operation property: The display name of the compute operation.
     *
     * @return the operation value.
     */
    String operation();

    /**
     * Gets the resource property: The display name of the resource the operation applies to.
     *
     * @return the resource value.
     */
    String resource();

    /**
     * Gets the description property: The description of the operation.
     *
     * @return the description value.
     */
    String description();

    /**
     * Gets the provider property: The resource provider for the operation.
     *
     * @return the provider value.
     */
    String provider();

    /**
     * Gets the inner com.azure.resourcemanager.compute.generated.fluent.models.ComputeOperationValueInner object.
     *
     * @return the inner object.
     */
    ComputeOperationValueInner innerModel();
}
