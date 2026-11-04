# HydroNudge third-party notices

Audited 2026-11-02 against the resolved releaseRuntimeClasspath (95 binary runtime artifacts). PASS — all runtime artifact license families resolved using published POMs, including Guava's parent POM; embedded license texts retained below. This is a conservative runtime inventory: R8 may remove unused code. Compiler, Gradle and test-only tools are not shipped dependencies. No new SDK was added.

The app uses AndroidX/Android Open Source Project libraries and the Kotlin, Kotlinx, Okio, Guava, JSpecify and JetBrains annotations projects. Their authors retain their rights. The license text and upstream notices below are supplied with the app under Settings > About HydroNudge > Third-party licenses. Libraries have not been forked or edited here; ordinary compilation, resource shrinking and R8 processing are applied.

## Version-specific runtime inventory

The POM link is authoritative publication metadata for the exact resolved binary. BSD entries are repackaged protobuf artifacts; their wrapper versions below are verified, not an assertion of an independently resolved upstream protobuf version. Guava ListenableFuture 1.0 inherits Apache-2.0 from [guava-parent 26.0-android](https://repo.maven.apache.org/maven2/com/google/guava/guava-parent/26.0-android/guava-parent-26.0-android.pom).

| Artifact | Version | License | Published POM |
| --- | --- | --- | --- |
| androidx.activity:activity | 1.13.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/activity/activity/1.13.0/activity-1.13.0.pom) |
| androidx.activity:activity-compose | 1.13.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/activity/activity-compose/1.13.0/activity-compose-1.13.0.pom) |
| androidx.activity:activity-ktx | 1.13.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/activity/activity-ktx/1.13.0/activity-ktx-1.13.0.pom) |
| androidx.annotation:annotation-experimental | 1.5.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/annotation/annotation-experimental/1.5.0/annotation-experimental-1.5.0.pom) |
| androidx.annotation:annotation-jvm | 1.10.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/annotation/annotation-jvm/1.10.0/annotation-jvm-1.10.0.pom) |
| androidx.arch.core:core-common | 2.2.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/arch/core/core-common/2.2.0/core-common-2.2.0.pom) |
| androidx.arch.core:core-runtime | 2.2.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/arch/core/core-runtime/2.2.0/core-runtime-2.2.0.pom) |
| androidx.autofill:autofill | 1.0.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/autofill/autofill/1.0.0/autofill-1.0.0.pom) |
| androidx.collection:collection-jvm | 1.6.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/collection/collection-jvm/1.6.0/collection-jvm-1.6.0.pom) |
| androidx.collection:collection-ktx | 1.6.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/collection/collection-ktx/1.6.0/collection-ktx-1.6.0.pom) |
| androidx.compose.animation:animation-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/animation/animation-android/1.12.1/animation-android-1.12.1.pom) |
| androidx.compose.animation:animation-core-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/animation/animation-core-android/1.12.1/animation-core-android-1.12.1.pom) |
| androidx.compose.foundation:foundation-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/foundation/foundation-android/1.12.1/foundation-android-1.12.1.pom) |
| androidx.compose.foundation:foundation-layout-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/foundation/foundation-layout-android/1.12.1/foundation-layout-android-1.12.1.pom) |
| androidx.compose.material:material-ripple-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/material/material-ripple-android/1.12.1/material-ripple-android-1.12.1.pom) |
| androidx.compose.material3:material3-android | 1.4.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/material3/material3-android/1.4.0/material3-android-1.4.0.pom) |
| androidx.compose.runtime:runtime-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/runtime/runtime-android/1.12.1/runtime-android-1.12.1.pom) |
| androidx.compose.runtime:runtime-annotation-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/runtime/runtime-annotation-android/1.12.1/runtime-annotation-android-1.12.1.pom) |
| androidx.compose.runtime:runtime-retain-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/runtime/runtime-retain-android/1.12.1/runtime-retain-android-1.12.1.pom) |
| androidx.compose.runtime:runtime-saveable-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/runtime/runtime-saveable-android/1.12.1/runtime-saveable-android-1.12.1.pom) |
| androidx.compose.ui:ui-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-android/1.12.1/ui-android-1.12.1.pom) |
| androidx.compose.ui:ui-geometry-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-geometry-android/1.12.1/ui-geometry-android-1.12.1.pom) |
| androidx.compose.ui:ui-graphics-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-graphics-android/1.12.1/ui-graphics-android-1.12.1.pom) |
| androidx.compose.ui:ui-text-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-text-android/1.12.1/ui-text-android-1.12.1.pom) |
| androidx.compose.ui:ui-tooling-preview-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-tooling-preview-android/1.12.1/ui-tooling-preview-android-1.12.1.pom) |
| androidx.compose.ui:ui-unit-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-unit-android/1.12.1/ui-unit-android-1.12.1.pom) |
| androidx.compose.ui:ui-util-android | 1.12.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-util-android/1.12.1/ui-util-android-1.12.1.pom) |
| androidx.concurrent:concurrent-futures | 1.1.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/concurrent/concurrent-futures/1.1.0/concurrent-futures-1.1.0.pom) |
| androidx.concurrent:concurrent-futures-ktx | 1.1.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/concurrent/concurrent-futures-ktx/1.1.0/concurrent-futures-ktx-1.1.0.pom) |
| androidx.core:core | 1.19.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/core/core/1.19.1/core-1.19.1.pom) |
| androidx.core:core | 1.19.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/core/core/1.19.1/core-1.19.1.pom) |
| androidx.core:core-ktx | 1.19.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/core/core-ktx/1.19.1/core-ktx-1.19.1.pom) |
| androidx.core:core-remoteviews | 1.1.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/core/core-remoteviews/1.1.0/core-remoteviews-1.1.0.pom) |
| androidx.core:core-viewtree | 1.0.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/core/core-viewtree/1.0.0/core-viewtree-1.0.0.pom) |
| androidx.customview:customview-poolingcontainer | 1.0.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/customview/customview-poolingcontainer/1.0.0/customview-poolingcontainer-1.0.0.pom) |
| androidx.datastore:datastore-android | 1.3.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-android/1.3.0/datastore-android-1.3.0.pom) |
| androidx.datastore:datastore-core-android | 1.3.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-core-android/1.3.0/datastore-core-android-1.3.0.pom) |
| androidx.datastore:datastore-core-okio-jvm | 1.3.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-core-okio-jvm/1.3.0/datastore-core-okio-jvm-1.3.0.pom) |
| androidx.datastore:datastore-preferences-android | 1.3.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-preferences-android/1.3.0/datastore-preferences-android-1.3.0.pom) |
| androidx.datastore:datastore-preferences-core-android | 1.3.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-preferences-core-android/1.3.0/datastore-preferences-core-android-1.3.0.pom) |
| androidx.datastore:datastore-preferences-external-protobuf | 1.3.0 | BSD-3-Clause | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-preferences-external-protobuf/1.3.0/datastore-preferences-external-protobuf-1.3.0.pom) |
| androidx.datastore:datastore-preferences-proto | 1.3.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/datastore/datastore-preferences-proto/1.3.0/datastore-preferences-proto-1.3.0.pom) |
| androidx.emoji2:emoji2 | 1.4.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/emoji2/emoji2/1.4.0/emoji2-1.4.0.pom) |
| androidx.glance:glance | 1.2.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/glance/glance/1.2.0/glance-1.2.0.pom) |
| androidx.glance:glance-appwidget | 1.2.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/glance/glance-appwidget/1.2.0/glance-appwidget-1.2.0.pom) |
| androidx.glance:glance-appwidget-external-protobuf | 1.2.0 | BSD-3-Clause | [POM](https://dl.google.com/dl/android/maven2/androidx/glance/glance-appwidget-external-protobuf/1.2.0/glance-appwidget-external-protobuf-1.2.0.pom) |
| androidx.glance:glance-appwidget-proto | 1.2.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/glance/glance-appwidget-proto/1.2.0/glance-appwidget-proto-1.2.0.pom) |
| androidx.glance:glance-material3 | 1.2.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/glance/glance-material3/1.2.0/glance-material3-1.2.0.pom) |
| androidx.graphics:graphics-path | 1.0.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/graphics/graphics-path/1.0.1/graphics-path-1.0.1.pom) |
| androidx.interpolator:interpolator | 1.0.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/interpolator/interpolator/1.0.0/interpolator-1.0.0.pom) |
| androidx.lifecycle:lifecycle-common-java8 | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-common-java8/2.11.0/lifecycle-common-java8-2.11.0.pom) |
| androidx.lifecycle:lifecycle-common-jvm | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-common-jvm/2.11.0/lifecycle-common-jvm-2.11.0.pom) |
| androidx.lifecycle:lifecycle-livedata | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-livedata/2.11.0/lifecycle-livedata-2.11.0.pom) |
| androidx.lifecycle:lifecycle-livedata-core | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-livedata-core/2.11.0/lifecycle-livedata-core-2.11.0.pom) |
| androidx.lifecycle:lifecycle-livedata-core-ktx | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-livedata-core-ktx/2.11.0/lifecycle-livedata-core-ktx-2.11.0.pom) |
| androidx.lifecycle:lifecycle-process | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-process/2.11.0/lifecycle-process-2.11.0.pom) |
| androidx.lifecycle:lifecycle-runtime-android | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-runtime-android/2.11.0/lifecycle-runtime-android-2.11.0.pom) |
| androidx.lifecycle:lifecycle-runtime-compose-android | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-runtime-compose-android/2.11.0/lifecycle-runtime-compose-android-2.11.0.pom) |
| androidx.lifecycle:lifecycle-runtime-ktx-android | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-runtime-ktx-android/2.11.0/lifecycle-runtime-ktx-android-2.11.0.pom) |
| androidx.lifecycle:lifecycle-service | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-service/2.11.0/lifecycle-service-2.11.0.pom) |
| androidx.lifecycle:lifecycle-viewmodel-android | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-viewmodel-android/2.11.0/lifecycle-viewmodel-android-2.11.0.pom) |
| androidx.lifecycle:lifecycle-viewmodel-compose-android | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-viewmodel-compose-android/2.11.0/lifecycle-viewmodel-compose-android-2.11.0.pom) |
| androidx.lifecycle:lifecycle-viewmodel-ktx | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-viewmodel-ktx/2.11.0/lifecycle-viewmodel-ktx-2.11.0.pom) |
| androidx.lifecycle:lifecycle-viewmodel-savedstate-android | 2.11.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/lifecycle/lifecycle-viewmodel-savedstate-android/2.11.0/lifecycle-viewmodel-savedstate-android-2.11.0.pom) |
| androidx.navigation:navigation-common-android | 2.10.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/navigation/navigation-common-android/2.10.2/navigation-common-android-2.10.2.pom) |
| androidx.navigation:navigation-compose-android | 2.10.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/navigation/navigation-compose-android/2.10.2/navigation-compose-android-2.10.2.pom) |
| androidx.navigation:navigation-runtime-android | 2.10.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/navigation/navigation-runtime-android/2.10.2/navigation-runtime-android-2.10.2.pom) |
| androidx.navigationevent:navigationevent-android | 1.1.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/navigationevent/navigationevent-android/1.1.2/navigationevent-android-1.1.2.pom) |
| androidx.navigationevent:navigationevent-compose-android | 1.1.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/navigationevent/navigationevent-compose-android/1.1.2/navigationevent-compose-android-1.1.2.pom) |
| androidx.profileinstaller:profileinstaller | 1.4.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/profileinstaller/profileinstaller/1.4.0/profileinstaller-1.4.0.pom) |
| androidx.room:room-common-jvm | 2.8.5 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/room/room-common-jvm/2.8.5/room-common-jvm-2.8.5.pom) |
| androidx.room:room-ktx | 2.8.5 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/room/room-ktx/2.8.5/room-ktx-2.8.5.pom) |
| androidx.room:room-runtime-android | 2.8.5 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/room/room-runtime-android/2.8.5/room-runtime-android-2.8.5.pom) |
| androidx.savedstate:savedstate-android | 1.5.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/savedstate/savedstate-android/1.5.0/savedstate-android-1.5.0.pom) |
| androidx.savedstate:savedstate-compose-android | 1.5.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/savedstate/savedstate-compose-android/1.5.0/savedstate-compose-android-1.5.0.pom) |
| androidx.savedstate:savedstate-ktx | 1.5.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/savedstate/savedstate-ktx/1.5.0/savedstate-ktx-1.5.0.pom) |
| androidx.sqlite:sqlite-android | 2.6.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/sqlite/sqlite-android/2.6.2/sqlite-android-2.6.2.pom) |
| androidx.sqlite:sqlite-framework-android | 2.6.2 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/sqlite/sqlite-framework-android/2.6.2/sqlite-framework-android-2.6.2.pom) |
| androidx.startup:startup-runtime | 1.1.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/startup/startup-runtime/1.1.1/startup-runtime-1.1.1.pom) |
| androidx.tracing:tracing-android | 2.0.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/tracing/tracing-android/2.0.1/tracing-android-2.0.1.pom) |
| androidx.tracing:tracing-ktx | 2.0.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/tracing/tracing-ktx/2.0.1/tracing-ktx-2.0.1.pom) |
| androidx.versionedparcelable:versionedparcelable | 1.1.1 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/versionedparcelable/versionedparcelable/1.1.1/versionedparcelable-1.1.1.pom) |
| androidx.window:window | 1.5.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/window/window/1.5.0/window-1.5.0.pom) |
| androidx.window:window-core-android | 1.5.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/window/window-core-android/1.5.0/window-core-android-1.5.0.pom) |
| androidx.work:work-runtime | 2.12.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/work/work-runtime/2.12.0/work-runtime-2.12.0.pom) |
| androidx.work:work-runtime-ktx | 2.12.0 | Apache-2.0 | [POM](https://dl.google.com/dl/android/maven2/androidx/work/work-runtime-ktx/2.12.0/work-runtime-ktx-2.12.0.pom) |
| com.google.guava:listenablefuture | 1.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/com/google/guava/listenablefuture/1.0/listenablefuture-1.0.pom) |
| com.squareup.okio:okio-jvm | 3.17.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/com/squareup/okio/okio-jvm/3.17.0/okio-jvm-3.17.0.pom) |
| org.jetbrains:annotations | 23.0.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jetbrains/annotations/23.0.0/annotations-23.0.0.pom) |
| org.jetbrains.kotlin:kotlin-stdlib | 2.4.21 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-stdlib/2.4.21/kotlin-stdlib-2.4.21.pom) |
| org.jetbrains.kotlinx:atomicfu-jvm | 0.28.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/atomicfu-jvm/0.28.0/atomicfu-jvm-0.28.0.pom) |
| org.jetbrains.kotlinx:kotlinx-coroutines-android | 1.9.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/kotlinx-coroutines-android/1.9.0/kotlinx-coroutines-android-1.9.0.pom) |
| org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm | 1.9.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/kotlinx-coroutines-core-jvm/1.9.0/kotlinx-coroutines-core-jvm-1.9.0.pom) |
| org.jetbrains.kotlinx:kotlinx-serialization-core-jvm | 1.7.3 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/kotlinx-serialization-core-jvm/1.7.3/kotlinx-serialization-core-jvm-1.7.3.pom) |
| org.jspecify:jspecify | 1.0.0 | Apache-2.0 | [POM](https://repo.maven.apache.org/maven2/org/jspecify/jspecify/1.0.0/jspecify-1.0.0.pom) |

## Bundled native libraries

| Library | Origin | ABIs | License |
| --- | --- | --- | --- |
| libandroidx.graphics.path.so | androidx.graphics:graphics-path:1.0.1 | arm64-v8a, armeabi-v7a, x86, x86_64 | Apache-2.0 |
| libdatastore_shared_counter.so | androidx.datastore:datastore-core-android:1.3.0 | arm64-v8a, armeabi-v7a, x86, x86_64 | Apache-2.0 |

These are the only native library names found in the runtime archives. Their ELF alignment evidence is in `qa-reports/release-preparation/validated-native-alignment.json`; final APK ZIP alignment evidence is in `qa-reports/closed-test/zipalign.txt`. System libraries/fonts are supplied by Android, not newly bundled assets.

## License texts and notices

The following full license texts come from the distributed artifacts or versioned upstream sources. Exact embedded license material is retained even where it repeats a shared license. Kotlin JVM source contains ThreeTen-derived time code and GWT-derived collections; their notices are included. Kotlin compiler, Native and Wasm-only third-party code is not attributed as shipped Android JVM runtime code.

### Embedded license text 1

Applies to: androidx.glance:glance-appwidget-external-protobuf:1.2.0, androidx.datastore:datastore-preferences-external-protobuf:1.3.0.

```text
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of the copyright holder nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

### Embedded license text 2

Applies to: androidx.glance:glance-appwidget-proto:1.2.0, androidx.glance:glance:1.2.0, androidx.glance:glance-material3:1.2.0, androidx.glance:glance-appwidget:1.2.0, androidx.work:work-runtime-ktx:2.12.0, androidx.work:work-runtime:2.12.0, androidx.room:room-common-jvm:2.8.5, androidx.room:room-runtime-android:2.8.5, androidx.room:room-ktx:2.8.5, androidx.navigation:navigation-runtime-android:2.10.2, androidx.navigation:navigation-common-android:2.10.2, androidx.navigation:navigation-compose-android:2.10.2, androidx.compose.material3:material3-android:1.4.0, androidx.compose.animation:animation-core-android:1.12.1, androidx.compose.material:material-ripple-android:1.12.1, androidx.compose.animation:animation-android:1.12.1, androidx.compose.foundation:foundation-layout-android:1.12.1, androidx.compose.foundation:foundation-android:1.12.1, androidx.compose.ui:ui-geometry-android:1.12.1, androidx.compose.ui:ui-util-android:1.12.1, androidx.compose.ui:ui-unit-android:1.12.1, androidx.compose.ui:ui-tooling-preview-android:1.12.1, androidx.compose.ui:ui-graphics-android:1.12.1, androidx.compose.ui:ui-text-android:1.12.1, androidx.lifecycle:lifecycle-process:2.11.0, androidx.lifecycle:lifecycle-viewmodel-ktx:2.11.0, androidx.lifecycle:lifecycle-common-java8:2.11.0, androidx.lifecycle:lifecycle-livedata-core-ktx:2.11.0, androidx.lifecycle:lifecycle-livedata-core:2.11.0, androidx.lifecycle:lifecycle-runtime-android:2.11.0, androidx.lifecycle:lifecycle-viewmodel-android:2.11.0, androidx.lifecycle:lifecycle-viewmodel-savedstate-android:2.11.0, androidx.navigationevent:navigationevent-android:1.1.2, androidx.navigationevent:navigationevent-compose-android:1.1.2, androidx.compose.runtime:runtime-retain-android:1.12.1, androidx.compose.runtime:runtime-annotation-android:1.12.1, androidx.compose.runtime:runtime-android:1.12.1, androidx.compose.runtime:runtime-saveable-android:1.12.1, androidx.savedstate:savedstate-compose-android:1.5.0, androidx.savedstate:savedstate-ktx:1.5.0, androidx.savedstate:savedstate-android:1.5.0, androidx.core:core-ktx:1.19.1, androidx.window:window-core-android:1.5.0, androidx.window:window:1.5.0, androidx.core:core:1.19.1, androidx.lifecycle:lifecycle-common-jvm:2.11.0, androidx.lifecycle:lifecycle-service:2.11.0, androidx.lifecycle:lifecycle-livedata:2.11.0, androidx.lifecycle:lifecycle-runtime-ktx-android:2.11.0, androidx.lifecycle:lifecycle-runtime-compose-android:2.11.0, androidx.lifecycle:lifecycle-viewmodel-compose-android:2.11.0, androidx.compose.ui:ui-android:1.12.1, androidx.activity:activity-ktx:1.13.0, androidx.activity:activity:1.13.0, androidx.activity:activity-compose:1.13.0, androidx.datastore:datastore-core-okio-jvm:1.3.0, androidx.datastore:datastore-preferences-proto:1.3.0, androidx.datastore:datastore-core-android:1.3.0, androidx.datastore:datastore-preferences-core-android:1.3.0, androidx.datastore:datastore-android:1.3.0, androidx.datastore:datastore-preferences-android:1.3.0, androidx.annotation:annotation-experimental:1.5.0, androidx.profileinstaller:profileinstaller:1.4.0, androidx.tracing:tracing-ktx:2.0.1, androidx.tracing:tracing-android:2.0.1, androidx.core:core-viewtree:1.0.0, androidx.sqlite:sqlite-framework-android:2.6.2, androidx.sqlite:sqlite-android:2.6.2, androidx.collection:collection-ktx:1.6.0, androidx.collection:collection-jvm:1.6.0, androidx.annotation:annotation-jvm:1.10.0.

```text

                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS
```

### Kotlin coroutines upstream NOTICE

[Upstream license/notice source](https://github.com/Kotlin/kotlinx.coroutines/blob/1.9.0/license/NOTICE.txt).

```text
=========================================================================
==  NOTICE file corresponding to the section 4 d of                    ==
==  the Apache License, Version 2.0,                                   ==
==  in this case for the kotlinx.coroutines library.                   ==
=========================================================================

kotlinx.coroutines library.
Copyright 2016-2024 JetBrains s.r.o and contributors
```

### Kotlin serialization upstream NOTICE

[Upstream license/notice source](https://github.com/Kotlin/kotlinx.serialization/blob/v1.7.3/license/NOTICE.txt).

```text
   =========================================================================
   ==  NOTICE file corresponding to the section 4 d of                    ==
   ==  the Apache License, Version 2.0,                                   ==
   ==  in this case for the kotlix.serialization library.                 ==
   =========================================================================

   kotlinx.serialization library.
   Copyright 2017-2019 JetBrains s.r.o and respective authors and developers
```

### Kotlin ThreeTen-derived code

[Upstream license/notice source](https://github.com/JetBrains/kotlin/blob/v2.4.21/license/third_party/threetenbp_license.txt).

```text
Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice,
  this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of JSR-310 nor the names of its contributors
  may be used to endorse or promote products derived from this software
  without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

### Protocol Buffers upstream attribution

[Upstream license/notice source](https://github.com/protocolbuffers/protobuf/blob/v3.25.5/LICENSE). Supplemental upstream copyright notice; 3.25.5 identifies this license-text source, not the repackaged library version.

```text
Copyright 2008 Google Inc.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Code generated by the Protocol Buffer compiler is owned by the owner
of the input file used when generating it.  This code is not
standalone and requires a support library to be linked with it.  This
support library is itself covered by the above license.
```

### Additional retained source attribution

Kotlin standard-library source 2.4.21: Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors. GWT-derived AbstractList/AbstractMap: Copyright 2007 Google Inc. ThreeTen-derived Instant: Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos. See [Kotlin source and licensing](https://github.com/JetBrains/kotlin/tree/v2.4.21/license).

Okio: [Apache-2.0 license at parent-3.17.0](https://github.com/square/okio/blob/parent-3.17.0/LICENSE.txt). Guava: [Apache-2.0](https://github.com/google/guava/blob/v23.0/COPYING). A shared full Apache license is included above and applies to all Apache entries.

## Audit evidence and maintenance

Local evidence is in qa-reports/release-preparation/runtime-artifacts.json, dependencies.log, license-metadata.json, embedded-licenses.json, native-dependencies.json and license-sources/. These contain exact resolved files and package metadata; build inputs remain unchanged. Sources were read from the Gradle artifact cache and official Google Maven/Maven Central publications and upstream project repositories, not inferred from library names.

Before any dependency change, resolve releaseRuntimeClasspath again, inspect publication metadata (including parent POMs), embedded/repackaged licenses and any upstream notices. Refresh this file and the bundled third-party-notices.txt together, rebuild and open the in-app dialog. No runtime dependency has an unresolved primary license in this audit. This inventory does not grant rights to publisher branding or unrelated artwork.
