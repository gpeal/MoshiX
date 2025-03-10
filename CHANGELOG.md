Changelog
=========

Version 0.16.0
--------------

_2021-12-24_

#### **New:** [moshi-ir](https://github.com/ZacSweers/MoshiX/tree/main/moshi-ir)

An experimental Kotlin IR implementation of Moshi code gen and moshi-sealed code gen.

The goal of this is to have functional parity with their native Kapt/KSP code gen analogues but run as a fully
embedded IR plugin.

**Benefits**
- Significantly faster build times.
  - No extra Kapt or KSP tasks, no extra source files to compile. This runs directly in kotlinc and generates IR that is
    lowered directly into bytecode.
- No reflection required at runtime to support default parameter values.
- Feature parity with Moshi's native code gen.
- More detailed error messages for unexpected null values and missing properties. Now all errors are accumulated and
  reported at the end, rather than failing eagerly with just the first one encountered.
  - See https://github.com/square/moshi/issues/836 for more details!

**Cons**
- No support for Proguard file generation for now [#193](https://github.com/ZacSweers/MoshiX/issues/193). You will
  need to add this manually to your rules if you use R8/Proguard.
  - One option is to use IR in debug builds and Kapt/KSP in release builds, the latter of which do still generate
    proguard rules.
  ```proguard
  # Keep names for JsonClass-annotated classes
  -keepnames class @com.squareup.moshi.JsonClass **

  # Keep generated adapter classes' constructors
  -keepclassmembers class *JsonAdapter {
      public <init>(...);
  }
  ```
- Kotlin IR is not a stable API and may change in future Kotlin versions. While I'll try to publish quickly to adjust to
  these, you should be aware. If you have any issues, you can always fall back to Kapt/KSP.

### Installation

Simply apply the Gradle plugin in your project to use it. You can enable moshi-sealed code gen via the `moshi`
extension.

The Gradle plugin is published to Maven Central, so ensure you have `mavenCentral()` visible to your buildscript
classpath.

[![Maven Central](https://img.shields.io/maven-central/v/dev.zacsweers.moshix/moshi-gradle-plugin.svg)](https://mvnrepository.com/artifact/dev.zacsweers.moshix/moshi-gradle-plugin)
```gradle
plugins {
  kotlin("jvm")
  id("dev.zacsweers.moshix") version "x.y.z"
}

moshi {
  // Opt-in to enable moshi-sealed, disabled by default.
  enableSealed.set(true)
}
```

#### Other

- Update to Kotlin `1.6.10`
- Update to KSP `1.6.10-1.0.2`

Version 0.15.0
--------------

_2021-12-10_

* Update to Moshi `1.13.0`
* **Removed:** The `moshi-ksp` artifact has been upstreamed to Moshi itself as is no longer published.
* **Removed:** The `moshi-records-reflect` artifact has been upstreamed to Moshi itself as is no longer published.
* Update to Kotlin `1.6.0`
* Update to KotlinPoet `1.10.2`

Version 0.14.1
--------------

_2021-09-21_

* Build against JDK 17.
  * This means that `moshi-sealed-java-sealed-reflect`'s support of `sealed` classes in Java is now out of preview
    and requires Java 17 to use.
  * `moshi-records-reflect` still targets Java 16 for maximum compatibility.
  * All other artifacts still target Java 8.
* Update Kotlin to `1.5.31`
* Update KotlinPoet to `1.10.1`

Version 0.14.0
--------------

_2021-09-07_

* Update KSP to `1.5.30-1.0.0` stable!
* `moshi-sealed-ksp` has now been merged into `moshi-sealed-codegen`. This artifact can be used for both `kapt` and
  `ksp`.
* `moshi-ksp` is now _soft-deprecated_ and will be fully deprecated once Moshi's next release is out with formal support.

Version 0.13.0
--------------

_2021-08-27_

* Update Kotlin to `1.5.30`.
* Update KSP to `1.5.30-1.0.0-beta08`.
* **Enhancement:** `RecordsJsonAdapterFactory` is now aligned with the upstreamed implementation on Moshi itself.
  * Note that this is now _soft-deprecated_ and will be fully deprecated once Moshi's next release is out with formal support.
  * This includes using a few more modern language APIs like `MethodHandle` and better unpacking of different runtime exceptions. Full details can be found in the [PR](https://github.com/square/moshi/pull/1381).
* **Fix:** Avoid implicitly converting elements to KotlinPoet in `CodeBlock`s to avoid noisy logging.
* **Fix:** Improve self-referencing type variables parsing in `moshi-ksp` (see [#125](https://github.com/ZacSweers/MoshiX/pull/125) and [#151](https://github.com/ZacSweers/MoshiX/pull/151)).

Special thanks to [@yigit](https://github.com/yigit) for contributing to this release!

Version 0.12.2
--------------

_2021-08-20_

* **Fix:** `RecordsJsonAdapterFactory` now properly respects `@JsonQualifier` annotations on components.
* **Fix:** `RecordsJsonAdapterFactory` now supports non-public constructors (i.e. package or file-private).
* **Fix:** Crash in `moshi-ksp` when dealing with generic typealias properties.

Version 0.12.1
--------------

_2021-08-19_

* Update to KSP `1.5.21-1.0.0-beta07`.
* **Fix:** Previously if you had a `@JsonClass`-annotated Java file with a custom generator, `moshi-ksp` would error
  out anyway due to it not being a Kotlin class. This is now fixed and it will safely ignore these files.
* **Fix:** Generate missing `@OptIn(ExperimentalStdLibApi::class)` annotations in `moshi-sealed` when `object`
  adapters are used, as we use Moshi's reified `addAdapter` extension.

Thanks to [@gabrielittner](https://github.com/gabrielittner) for contributing to this release!

Version 0.12.0
--------------

_2021-07-15_

* Update to KSP `1.5.21-1.0.0-beta05`.
* Update to Kotlin `1.5.21`.
* Update to Dokka `1.5.0`.
* Update to KotlinPoet `1.9.0`.
* Test against JDK 17 early access previews.
* **New:** `moshi-ksp` and moshi-sealed's codegen both support a new `moshi.generateProguardRules` option. This can be
  set to `false` to disable proguard rule generation.
* **Fix:** Artifacts now ship with a `-module-name` attribute set to their artifact ID to help avoid module name
  collisions.

Thanks to [@SeongUgJung](https://github.com/SeongUgJung) and [@slmlt](https://github.com/slmlt) for contributing to this
release!

Version 0.11.2
--------------

_2021-05-31_

* `moshi-ksp` - Fix a bug where supertypes compiled outside the current compilation weren't recognized as Kotlin types.

Version 0.11.1
--------------

_2021-05-27_

* Update to KSP `1.5.10-1.0.0-beta01`
* Update to Kotlin `1.5.10`

Version 0.11.0
--------------

_2021-05-14_

#### Project-wide

* Update Kotlin to `1.5.0`.
* Update deprecated Kotlin stdlib usages during `1.5.0` upgrade.
* Support Java 16.
* Update KotlinPoet to `1.8.0`.
* Small documentation improvements.

#### All KSP artifacts

* Update KSP to `1.5.0-1.0.0-alpha10`.
* Switch to new `SymbolProcessorProvider` APIs.
* Adopt new `Sequence`-based KSP APIs where possible.

#### All metadata-reflect artifacts

* Update kotlinx-metadata to `0.3.0`.

#### moshi-ksp

* **Fix:** Don't fail on annotations that are `typealias`'d.
* **Fix:** Support enum entry values in copied `@JsonQualifier` annotations.
* **Fix:** Support array values in copied `@JsonQualifier` annotations.

#### moshi-sealed

* **Enhancement:** sealed interfaces and package-wide sealed classes are fully supported in KSP, kapt, reflect, and
  metadata-reflect.
* **Fix:** Make `moshi-adapters` an `api` dependency in `moshi-sealed-runtime`

#### moshi-records-reflect

* `RecordsJsonAdapterFactory` is no longer in preview and now built against JDK 16.
* **New:** A dedicated README page can be found [here](https://github.com/ZacSweers/MoshiX/tree/main/moshi-records-reflect).

```java
final record Message(String value) {
}

public static void main(String[] args) {
  Moshi moshi = new Moshi.Builder()
    .add(new RecordsJsonAdapterFactory())
    .build();

  JsonAdapter<Message> messageAdapter = moshi.adapter(Message.class);
}
```

#### moshi-sealed: java-sealed-reflect

* `JavaSealedJsonAdapterFactory` is now built against JDK 16. Note this feature is still in preview.
* **New:** A dedicated README section can be found [here](https://github.com/ZacSweers/MoshiX/tree/main/moshi-sealed#java-sealed-classes-support).

_Thanks to the following contributors for contributing to this release! [@remcomokveld](https://github.com/remcomokveld), [@martinbonnin](https://github.com/martinbonnin), and [@eneim](https://github.com/eneim)_

Version 0.10.0
-------------

_2021-04-09_

* Update KSP to `1.4.32-1.0.0-alpha07`.
* `moshi-ksp` - Report missing primary constructor JVM signatures to `KSPLogger`.
* Update Kotlin to `1.4.32`.
* Update Moshi to `1.12.0`.

Version 0.9.2
-------------

_2021-03-01_

#### KSP

* Update KSP to `1.4.30-1.0.0-alpha04` in KSP-using libraries. Among other changes, these processors now run all
  errors through KSP's native `KSPLogger.error()` API now.

#### moshi-ksp

* **Fix:** Support function types as property types.
* **Fix:** Support generic arrays when invoking defaults constructors.
* Some small readability improvements to generated code.

#### Moshi-sealed

* Add tests for Kotlin 1.4.30's preview support for sealed interfaces. These won't be officially supported until
  Kotlin 1.5, but they do appear to Just Work™️ since Kotlin reuses the same sealed APIs under the hood.
* Support Kotlin 1.5's upcoming sealed interfaces in KSP.

Version 0.9.1
-------------

_2021-02-15_

* Update to Kotlin `1.4.30`.

#### KSP

_Applies to all KSP-using artifacts._

* Update to KSP `1.4.30-1.0.0-alpha02`. Note that `incremental` is now _on_ by default.

#### moshi-ksp

* **Fix:** Reserve property type simple names eagerly to avoid collisions like https://github.com/square/moshi/issues/1277
* **Fix:** Include `"RedundantVisibilityModifier"` suppression in generated adapters to cover for KotlinPoet's
  explicit `public` modifiers.
* **Enhancement:** Invoke constructor directly in generated adapters if all parameters with defaults are present in
  the JSON. This allows generated adapters to avoid reflective lookup+invocation of the Kotlin synthetic defaults
  constructor that we otherwise have to use to support default parameter values.

#### moshi-sealed

_Changes apply to all moshi-sealed implementations (Java, reflect, KSP, code gen, etc) unless otherwise
specified._

* **New:** `moshi-sealed-metadata-reflect` artifact with a `kotlinx-metadata`-based implementation, allowing
  reflective use without `kotlin-reflect`.

  [![Maven Central](https://img.shields.io/maven-central/v/dev.zacsweers.moshix/moshi-sealed-metadata-reflect.svg)](https://mvnrepository.com/artifact/dev.zacsweers.moshix/moshi-sealed-metadata-reflect)
  ```gradle
  implementation "dev.zacsweers.moshix:moshi-sealed-metadata-reflect:{version}"
  ```

* **Fix:** Check for generic sealed subtypes. The base sealed type can be generic, but subtypes cannot since we
  can't plumb their generic information down to them when looking up from the base alone!
* **Fix:** Code gen and ksp now respect `JsonClass.generateAdapter`.
* **Fix:** KSP failing to find sealed subclasses when sealed base class is generic.
* **Fix:** Check for duplicate labels.
* **Fix:** KSP now routes all errors through `KSPLogger.error()`.
* **Fix:** Generate `@Suppress` annotations with suppressions for common warnings in generated code in both KSP and
  code gen.

#### moshi-adapters

* **New:** `@JsonString` can now be used on functions/methods, allowing use in more scenarios like AutoValue and
  Retrofit.

  ```kotlin
  interface TacoApi {
    @JsonString
    @GET("/")
    fun getTacosAsRawJsonString(): String
  }
  ```

* **New:** `@TrackUnknownKeys` annotation + factory to record unknown keys in a JSON body. See its doc for more
  information. This API should be treated as experimental (even by MoshiX standards), feedback welcome on how best
  to improve the API!

  ```kotlin
  val moshi = Moshi.Builder()
    .add(TrackUnknownKeys.Factory())
    .build()

  @TrackUnknownKeys
  @JsonClass(generateAdapter = true)
  data class Message(
    val data: String
  )

  // JSON of {"data": "value", "foo": "bar"} would report an unknown "foo"
  ```

#### moshi-metadata-reflect

* **Fix:** Embedded proguard rules now keep the right package for kotlinx-metadata extensions.

_Special thanks to [@efemoney](https://github.com/efemoney) and [@plnice](https://github.com/plnice) for
contributing to this release!_

Version 0.9.0
-------------

This version had a bug in releasing, please ignore.

Version 0.8.0
-------------------

_2021-01-27_

* **New:** Experimental support for Java `record` classes via new `moshi-records-reflect` artifact. See
`RecordsJsonAdapterFactory`. Requires JDK 15 + `--enable-preview`.
  ```java
  Moshi moshi = new Moshi.Builder()
      .add(new RecordsJsonAdapterFactory())
      .build();

  final record Message(String value) {
  }
  ```

* **New:** Experimental support for Java `sealed` classes and interfaces in moshi-sealed via new
  `moshi-sealed-java-sealed-reflect` artifact. See `JavaSealedJsonAdapterFactory`.  Requires JDK 15 + `--enable-preview`.
  ```java
  Moshi moshi = new Moshi.Builder()
      .add(new JavaSealedJsonAdapterFactory())
      .add(new RecordsJsonAdapterFactory())
      .build();

  @JsonClass(generateAdapter = true, generator = "sealed:type")
  sealed interface MessageInterface
      permits MessageInterface.Success, MessageInterface.Error {

    @TypeLabel(label = "success", alternateLabels = {"successful"})
    final record Success(String value) implements MessageInterface {
    }

    @TypeLabel(label = "error")
    final record Error(Map<String, Object> error_logs) implements MessageInterface {
    }
  }
  ```

* **New:** `@AdaptedBy` annotation support in `moshi-adapters`. This is analogous to Gson's `@JsonAdapter` annotation,
 allowing you to annotate a class or a property with it to indicate which `JsonAdapter` or `JsonAdapter.Factory`
 should be used to encode it.
 ```Kotlin
  val moshi = Moshi.Builder()
    .add(AdaptedBy.Factory())
    .build()

  @AdaptedBy(StringAliasAdapter::class)
  data class StringAlias(val value: String)

  class StringAliasAdapter : JsonAdapter<StringAlias>() {
    override fun fromJson(reader: JsonReader): StringAlias? {
      return StringAlias(reader.nextString())
    }

    override fun toJson(writer: JsonWriter, value: StringAlias?) {
      if (value == null) {
        writer.nullValue()
        return
      }
      writer.value(value.value)
    }
  }
  ```

Version 0.7.1
-------------

_2021-01-11_

* Update to KSP `1.4.20-dev-experimental-20210111`.

Version 0.7.0
-------------

_2020-12-26_

This introduces support for KSP's new incremental processing support. Because all outputs in both
`moshi-ksp` and `moshi-sealed`'s `codegen-ksp`, both of them are effectively "isolating" processors.

Note that incremental processing itself is _not_ enabled by default and must be enabled via
`ksp.incremental=true` Gradle property. See KSP's release notes for more details:
https://github.com/google/ksp/releases/tag/1.4.20-dev-experimental-20201222

* KSP `1.4.20-dev-experimental-20201222`
* Kotlin `1.4.20`

Version 0.6.1
-------------

_2020-11-12_

`moshi-ksp` and `moshi-sealed-ksp` are now built against KSP version `1.4.10-dev-experimental-20201110`.

Version 0.6.0
-------------

_2020-10-30_

#### moshi-sealed

`@TypeLabel` now has an optional `alternateLabels` array property for cases where multiple labels
can match the same sealed subtype.

```kotlin
@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Message {

  @TypeLabel("success", alternateLabels = ["successful"])
  @JsonClass(generateAdapter = true)
  data class Success(val value: String) : Message()
}
```

**NOTE:** We also changed `@TypeLabel`'s `value` property to the more meaningful `label` name. This
is technically a breaking change, but should be pretty low impact since most people wouldn't be
defining this parameter name or reading the property directly.

Version 0.5.0
-------------

_2020-10-25_

Dependency updates for all code generation artifacts:
* KSP `1.4.10-dev-experimental-20201023`
* KotlinPoet `1.7.2`

#### moshi-ksp

* Use KSP's new `asMemberOf` API for materializing type parameters, allowing us to remove a lot of ugly
  `moshi-ksp` code that existed to accomplish the same.
* Defer failing the compilation when errors are reported to the `KSPLogger` until the end of the KSP run,
  allowing reporting all errors rather than just the first.

#### moshi-sealed

`moshi-sealed-codegen` and `moshi-sealed-codegen-ksp` now generate proguard rules for generated adapters
on the fly, matching Moshi's new behavior introduced in 1.10.0.

Thanks to [@plnice](https://github.com/plnice) for contributing to this release.

Version 0.4.0
-------------

_2020-10-12_

Updated Moshi to 1.11.0

#### moshi-ksp

Updated to `1.4.10-dev-experimental-20201009`

#### moshi-ktx

Removed! These APIs live in Moshi natively now as of 1.11.0

#### moshi-adapters

New artifact!

First adapter in this release is a new `@JsonString` qualifier + adapter, so you can
capture raw JSON content from payloads. This is adapted from the recipe in Moshi.

```Kotlin
val moshi = Moshi.Builder()
  .add(JsonString.Factory())
  .build()

@JsonClass(generateAdapter = true)
data class Message(
  val type: String,
  /** Raw JSON string for the `data` key. */
  @JsonString val data: String
)
```

Get it via

```kotlin
dependencies {
  implementation("dev.zacsweers.moshix:moshi-adapters:<version>")
}
```

#### moshi-sealed

New support for multiple `object` subtypes. This allows for sentinel types who only contain an indicator
label but no other data.

In the below example, we have a `FunctionSpec` that defines the signature of a function and a
`Type` representations that can be used to model its return type and parameter types. These are all
`object` types, so any contents are skipped in its serialization and only its `type` key is read
by the `PolymorphicJsonAdapterFactory` to determine its type.

```kotlin
@JsonClass(generateAdapter = false, generator = "sealed:type")
sealed class Type(val type: String) {
  @TypeLabel("void")
  object VoidType : Type("void")
  @TypeLabel("boolean")
  object BooleanType : Type("boolean")
  @TypeLabel("int")
  object IntType : Type("int")
}

data class FunctionSpec(
 val name: String,
 val returnType: Type,
 val parameters: Map<String, Type>
)
```

**NOTE**: As part of this change, the `moshi-sealed-annotations` artifact was replaced with a
`moshi-sealed-runtime` artifact. Please update your coordinates accordingly, and don't use `compileOnly`
anymore.

Version 0.3.2
-------------

_2020-10-01_

Fixes two issues with `moshi-ksp`:
- Handle `Any` superclasses when the supertype is from another module
- Filter out non-`CLASS` kinds from supertypes

Special thanks to [@JvmName](https://github.com/JvmName) for reporting and helping debug this!

Version 0.3.1
-------------

_2020-09-30_

`moshi-ksp` now fully supports nullable generic types, which means it is now at feature parity with
Moshi's annotation-processor-based code gen 🥳

Version 0.3.0
-------------

_2020-09-27_

### THIS IS BIG

This project is now **MoshiX** and contains multiple Moshi extensions.

* **New:** [moshi-ksp](https://github.com/ZacSweers/MoshiX/blob/main/moshi-ksp/README.md) - A [KSP](https://github.com/google/ksp) implementation of Moshi Kotlin Codegen.
* **New:** [moshi-ktx](https://github.com/ZacSweers/MoshiX/blob/main/moshi-ktx/README.md) - Kotlin extensions for Moshi with no kotlin-reflect requirements and fully compatible with generic reified types via the stdlib's `typeOf()` API.
* **New:** [moshi-metadata-reflect](https://github.com/ZacSweers/MoshiX/blob/main/moshi-metadata-reflect/README.md) - A [kotlinx-metadata](https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata/jvm) based implementation of `KotlinJsonAdapterFactory`. This allows for reflective Moshi serialization on Kotlin classes without the cost of including kotlin-reflect.
* **Updated:** [moshi-sealed](https://github.com/ZacSweers/MoshiX/blob/main/moshi-sealed/README.md) - Largely unchanged, but now there is a new `moshi-sealed-ksp` artifact available for KSP users.

Some of these will eventually move to Moshi directly. This project going forward is a focused set of extensions that
either don't belong in Moshi directly or can be a non-API-stable testing ground for early adopters.

Version 0.2.0
-------------

_2020-04-26_

* Fix reflect artifact depending on a snapshot Moshi version.
* Update to Kotlin 1.3.72
* Update to KotlinPoet 1.5.0

Version 0.1.0
-------------

_2019-10-29_

Initial release!
