/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

/**
 *
 * @author Dan Cunningham
 *
 */
public class NodeJsDownloader {

    // public static void main(String[] args) {
    // try {
    // String os = System.getProperty("os.name").toLowerCase();
    // String arch = System.getProperty("os.arch").toLowerCase();
    // String nodeJsBinaryURL = getNodeJsBinaryURL(os, arch);
    //
    // Path archivePath = downloadNodeJsBinary(nodeJsBinaryURL);
    // Path nodeJsPath = extractNodeExecutable(archivePath, os);
    //
    // if (!os.contains("win")) {
    // setExecutablePermission(nodeJsPath);
    // }
    //
    // runJavaScriptFile(nodeJsPath, "path/to/your/script.js");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // private static String getNodeJsBinaryURL(String os, String arch) {
    // String extension = os.contains("win") ? "7z" : "xz";
    // // Construct the URL based on OS and architecture
    // return "https://nodejs.org/dist/v.../node-v...-" + os + "-" + arch + "." + extension;
    // }
    //
    // private static Path downloadNodeJsBinary(String urlString) throws IOException {
    // URL url = new URL(urlString);
    // Path tempDir = Files.createTempDirectory("nodejs");
    // Path archivePath = tempDir.resolve("nodejs-archive");
    //
    // try (InputStream in = url.openStream()) {
    // Files.copy(in, archivePath, StandardCopyOption.REPLACE_EXISTING);
    // }
    //
    // return archivePath;
    // }
    //
    // private static Path extractNodeExecutable(Path archivePath, String os) throws IOException {
    // Path tempDir = Files.createTempDirectory("nodejs-extract");
    // Path executablePath = tempDir.resolve("node");
    //
    // if (os.contains("win")) {
    // // Extract from 7z
    // try (SevenZFile sevenZFile = new SevenZFile(archivePath.toFile())) {
    // SevenZArchiveEntry entry;
    // while ((entry = sevenZFile.getNextEntry()) != null) {
    // if (entry.getName().endsWith("node.exe")) {
    // Files.copy(sevenZFile.getInputStream(entry), executablePath,
    // StandardCopyOption.REPLACE_EXISTING);
    // break;
    // }
    // }
    // }
    // } else {
    // // Extract from xz
    // try (InputStream fin = Files.newInputStream(archivePath);
    // BufferedInputStream in = new BufferedInputStream(fin);
    // XZInputStream xzIn = new XZInputStream(in);
    // OutputStream fout = Files.newOutputStream(executablePath)) {
    // byte[] buffer = new byte[8192];
    // int n;
    // while ((n = xzIn.read(buffer)) != -1) {
    // fout.write(buffer, 0, n);
    // }
    // }
    // }
    //
    // return executablePath;
    // }
    //
    // private static void setExecutablePermission(Path path) throws IOException {
    // Set<PosixFilePermission> perms = new HashSet<>();
    // perms.add(PosixFilePermission.OWNER_EXECUTE);
    // Files.setPosixFilePermissions(path, perms);
    // }
    //
    // private static void runJavaScriptFile(Path nodeJsPath, String jsFilePath) throws IOException {
    // ProcessBuilder processBuilder = new ProcessBuilder(nodeJsPath.toString(), jsFilePath);
    // Process process = processBuilder.start();
    //
    // try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
    // String line;
    // while ((line = reader.readLine()) != null) {
    // System.out.println(line);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
}
