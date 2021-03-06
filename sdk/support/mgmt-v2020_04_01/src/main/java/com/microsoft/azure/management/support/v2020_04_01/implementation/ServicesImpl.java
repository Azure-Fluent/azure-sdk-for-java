/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * abc
 */

package com.microsoft.azure.management.support.v2020_04_01.implementation;

import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import com.microsoft.azure.management.support.v2020_04_01.Services;
import rx.functions.Func1;
import rx.Observable;
import java.util.List;
import com.microsoft.azure.management.support.v2020_04_01.Service;

class ServicesImpl extends WrapperImpl<ServicesInner> implements Services {
    private final SupportManager manager;

    ServicesImpl(SupportManager manager) {
        super(manager.inner().services());
        this.manager = manager;
    }

    public SupportManager manager() {
        return this.manager;
    }

    @Override
    public Observable<Service> getAsync(String serviceName) {
        ServicesInner client = this.inner();
        return client.getAsync(serviceName)
        .map(new Func1<ServiceInner, Service>() {
            @Override
            public Service call(ServiceInner inner) {
                return new ServiceImpl(inner, manager());
            }
        });
    }

    @Override
    public Observable<Service> listAsync() {
        ServicesInner client = this.inner();
        return client.listAsync()
        .flatMap(new Func1<List<ServiceInner>, Observable<ServiceInner>>() {
            @Override
            public Observable<ServiceInner> call(List<ServiceInner> innerList) {
                return Observable.from(innerList);
            }
        })
        .map(new Func1<ServiceInner, Service>() {
            @Override
            public Service call(ServiceInner inner) {
                return new ServiceImpl(inner, manager());
            }
        });
    }

}
