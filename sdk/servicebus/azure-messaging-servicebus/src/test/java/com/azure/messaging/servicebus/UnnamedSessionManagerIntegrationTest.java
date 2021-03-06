// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.messaging.servicebus;

import com.azure.core.amqp.AmqpRetryOptions;
import com.azure.core.util.CoreUtils;
import com.azure.core.util.logging.ClientLogger;
import com.azure.messaging.servicebus.ServiceBusClientBuilder.ServiceBusSessionReceiverClientBuilder;
import com.azure.messaging.servicebus.implementation.MessagingEntityType;
import com.azure.messaging.servicebus.models.ReceiveAsyncOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.azure.messaging.servicebus.TestUtils.getServiceBusMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link UnnamedSessionManager}.
 */
class UnnamedSessionManagerIntegrationTest extends IntegrationTestBase {
    private final AtomicInteger messagesPending = new AtomicInteger();

    private ServiceBusReceiverAsyncClient receiver;
    private ServiceBusSenderAsyncClient sender;

    UnnamedSessionManagerIntegrationTest() {
        super(new ClientLogger(UnnamedSessionManagerIntegrationTest.class));
    }

    @Override
    protected void beforeTest() {
        sessionId = UUID.randomUUID().toString();
    }

    @Override
    protected void afterTest() {
        final int pending = messagesPending.get();
        logger.info("Pending messages: {}", pending);
    }

    @ParameterizedTest
    @MethodSource("messagingEntityProvider")
    void singleUnnamedSession(MessagingEntityType entityType) {
        // Arrange
        final String messageId = "singleUnnamedSession";
        final String sessionId = "singleUnnamedSession-" + Instant.now().toString();
        final String contents = "hello world";
        final int numberToSend = 5;
        final ReceiveAsyncOptions receiveOptions = new ReceiveAsyncOptions()
            .setMaxAutoLockRenewalDuration(Duration.ofMinutes(2))
            .setIsAutoCompleteEnabled(true);

        setSenderAndReceiver(entityType, TIMEOUT, Function.identity());

        final Disposable subscription = Flux.interval(Duration.ofMillis(500))
            .take(numberToSend)
            .flatMap(index -> {
                final ServiceBusMessage message = getServiceBusMessage(contents, messageId)
                    .setSessionId(sessionId);
                return sender.send(message).thenReturn(index);
            }).subscribe(
                number -> logger.info("sessionId[{}] sent[{}] Message sent.", sessionId, number),
                error -> logger.error("sessionId[{}] Error encountered.", sessionId, error),
                () -> logger.info("sessionId[{}] Finished sending.", sessionId));

        // Act & Assert
        try {
            StepVerifier.create(receiver.receive(receiveOptions))
                .assertNext(context -> assertMessageEquals(sessionId, messageId, contents, context))
                .assertNext(context -> assertMessageEquals(sessionId, messageId, contents, context))
                .assertNext(context -> assertMessageEquals(sessionId, messageId, contents, context))
                .assertNext(context -> assertMessageEquals(sessionId, messageId, contents, context))
                .assertNext(context -> assertMessageEquals(sessionId, messageId, contents, context))
                .expectComplete()
                .verify(Duration.ofMinutes(2));
        } finally {
            subscription.dispose();
        }
    }

    /**
     * Verifies that we can roll over to a next session.
     */
    @Test
    void multipleSessions() {
        // Arrange
        final String messageId = "singleUnnamedSession";
        final String now = Instant.now().toString();
        final List<String> sessionIds = IntStream.range(0, 3)
            .mapToObj(number -> String.join("-", String.valueOf(number), "singleUnnamedSession", now))
            .collect(Collectors.toList());

        logger.info("------ Session ids ------");
        for (int i = 0; i < sessionIds.size(); i++) {
            logger.info("[{}]: {}", i, sessionIds.get(i));
        }

        final String contents = "hello world";
        final int numberToSend = 3;
        final int maxMessages = numberToSend * sessionIds.size();
        final int maxConcurrency = 2;
        final Set<String> set = new HashSet<>();
        final ReceiveAsyncOptions receiveOptions = new ReceiveAsyncOptions()
            .setMaxAutoLockRenewalDuration(Duration.ofMinutes(2))
            .setIsAutoCompleteEnabled(true);

        setSenderAndReceiver(MessagingEntityType.SUBSCRIPTION, Duration.ofSeconds(20),
            builder -> builder.maxConcurrentSessions(maxConcurrency));

        final Disposable subscription = Flux.interval(Duration.ofMillis(500))
            .take(maxMessages)
            .flatMap(index -> {
                final int i = (int) (index % sessionIds.size());
                final String id = sessionIds.get(i);
                final ServiceBusMessage message = getServiceBusMessage(contents, messageId)
                    .setSessionId(id);
                return sender.send(message).thenReturn(
                    String.format("sessionId[%s] sent[%s] Message sent.", id, index));
            }).subscribe(
                message -> logger.info(message),
                error -> logger.error("Error encountered.", error),
                () -> logger.info("Finished sending."));

        // Act & Assert
        try {
            StepVerifier.create(receiver.receive(receiveOptions))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency, messageId, contents, context))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency, messageId, contents, context))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency, messageId, contents, context))

                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency, messageId, contents, context))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency, messageId, contents, context))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency, messageId, contents, context))

                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency + 1, messageId, contents, context))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency + 1, messageId, contents, context))
                .assertNext(context -> assertFromSession(sessionIds, set, maxConcurrency + 1, messageId, contents, context))
                .thenCancel()
                .verify(Duration.ofMinutes(2));
        } finally {
            subscription.dispose();
        }
    }

    private void assertFromSession(List<String> sessionIds, Set<String> currentSessions, int maxSize,
        String messageId, String contents, ServiceBusReceivedMessageContext context) {
        logger.info("Verifying message: {}", context.getSessionId());

        assertNotNull(context.getSessionId());
        assertTrue(sessionIds.contains(context.getSessionId()));

        if (currentSessions.add(context.getSessionId())) {
            logger.info("Adding sessionId: {}", context.getSessionId());
        }

        assertTrue(currentSessions.size() <= maxSize, String.format(
            "Current size (%s) is larger than max (%s).", currentSessions.size(), maxSize));
        assertMessageEquals(null, messageId, contents, context);
    }

    /**
     * Sets the sender and receiver. If session is enabled, then a single-named session receiver is created.
     */
    private void setSenderAndReceiver(MessagingEntityType entityType, Duration operationTimeout,
        Function<ServiceBusSessionReceiverClientBuilder, ServiceBusSessionReceiverClientBuilder> onBuild) {

        this.sender = getSenderBuilder(false, entityType, true).buildAsyncClient();
        this.receiver = getSessionReceiverBuilder(false, entityType,
            builder -> builder.retryOptions(new AmqpRetryOptions().setTryTimeout(operationTimeout)),
            builder -> onBuild.apply(builder))
            .buildAsyncClient();
    }

    private static void assertMessageEquals(String sessionId, String messageId, String contents,
        ServiceBusReceivedMessageContext actual) {
        ServiceBusReceivedMessage message = actual.getMessage();

        assertNotNull(message, "'message' should not be null. Error? " + actual.getThrowable());

        if (!CoreUtils.isNullOrEmpty(sessionId)) {
            assertEquals(sessionId, message.getSessionId());
        }

        assertEquals(messageId, message.getMessageId());
        assertEquals(contents, new String(message.getBody(), StandardCharsets.UTF_8));

        assertNull(actual.getThrowable());
    }
}
