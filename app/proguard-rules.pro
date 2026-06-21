# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep class androidx.room.** { *; }

# Kotlin Metadata
-keep class kotlin.Metadata { *; }

# Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}