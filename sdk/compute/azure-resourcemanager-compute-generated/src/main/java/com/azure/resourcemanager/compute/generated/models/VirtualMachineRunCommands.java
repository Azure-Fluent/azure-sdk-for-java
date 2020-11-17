// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.compute.generated.models;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

/** Resource collection API of VirtualMachineRunCommands. */
public interface VirtualMachineRunCommands {
    /**
     * Lists all available run commands for a subscription in a location.
     *
     * @param location The location upon which run commands is queried.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the List Virtual Machine operation response.
     */
    PagedIterable<RunCommandDocumentBase> list(String location);

    /**
     * Lists all available run commands for a subscription in a location.
     *
     * @param location The location upon which run commands is queried.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the List Virtual Machine operation response.
     */
    PagedIterable<RunCommandDocumentBase> list(String location, Context context);

    /**
     * Gets specific run command for a subscription in a location.
     *
     * @param location The location upon which run commands is queried.
     * @param commandId The command id.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return specific run command for a subscription in a location.
     */
    RunCommandDocument get(String location, String commandId);

    /**
     * Gets specific run command for a subscription in a location.
     *
     * @param location The location upon which run commands is queried.
     * @param commandId The command id.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return specific run command for a subscription in a location.
     */
    Response<RunCommandDocument> getWithResponse(String location, String commandId, Context context);

    /**
     * The operation to delete the run command.
     *
     * @param resourceGroupName The name of the resource group.
     * @param vmName The name of the virtual machine where the run command should be deleted.
     * @param runCommandName The name of the virtual machine run command.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.compute.generated.models.ApiErrorException thrown if the request is rejected by
     *     server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    void delete(String resourceGroupName, String vmName, String runCommandName);

    /**
     * The operation to delete the run command.
     *
     * @param resourceGroupName The name of the resource group.
     * @param vmName The name of the virtual machine where the run command should be deleted.
     * @param runCommandName The name of the virtual machine run command.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.compute.generated.models.ApiErrorException thrown if the request is rejected by
     *     server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    void delete(String resourceGroupName, String vmName, String runCommandName, Context context);

    /**
     * The operation to get the run command.
     *
     * @param resourceGroupName The name of the resource group.
     * @param vmName The name of the virtual machine containing the run command.
     * @param runCommandName The name of the virtual machine run command.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.compute.generated.models.ApiErrorException thrown if the request is rejected by
     *     server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return describes a Virtual Machine run command.
     */
    VirtualMachineRunCommand getByVirtualMachine(String resourceGroupName, String vmName, String runCommandName);

    /**
     * The operation to get the run command.
     *
     * @param resourceGroupName The name of the resource group.
     * @param vmName The name of the virtual machine containing the run command.
     * @param runCommandName The name of the virtual machine run command.
     * @param expand The expand expression to apply on the operation.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.compute.generated.models.ApiErrorException thrown if the request is rejected by
     *     server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return describes a Virtual Machine run command.
     */
    Response<VirtualMachineRunCommand> getByVirtualMachineWithResponse(
        String resourceGroupName, String vmName, String runCommandName, String expand, Context context);

    /**
     * The operation to get all run commands of a Virtual Machine.
     *
     * @param resourceGroupName The name of the resource group.
     * @param vmName The name of the virtual machine containing the run command.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.compute.generated.models.ApiErrorException thrown if the request is rejected by
     *     server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the List run command operation response.
     */
    PagedIterable<VirtualMachineRunCommand> listByVirtualMachine(String resourceGroupName, String vmName);

    /**
     * The operation to get all run commands of a Virtual Machine.
     *
     * @param resourceGroupName The name of the resource group.
     * @param vmName The name of the virtual machine containing the run command.
     * @param expand The expand expression to apply on the operation.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.compute.generated.models.ApiErrorException thrown if the request is rejected by
     *     server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the List run command operation response.
     */
    PagedIterable<VirtualMachineRunCommand> listByVirtualMachine(
        String resourceGroupName, String vmName, String expand, Context context);

    /**
     * Begins definition for a new VirtualMachineRunCommand resource.
     *
     * @param name resource name.
     * @return the first stage of the new VirtualMachineRunCommand definition.
     */
    VirtualMachineRunCommand.DefinitionStages.Blank define(String name);
}
