# Azure Stack Profiles Overview

- [Changes Summary](#changes-summary)
- [Current SDK profile](#current-sdk-profile)
    - [2019-03-01](#2019-03-01)
    - [2020-09-01](#2020-09-01)
- [New SDK Planning](#new-sdk-planning)
    - [Important Change](#important-change)
    - [Release without profile](#release-without-profile)
    - [Release with profile](#release-with-profile)

## Changes Summary
* package and namespace changed
* Usage changed slightly
* [http client changed](#important-change) (affect the azure-stack cipher suites)

## Current SDK profile

### 2019-03-01

**Package**
```xml
<!-- https://mvnrepository.com/artifact/com.microsoft.azure.profile_2019_03_01_hybrid/azure -->
<dependency>
    <groupId>com.microsoft.azure.profile_2019_03_01_hybrid</groupId>
    <artifactId>azure</artifactId>
    <version>1.0.0-beta-1</version>
</dependency>
```

**Sample Usage**
```java
Azure azure = Azure.authenticate(credential).withDefaultSubscription();
azure.virtualMachines().list();
azure.virtualMachines().define()...
```
> Authentication Credential could refer to https://github.com/Azure/azure-libraries-for-java/blob/master/AUTH.md

### 2020-09-01

*Note: not released yet*

**Package** (after release)
```xml
<!-- https://mvnrepository.com/artifact/com.microsoft.azure.profile_2020_09_01_hybrid/azure -->
<dependency>
    <groupId>com.microsoft.azure.profile_2020_09_01_hybrid</groupId>
    <artifactId>azure</artifactId>
    <version>1.0.0-beta</version>
</dependency>
```

**Sample Usage**
```java
Azure azure = Azure.authenticate(credential).withDefaultSubscription();
azure.computeManager().virtualMachines().list();
azure.computeManager().virtualMachines().define()...
```
> Authentication Credential could refer to https://github.com/Azure/azure-libraries-for-java/blob/master/AUTH.md

## New SDK Planning

### Important Change

Due to the http client is not controlled by SDK, we could not be able to add [customized cipher suites](https://github.com/Azure/azure-sdk-for-java/blob/5f1ea7d/profiles/2019-03-01-hybrid/azure/src/main/java/com/microsoft/azure/management/profile_2019_03_01_hybrid/CustomCipherSuites.java)

If the default http client in azure-core like [netty](https://github.com/Azure/azure-sdk-for-java/tree/f805ae0c8d7b8eb9dbb30cb4164423ff84dd4326/sdk/core/azure-core-http-netty) or [okhttp](https://github.com/Azure/azure-sdk-for-java/tree/f805ae0c8d7b8eb9dbb30cb4164423ff84dd4326/sdk/core/azure-core-http-okhttp) cannot successfully send request to Azure Stack, it may need to build a customized http client for azure stack, which may need help from azure-core.

### Release without profile

**Package** (need to be imported separately)
```xml
<dependency>
    <groupId>com.azure.resourcemanager</groupId>
    <artifactId>azure-resourcemanager-compute-generated</artifactId>
    <version>1.0.0-stack</version>
</dependency>
<dependency>
    <groupId>com.azure.resourcemanager</groupId>
    <artifactId>azure-resourcemanager-storage-generated</artifactId>
    <version>1.0.0-stack</version>
</dependency>
...
```

**Sample Usage**
```java
ComputeManager computeManager = ComputeManager
    .configure()
    .withHttpClient(client)
    .authenticate(credential, profile);
computeManager.virtualMachines().list();
computeManager.virtualMachines().define()...
```
> Credential and Profile could be refer to https://github.com/Azure/azure-sdk-for-java/blob/master/sdk/resourcemanager/docs/AUTH.md

### Release with profile

**Package** (not decided yet)
```xml
<dependency>
    <groupId>com.azure.resourcemanager</groupId>
    <artifactId>azure-resourcemanager-generated</artifactId>
    <version>1.0.0-stack</version>
</dependency>
```

**Sample Usage**
```java
AzureResourceManager azure = AzureResourceManager
    .configure()
    .withHttpClient(client)
    .authenticate(credential, profile)
    .withDefaultSubscription();
azure.computeManager().virtualMachines().list();
azure.computeManager().virtualMachines().defines()...
```
> Credential and Profile could be refer to https://github.com/Azure/azure-sdk-for-java/blob/master/sdk/resourcemanager/docs/AUTH.md
