// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.compute.generated.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Defines values for ContainerServiceOrchestratorTypes. */
public enum ContainerServiceOrchestratorTypes {
    /** Enum value Swarm. */
    SWARM("Swarm"),

    /** Enum value DCOS. */
    DCOS("DCOS"),

    /** Enum value Custom. */
    CUSTOM("Custom"),

    /** Enum value Kubernetes. */
    KUBERNETES("Kubernetes");

    /** The actual serialized value for a ContainerServiceOrchestratorTypes instance. */
    private final String value;

    ContainerServiceOrchestratorTypes(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a ContainerServiceOrchestratorTypes instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed ContainerServiceOrchestratorTypes object, or null if unable to parse.
     */
    @JsonCreator
    public static ContainerServiceOrchestratorTypes fromString(String value) {
        ContainerServiceOrchestratorTypes[] items = ContainerServiceOrchestratorTypes.values();
        for (ContainerServiceOrchestratorTypes item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
