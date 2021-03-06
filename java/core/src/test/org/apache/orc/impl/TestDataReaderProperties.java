/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.orc.impl;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.orc.CompressionKind;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class TestDataReaderProperties {

  private Supplier<FileSystem> mockedSupplier = mock(Supplier.class);
  private Path mockedPath = mock(Path.class);
  private boolean mockedZeroCopy = false;

  @Test
  public void testCompleteBuild() throws IOException {
    DataReaderProperties properties = DataReaderProperties.builder()
      .withFileSystemSupplier(mockedSupplier)
      .withPath(mockedPath)
      .withCompression(CompressionKind.ZLIB)
      .withZeroCopy(mockedZeroCopy)
      .build();
    assertEquals(mockedSupplier, properties.getFileSystemSupplier());
    assertEquals(mockedPath, properties.getPath());
    assertEquals(CompressionKind.ZLIB, properties.getCompression());
    assertEquals(mockedZeroCopy, properties.getZeroCopy());
  }

  @Test
  public void testFileSystemSupplier() throws IOException {

    DataReaderProperties properties = DataReaderProperties.builder()
        .withFileSystemSupplier(mockedSupplier)
        .withPath(mockedPath)
        .build();

    assertEquals(mockedSupplier, properties.getFileSystemSupplier());
  }

  @Test
  public void testWhenFilesystemIsProvidedGetFileSystemSupplierReturnsSupplier() throws IOException {
    DataReaderProperties properties = DataReaderProperties.builder()
        .withFileSystemSupplier(mockedSupplier)
        .withPath(mockedPath)
        .build();

    Supplier<FileSystem> supplierFromProperties = properties.getFileSystemSupplier();
    assertEquals(mockedSupplier, supplierFromProperties);
  }

  @Test
  public void testMissingNonRequiredArgs() throws IOException {
    DataReaderProperties properties = DataReaderProperties.builder()
      .withFileSystemSupplier(mockedSupplier)
      .withPath(mockedPath)
      .build();
    assertEquals(mockedSupplier, properties.getFileSystemSupplier());
    assertEquals(mockedPath, properties.getPath());
    assertNull(properties.getCompression());
    assertFalse(properties.getZeroCopy());
  }

  @Test(expected = java.lang.NullPointerException.class)
  public void testEmptyBuild() {
    DataReaderProperties.builder().build();
  }

  @Test(expected = java.lang.NullPointerException.class)
  public void testMissingPath() {
    DataReaderProperties.builder()
      .withFileSystemSupplier(mockedSupplier)
      .withCompression(CompressionKind.NONE)
      .withZeroCopy(mockedZeroCopy)
      .build();
  }

  @Test(expected = java.lang.NullPointerException.class)
  public void testMissingFileSystem() {
    DataReaderProperties.builder()
      .withPath(mockedPath)
      .withCompression(CompressionKind.NONE)
      .withZeroCopy(mockedZeroCopy)
      .build();
  }

}
