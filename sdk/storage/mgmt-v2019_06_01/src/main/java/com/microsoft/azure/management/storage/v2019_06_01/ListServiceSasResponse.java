/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.storage.v2019_06_01;

import com.microsoft.azure.arm.model.HasInner;
import com.microsoft.azure.arm.resources.models.HasManager;
import com.microsoft.azure.management.storage.v2019_06_01.implementation.StorageManager;
import com.microsoft.azure.management.storage.v2019_06_01.implementation.ListServiceSasResponseInner;

/**
 * Type representing ListServiceSasResponse.
 */
public interface ListServiceSasResponse extends HasInner<ListServiceSasResponseInner>, HasManager<StorageManager> {
    /**
     * @return the serviceSasToken value.
     */
    String serviceSasToken();

}
