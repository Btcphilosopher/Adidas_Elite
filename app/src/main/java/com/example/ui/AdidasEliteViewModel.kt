package com.example.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.data.local.AdidasEliteRepository
import com.example.data.local.AppDatabase
import com.example.data.local.CartItemEntity
import com.example.data.local.FitProfileEntity
import com.example.data.local.LoggedWorkoutEntity
import com.example.data.model.ProductSystem
import com.example.data.model.SampleData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    COLLECTIONS,
    TRAINING,
    FITTING,
    BOUTIQUE,
    MEMBERSHIP
}

class AdidasEliteViewModel(
    private val repository: AdidasEliteRepository
) : ViewModel() {

    // --- NAVIGATION / METADATA ---
    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _selectedProduct = MutableStateFlow<ProductSystem?>(null)
    val selectedProduct: StateFlow<ProductSystem?> = _selectedProduct.asStateFlow()

    fun navigateToTab(tab: MainTab) {
        _currentTab.value = tab
        _selectedProduct.value = null // Close detail view on tab switch
    }

    fun selectProduct(product: ProductSystem?) {
        _selectedProduct.value = product
    }

    // --- PRODUCT FILTERING & SEARCH ---
    private val _selectedCollection = MutableStateFlow<String?>("All")
    val selectedCollection: StateFlow<String?> = _selectedCollection.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String>("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun selectCollectionFilter(collection: String?) {
        _selectedCollection.value = collection
    }

    fun selectCategoryFilter(category: String) {
        _selectedCategory.value = category
    }

    val filteredProducts: StateFlow<List<ProductSystem>> = combine(
        _selectedCollection,
        _selectedCategory
    ) { collection, category ->
        var list = SampleData.PRODUCTS
        if (collection != null && collection != "All") {
            list = list.filter { it.collection.equals(collection, ignoreCase = true) }
        }
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SampleData.PRODUCTS)

    // --- ROOM REACTIVE DATA CHANNELS ---
    val fitProfile: StateFlow<FitProfileEntity> = repository.fitProfile
        .map { it ?: FitProfileEntity() } // Default if null
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FitProfileEntity())

    val workouts: StateFlow<List<LoggedWorkoutEntity>> = repository.allWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- ATHLETE PERFORMANCE ANALYTICS ---
    val totalWorkoutsLogged: StateFlow<Int> = workouts.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDistanceKm: StateFlow<Double> = workouts.map { list ->
        list.sumOf { it.distanceKm }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalMinutes: StateFlow<Int> = workouts.map { list ->
        list.sumOf { it.durationMin }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Recommended Size Logic based on Fitting Profile info
    val recommendedFootwearSize: StateFlow<Double> = fitProfile.map { profile ->
        // Standard German engineered size formula:
        // cm length to US pricing (approximate)
        val baseLength = profile.footLengthCm
        var size = (baseLength - 20.0) * 1.3 + 3.0
        // Round to nearest 0.5 size
        Math.round(size * 2).toDouble() / 2.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 9.5)

    val recommendedApparelSize: StateFlow<String> = fitProfile.map { profile ->
        val bmi = profile.weightKg / ((profile.heightCm / 100.0) * (profile.heightCm / 100.0))
        when {
            bmi < 18.5 -> "S (Minimal Pro)"
            bmi < 24.9 -> "M (Precision Active)"
            bmi < 29.9 -> "L (Maxi-Athletic)"
            else -> "XL (Extended Form)"
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "M")

    // --- MUTABLE UI STATES ---
    // Digital Fitting variables
    val inputHeight = MutableStateFlow("180")
    val inputWeight = MutableStateFlow("75")
    val inputFootLength = MutableStateFlow("27.5")
    val inputBodyProfile = MutableStateFlow("Athletic")
    val inputSport = MutableStateFlow("Running")

    fun initFittingInputs(profile: FitProfileEntity) {
        inputHeight.value = profile.heightCm.toString()
        inputWeight.value = profile.weightKg.toString()
        inputFootLength.value = profile.footLengthCm.toString()
        inputBodyProfile.value = profile.bodyProfile
        inputSport.value = profile.primarySport
    }

    fun triggerFittingSave() {
        viewModelScope.launch {
            val h = inputHeight.value.toDoubleOrNull() ?: 180.0
            val w = inputWeight.value.toDoubleOrNull() ?: 75.0
            val fl = inputFootLength.value.toDoubleOrNull() ?: 27.5
            repository.saveProfile(
                FitProfileEntity(
                    id = 1,
                    heightCm = h,
                    weightKg = w,
                    footLengthCm = fl,
                    bodyProfile = inputBodyProfile.value,
                    primarySport = inputSport.value
                )
            )
        }
    }

    // Interactive Workout logger states
    val logSport = MutableStateFlow("Running")
    val logDuration = MutableStateFlow("30")
    val logDistance = MutableStateFlow("5.0")
    val logIntensity = MutableStateFlow("High")
    val logNotes = MutableStateFlow("")

    fun logWorkout() {
        viewModelScope.launch {
            val dur = logDuration.value.toIntOrNull() ?: 30
            val dist = logDistance.value.toDoubleOrNull() ?: 0.0
            val intensityString = logIntensity.value
            // Simple calorie estimation per sport and intensity
            val calPerMin = when (intensityString) {
                "Low" -> 6
                "Medium" -> 9
                "High" -> 13
                else -> 17 // Elite
            }
            val totalCal = dur * calPerMin
            repository.addWorkout(
                LoggedWorkoutEntity(
                    sportType = logSport.value,
                    durationMin = dur,
                    intensity = intensityString,
                    distanceKm = dist,
                    caloriesBurned = totalCal,
                    notes = logNotes.value
                )
            )
            // Reset input values to typical defaults
            logDuration.value = "30"
            logDistance.value = "5.0"
            logNotes.value = ""
        }
    }

    fun deleteLoggedWorkout(id: Int) {
        viewModelScope.launch {
            repository.deleteWorkout(id)
        }
    }

    // Interactive checkout configurations
    private val _customPackaging = MutableStateFlow("Standard Carbon-Neutral") // or "Premium Brushed Aluminum Case"
    val customPackaging: StateFlow<String> = _customPackaging.asStateFlow()

    fun selectPackaging(packaging: String) {
        _customPackaging.value = packaging
    }

    fun addProductToCart(product: ProductSystem, size: String) {
        viewModelScope.launch {
            repository.addToCart(
                CartItemEntity(
                    productId = product.id,
                    productName = product.name,
                    price = product.price,
                    size = size,
                    imageUrl = product.vectorSilhouetteId,
                    quantity = 1
                )
            )
        }
    }

    fun updateCartItemQuantity(id: Int, relative: Int) {
        viewModelScope.launch {
            val item = cartItems.value.find { it.id == id } ?: return@launch
            val newQty = item.quantity + relative
            repository.updateCartQuantity(id, newQty)
        }
    }

    fun removeItemFromCart(id: Int) {
        viewModelScope.launch {
            repository.removeFromCart(id)
        }
    }

    // Simulated Checkout flow state
    private val _isCheckingOut = MutableStateFlow(false)
    val isCheckingOut: StateFlow<Boolean> = _isCheckingOut.asStateFlow()

    private val _checkoutSuccess = MutableStateFlow(false)
    val checkoutSuccess: StateFlow<Boolean> = _checkoutSuccess.asStateFlow()

    fun startCheckout() {
        _isCheckingOut.value = true
        _checkoutSuccess.value = false
    }

    fun performSimulatedPurchase() {
        viewModelScope.launch {
            _isCheckingOut.value = false
            _checkoutSuccess.value = true
            // Clear cart items on successful order
            repository.clearCart()
        }
    }

    fun dismissCheckoutSuccess() {
        _checkoutSuccess.value = false
    }

    // Support single interactive dropdown/sheet for adding products
    // Footwear visual details: selected size in screen
    val selectedProductSize = MutableStateFlow("9.5")
    val selected360Angle = MutableStateFlow(0f) // 360-degree rotation index
    val selectedVisualMode = MutableStateFlow("Solid") // Solid, Material Close-Up, Wireframe X-Ray
}

@Suppress("UNCHECKED_CAST")
class AdidasEliteViewModelFactory(
    private val repository: AdidasEliteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdidasEliteViewModel::class.java)) {
            return AdidasEliteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
