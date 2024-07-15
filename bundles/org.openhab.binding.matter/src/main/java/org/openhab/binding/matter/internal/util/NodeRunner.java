/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.openhab.binding.matter.internal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham - Initial contribution
 */
public class NodeRunner {
    private static final Logger logger = LoggerFactory.getLogger(NodeRunner.class);

    private final String nodePath;
    private Process nodeProcess;
    private final List<NodeExitListener> exitListeners = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public NodeRunner(String nodePath) {
        this.nodePath = nodePath;
    }

    public interface NodeExitListener {
        void onNodeExit(int exitCode);
    }

    public void addExitListener(NodeExitListener listener) {
        exitListeners.add(listener);
    }

    public void removeExitListener(NodeExitListener listener) {
        exitListeners.remove(listener);
    }

    public int runNodeWithResource(String resourcePath, String... additionalArgs) throws IOException {
        Path scriptPath = extractResourceToTempFile(resourcePath);

        int port = findAvailablePort();
        List<String> command = new ArrayList<>();
        command.add(nodePath);
        command.add(scriptPath.toString());
        command.add("--host");
        command.add("localhost");
        command.add("--port");
        command.add(String.valueOf(port));
        command.addAll(List.of(additionalArgs));

        ProcessBuilder pb = new ProcessBuilder(command);
        nodeProcess = pb.start();

        // Start output and error stream readers
        executorService.submit(this::readOutputStream);
        executorService.submit(this::readErrorStream);

        // Wait for the process to exit in a separate thread
        executorService.submit(() -> {
            try {
                int exitCode = nodeProcess.waitFor();
                logger.debug("Node process exited with code: {}", exitCode);
                notifyExitListeners(exitCode);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.debug("Interrupted while waiting for Node process to exit", e);
            } finally {
                try {
                    Files.deleteIfExists(scriptPath);
                } catch (IOException e) {
                    logger.debug("Failed to delete temporary script file", e);
                }
            }
        });
        return port;
    }

    public void runNode(String... args) throws IOException {
        List<String> command = new ArrayList<>();
        command.add(nodePath);
        command.addAll(List.of(args));

        ProcessBuilder pb = new ProcessBuilder(command);
        nodeProcess = pb.start();

        executorService.submit(this::readOutputStream);
        executorService.submit(this::readErrorStream);

        // Wait for the process to exit in a separate thread
        executorService.submit(() -> {
            try {
                int exitCode = nodeProcess.waitFor();
                logger.debug("Node process exited with code: {}", exitCode);
                notifyExitListeners(exitCode);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.debug("Interrupted while waiting for Node process to exit", e);
            }
        });
    }

    private void readOutputStream() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(nodeProcess.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug(line);
            }
        } catch (IOException e) {
            logger.debug("Error reading Node process output", e);
        }
    }

    private void readErrorStream() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(nodeProcess.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug(line);
            }
        } catch (IOException e) {
            logger.debug("Error reading Node process error stream", e);
        }
    }

    private void notifyExitListeners(int exitCode) {
        for (NodeExitListener listener : exitListeners) {
            listener.onNodeExit(exitCode);
        }
    }

    public void stopNode() {
        if (nodeProcess != null && nodeProcess.isAlive()) {
            nodeProcess.destroy();
            try {
                // Wait for the process to terminate
                if (!nodeProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    // Force terminate if it doesn't exit within 5 seconds
                    nodeProcess.destroyForcibly();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.debug("Interrupted while waiting for Node process to stop", e);
            }
        }
        executorService.shutdownNow();
    }

    private Path extractResourceToTempFile(String resourcePath) throws IOException {
        Path tempFile = Files.createTempFile("node-script-", ".js");
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        tempFile.toFile().deleteOnExit(); // Ensure the temp file is deleted on JVM exit
        return tempFile;
    }

    private int findAvailablePort() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.debug("Failed to close ServerSocket", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            NodeManager nodeManager = new NodeManager();
            String nodePath = nodeManager.getNodePath();

            NodeRunner nodeRunner = new NodeRunner(nodePath);
            nodeRunner.addExitListener(exitCode -> logger.info("Node.js process exited with code: {}", exitCode));

            nodeRunner.runNode("src/main/resources/matter.js");

            // Wait for a bit to allow the process to complete
            Thread.sleep(10000);

            nodeRunner.stopNode();
        } catch (IOException e) {
            logger.error("Error running Node.js", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting", e);
        }
    }
}
