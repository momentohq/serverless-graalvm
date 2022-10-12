package com.momento.nativeimage;

import static com.momento.nativeimage.NativeImageUtils.registerClassForReflection;
import static com.momento.nativeimage.NativeImageUtils.registerClassHierarchyForReflection;
import static com.momento.nativeimage.NativeImageUtils.registerForReflectiveInstantiation;
import static com.momento.nativeimage.NativeImageUtils.registerForUnsafeFieldAccess;

import com.oracle.svm.core.annotate.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

/**
 * Configures Native Image settings for the grpc-netty-shaded dependency. Takes what was in feature here minus Auth specific.
 * https://github.com/GoogleCloudPlatform/native-image-support-java/blob/master/native-image-support/src/main/java/com/google/cloud/nativeimage/features/core/GrpcNettyFeature.java
 */
@AutomaticFeature
final class GrpcNettyFeature implements Feature {

  private static final String GRPC_NETTY_SHADED_CLASS =
      "io.grpc.netty.shaded.io.grpc.netty.NettyServer";

  private static final String GOOGLE_AUTH_CLASS =
      "com.google.auth.oauth2.ServiceAccountCredentials";

  @Override
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    loadGrpcNettyClasses(access);
    loadMiscClasses(access);
  }

  private static void loadGrpcNettyClasses(BeforeAnalysisAccess access) {
    // For io.grpc:grpc-netty-shaded
    Class<?> nettyShadedClass = access.findClassByName(GRPC_NETTY_SHADED_CLASS);
    if (nettyShadedClass != null) {
      // Misc. classes used by grpc-netty-shaded
      registerForReflectiveInstantiation(
          access, "io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel");
      registerClassForReflection(
          access, "io.grpc.netty.shaded.io.netty.util.internal.NativeLibraryUtil");
      registerClassForReflection(
          access, "io.grpc.netty.shaded.io.netty.util.ReferenceCountUtil");
      registerClassForReflection(
          access, "io.grpc.netty.shaded.io.netty.buffer.AbstractByteBufAllocator");

      // Epoll Libraries
      registerClassForReflection(
          access, "io.grpc.netty.shaded.io.netty.channel.epoll.Epoll");
      registerClassForReflection(
          access, "io.grpc.netty.shaded.io.netty.channel.epoll.EpollChannelOption");
      registerClassForReflection(
          access, "io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup");
      registerForReflectiveInstantiation(
          access, "io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerSocketChannel");
      registerForReflectiveInstantiation(
          access, "io.grpc.netty.shaded.io.netty.channel.epoll.EpollSocketChannel");

      // Unsafe field accesses
      registerForUnsafeFieldAccess(
          access,
          "io.grpc.netty.shaded.io.netty.util.internal.shaded."
              + "org.jctools.queues.MpscArrayQueueProducerIndexField",
          "producerIndex");
      registerForUnsafeFieldAccess(
          access,
          "io.grpc.netty.shaded.io.netty.util.internal.shaded."
              + "org.jctools.queues.MpscArrayQueueProducerLimitField",
          "producerLimit");
      registerForUnsafeFieldAccess(
          access,
          "io.grpc.netty.shaded.io.netty.util.internal.shaded."
              + "org.jctools.queues.MpscArrayQueueConsumerIndexField",
          "consumerIndex");
      registerForUnsafeFieldAccess(
          access,
          "io.grpc.netty.shaded.io.netty.util.internal.shaded."
              + "org.jctools.queues.BaseMpscLinkedArrayQueueProducerFields",
          "producerIndex");
      registerForUnsafeFieldAccess(
          access,
          "io.grpc.netty.shaded.io.netty.util.internal.shaded."
              + "org.jctools.queues.BaseMpscLinkedArrayQueueColdProducerFields",
          "producerLimit");
      registerForUnsafeFieldAccess(
          access,
          "io.grpc.netty.shaded.io.netty.util.internal.shaded."
              + "org.jctools.queues.BaseMpscLinkedArrayQueueConsumerFields",
          "consumerIndex");
    }
  }

  /**
   * Miscellaneous classes that need to be registered coming from various JARs.
   */
  private static void loadMiscClasses(BeforeAnalysisAccess access) {
    registerClassHierarchyForReflection(
        access, "com.google.protobuf.DescriptorProtos");
    registerClassForReflection(access, "com.google.api.FieldBehavior");

    registerForUnsafeFieldAccess(
        access, "javax.net.ssl.SSLContext", "contextSpi");
  }
}
