// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.implementation.encryption;

import com.azure.cosmos.implementation.Utils;
import com.azure.cosmos.implementation.encryption.api.CosmosEncryptionAlgorithm;
import com.azure.cosmos.implementation.encryption.api.DataEncryptionKey;
import com.azure.cosmos.implementation.encryption.api.EncryptionOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EncryptionProcessorTest {

    public EncryptionProcessorTest() {}

    private byte[] key;

    @BeforeClass(groups = "unit")
    public void beforeClass() throws Exception {
        key = TestUtils.generatePBEKeySpec("myPassword");
    }

    public static class TestPojo {
        @JsonProperty
        public String id;
        @JsonProperty
        public String pk;
        @JsonProperty
        public String nonSensitive;
        @JsonProperty
        public String sensitive;
    }

    private TestPojo getTestDate() {

        TestPojo test = new TestPojo();
        test.id = UUID.randomUUID().toString();
        test.pk = UUID.randomUUID().toString();
        test.nonSensitive = UUID.randomUUID().toString();
        test.sensitive = UUID.randomUUID().toString();

        return test;
    }

    @Test(groups = "unit")
    public void aesEncryptThenDecrypt()  {
        AeadAes256CbcHmac256EncryptionKey aeadAesKey = new AeadAes256CbcHmac256EncryptionKey(key, "AES");
        AeadAes256CbcHmac256Algorithm encryptionAlgorithm = new AeadAes256CbcHmac256Algorithm(aeadAesKey, EncryptionType.RANDOMIZED, (byte) 0x01);
        DataEncryptionKey javaDataEncryptionKey = new DataEncryptionKey() {
            String keyId = UUID.randomUUID().toString();

            @Override
            public String getId() {
                return keyId;
            }

            @Override
            public byte[] getRawKey() {
                return  key;
            }

            @Override
            public CosmosEncryptionAlgorithm getCosmosEncryptionAlgorithm() {
                return CosmosEncryptionAlgorithm.AE_AES_256_CBC_HMAC_SHA_256_RANDOMIZED;
            }

            @Override
            public byte[] encryptData(byte[] plainText) {
                return encryptionAlgorithm.encryptData(plainText);
            }

            @Override
            public byte[] decryptData(byte[] cipherText) {
                return encryptionAlgorithm.decryptData(cipherText);
            }
        };

        JavaKeyProvider keyProvider = new JavaKeyProvider();
        keyProvider.addKey(javaDataEncryptionKey);

        EncryptionProcessor encryptionProcessor = new EncryptionProcessor();
        com.azure.cosmos.implementation.encryption.api.EncryptionOptions encryptionOptions = new EncryptionOptions();
        encryptionOptions.setPathsToEncrypt(ImmutableList.of("/sensitive"));
        encryptionOptions.setDataEncryptionKeyId(javaDataEncryptionKey.getId());

        TestPojo testDate = getTestDate();
        byte[] inputAsByteArray = toByteArray(testDate);

        ObjectNode objectNode = Utils.getSimpleObjectMapper().convertValue(testDate, ObjectNode.class);
        ObjectNode itemObjectWithEncryptedSensitiveData = encryptionProcessor.encryptAsync(objectNode, encryptionOptions, keyProvider);
        ObjectNode itemObjectWithDecryptedSensitiveData = encryptionProcessor.decryptAsync(itemObjectWithEncryptedSensitiveData, keyProvider);

        assertThat(serializeToByteArray(Utils.getSimpleObjectMapper(), itemObjectWithDecryptedSensitiveData)).isEqualTo(inputAsByteArray);
    }

    @DataProvider(name = "encryptionInput")
    public Object[][] encryptionInput() {
        return new Object[][]{
            { new byte[] {} },
            {"secret".getBytes(StandardCharsets.UTF_8) },
            {"محرمانه".getBytes(StandardCharsets.UTF_8) },
            { RandomStringUtils.randomAlphabetic(100_000).getBytes(StandardCharsets.UTF_8) }
        };
    }

    private static byte[] toByteArray(Object object) {
        return serializeToByteArray(Utils.getSimpleObjectMapper(), object);
    }

    public static byte[] serializeToByteArray(ObjectMapper mapper, Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to convert JSON to byte[]", e);
        }
    }
}
