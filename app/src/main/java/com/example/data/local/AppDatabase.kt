package com.example.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- --- --- ENTITIES --- --- ---

@Entity(tableName = "fit_profile")
data class FitProfileEntity(
    @PrimaryKey val id: Int = 1,
    val heightCm: Double = 180.0,
    val weightKg: Double = 75.0,
    val footLengthCm: Double = 27.5,
    val bodyProfile: String = "Athletic",
    val primarySport: String = "Running"
)

@Entity(tableName = "logged_workouts")
data class LoggedWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sportType: String,
    val durationMin: Int,
    val intensity: String, // "Low", "Medium", "High", "Elite"
    val distanceKm: Double = 0.0,
    val caloriesBurned: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val productName: String,
    val price: Double,
    val size: String,
    val imageUrl: String = "",
    val quantity: Int = 1
)

// --- --- --- DAOS --- --- ---

@Dao
interface FitProfileDao {
    @Query("SELECT * FROM fit_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<FitProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: FitProfileEntity)
}

@Dao
interface LoggedWorkoutDao {
    @Query("SELECT * FROM logged_workouts ORDER BY timestamp DESC")
    fun getAllWorkouts(): Flow<List<LoggedWorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: LoggedWorkoutEntity)

    @Query("DELETE FROM logged_workouts WHERE id = :id")
    suspend fun deleteWorkout(id: Int)

    @Query("DELETE FROM logged_workouts")
    suspend fun clearAll()
}

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

// --- --- --- DATABASE --- --- ---

@Database(
    entities = [FitProfileEntity::class, LoggedWorkoutEntity::class, CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fitProfileDao(): FitProfileDao
    abstract fun loggedWorkoutDao(): LoggedWorkoutDao
    abstract fun cartItemDao(): CartItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adidas_elite_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// --- --- --- REPOSITORY --- --- ---

class AdidasEliteRepository(private val db: AppDatabase) {
    val fitProfile: Flow<FitProfileEntity?> = db.fitProfileDao().getProfile()
    val allWorkouts: Flow<List<LoggedWorkoutEntity>> = db.loggedWorkoutDao().getAllWorkouts()
    val cartItems: Flow<List<CartItemEntity>> = db.cartItemDao().getCartItems()

    suspend fun saveProfile(profile: FitProfileEntity) {
        db.fitProfileDao().saveProfile(profile)
    }

    suspend fun addWorkout(workout: LoggedWorkoutEntity) {
        db.loggedWorkoutDao().insertWorkout(workout)
    }

    suspend fun deleteWorkout(id: Int) {
        db.loggedWorkoutDao().deleteWorkout(id)
    }

    suspend fun clearWorkouts() {
        db.loggedWorkoutDao().clearAll()
    }

    suspend fun addToCart(item: CartItemEntity) {
        db.cartItemDao().insertCartItem(item)
    }

    suspend fun updateCartQuantity(id: Int, quantity: Int) {
        if (quantity <= 0) {
            db.cartItemDao().deleteCartItem(id)
        } else {
            db.cartItemDao().updateQuantity(id, quantity)
        }
    }

    suspend fun removeFromCart(id: Int) {
        db.cartItemDao().deleteCartItem(id)
    }

    suspend fun clearCart() {
        db.cartItemDao().clearCart()
    }
}
