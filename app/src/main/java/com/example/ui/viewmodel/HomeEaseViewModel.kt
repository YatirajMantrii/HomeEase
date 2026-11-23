package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookingEntity
import com.example.data.local.HomeEaseDatabase
import com.example.data.local.HomeProfileEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.ProfessionalEntity
import com.example.data.local.QuotationEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.ServiceRequestEntity
import com.example.data.local.SessionPreferences
import com.example.data.local.UserAccountEntity
import com.example.data.model.CatalogData
import com.example.data.repository.HomeEaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeEaseUiState(
    val isInitialLoading: Boolean = true,
    val currentUser: UserAccountEntity? = null,
    val currentProProfile: ProfessionalEntity? = null,
    val selectedLocality: String = "Pune (All Areas)",
    val searchQuery: String = "",
    val professionals: List<ProfessionalEntity> = emptyList(),
    val shortlistedIds: Set<String> = emptySet(),
    val recentlyViewedIds: List<String> = emptyList(),
    val customerRequests: List<ServiceRequestEntity> = emptyList(),
    val allRequests: List<ServiceRequestEntity> = emptyList(),
    val allQuotations: List<QuotationEntity> = emptyList(),
    val customerBookings: List<BookingEntity> = emptyList(),
    val allBookings: List<BookingEntity> = emptyList(),
    val homeProfile: HomeProfileEntity? = null,
    val notifications: List<NotificationEntity> = emptyList(),
    val authError: String? = null,
    val isBusy: Boolean = false
) {
    val unreadNotificationCount: Int get() = notifications.count { !it.isRead }
    val isProfessionalMode: Boolean get() = currentUser?.role == "PROFESSIONAL"
    val shortlistedPros: List<ProfessionalEntity>
        get() = professionals.filter { it.id in shortlistedIds }
    val recentlyViewedPros: List<ProfessionalEntity>
        get() = recentlyViewedIds.mapNotNull { id -> professionals.find { it.id == id } }
}

class HomeEaseViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HomeEaseDatabase.getInstance(application)
    private val prefs = SessionPreferences(application)
    private val repository = HomeEaseRepository(db.homeEaseDao(), prefs)

    private val _searchQuery = MutableStateFlow("")
    private val _isInitialLoading = MutableStateFlow(true)
    private val _authError = MutableStateFlow<String?>(null)
    private val _isBusy = MutableStateFlow(false)

    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    val uiState: StateFlow<HomeEaseUiState> = combine(
        _isInitialLoading,
        repository.currentUser,
        repository.currentProfessionalProfile,
        repository.selectedLocality,
        _searchQuery,
        repository.allProfessionals,
        repository.shortlistedProIds,
        repository.recentlyViewedProIds,
        repository.customerRequests,
        repository.allRequests,
        repository.allQuotations,
        repository.customerBookings,
        repository.allBookings,
        repository.homeProfile,
        repository.notifications,
        _authError,
        _isBusy
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        HomeEaseUiState(
            isInitialLoading = values[0] as Boolean,
            currentUser = values[1] as UserAccountEntity?,
            currentProProfile = values[2] as ProfessionalEntity?,
            selectedLocality = values[3] as String,
            searchQuery = values[4] as String,
            professionals = values[5] as List<ProfessionalEntity>,
            shortlistedIds = (values[6] as List<String>).toSet(),
            recentlyViewedIds = values[7] as List<String>,
            customerRequests = values[8] as List<ServiceRequestEntity>,
            allRequests = values[9] as List<ServiceRequestEntity>,
            allQuotations = values[10] as List<QuotationEntity>,
            customerBookings = values[11] as List<BookingEntity>,
            allBookings = values[12] as List<BookingEntity>,
            homeProfile = values[13] as HomeProfileEntity?,
            notifications = values[14] as List<NotificationEntity>,
            authError = values[15] as String?,
            isBusy = values[16] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeEaseUiState()
    )

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            kotlinx.coroutines.delay(950)
            _isInitialLoading.value = false
        }
    }

    fun dismissSplashEarly() {
        _isInitialLoading.value = false
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectLocality(locality: String) {
        viewModelScope.launch {
            repository.setLocality(locality)
            _events.emit("Location updated to $locality")
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }

    fun signIn(email: String, password: String, onSuccess: (UserAccountEntity) -> Unit) {
        viewModelScope.launch {
            _isBusy.value = true
            _authError.value = null
            repository.signIn(email, password)
                .onSuccess { user ->
                    _events.emit("Welcome back, ${user.name}")
                    onSuccess(user)
                }
                .onFailure { err ->
                    _authError.value = err.message ?: "Unable to sign in"
                }
            _isBusy.value = false
        }
    }

    fun switchDemoAccount(isPro: Boolean, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val targetId = if (isPro) SessionPreferences.DEFAULT_PRO_ID else SessionPreferences.DEFAULT_CUSTOMER_ID
            repository.quickSwitchDemoAccount(targetId)
            _events.emit(if (isPro) "Switched to Professional: Rohan Deshmukh" else "Switched to Customer: Aarav Kulkarni")
            onDone()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _events.emit("Signed out")
        }
    }

    fun signUpCustomer(
        name: String,
        email: String,
        phone: String,
        locality: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            _authError.value = null
            repository.signUpCustomer(name, email, phone, locality, password)
                .onSuccess {
                    _events.emit("Account created for ${it.name}")
                    onSuccess()
                }
                .onFailure { err ->
                    _authError.value = err.message
                }
            _isBusy.value = false
        }
    }

    fun registerProfessional(
        name: String,
        email: String,
        phone: String,
        password: String,
        businessName: String,
        categoryId: String,
        experienceYears: Int,
        startingPriceInr: Int,
        serviceAreas: List<String>,
        bio: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            _authError.value = null
            repository.registerProfessional(
                name, email, phone, password, businessName, categoryId,
                experienceYears, startingPriceInr, serviceAreas, bio
            )
                .onSuccess {
                    _events.emit("Professional profile ready for $businessName")
                    onSuccess()
                }
                .onFailure { err ->
                    _authError.value = err.message
                }
            _isBusy.value = false
        }
    }

    fun updateProfessionalProfile(pro: ProfessionalEntity) {
        viewModelScope.launch {
            repository.updateProfessionalProfile(pro)
            _events.emit("Business profile updated")
        }
    }

    fun observeProfessional(proId: String): Flow<ProfessionalEntity?> =
        repository.observeProfessional(proId)

    fun observeReviews(proId: String): Flow<List<ReviewEntity>> =
        repository.observeReviewsForProfessional(proId)

    fun observeRequest(requestId: String): Flow<ServiceRequestEntity?> =
        repository.observeRequest(requestId)

    fun observeQuotationsForRequest(requestId: String): Flow<List<QuotationEntity>> =
        repository.observeQuotationsForRequest(requestId)

    fun observeBooking(bookingId: String): Flow<BookingEntity?> =
        repository.observeBooking(bookingId)

    fun recordRecentlyViewed(proId: String) {
        val uid = uiState.value.currentUser?.id ?: return
        viewModelScope.launch {
            repository.recordRecentlyViewed(uid, proId)
        }
    }

    fun toggleShortlist(proId: String) {
        val uid = uiState.value.currentUser?.id
        if (uid == null) {
            viewModelScope.launch { _events.emit("Please sign in to save professionals") }
            return
        }
        val saved = proId in uiState.value.shortlistedIds
        viewModelScope.launch {
            repository.toggleShortlist(uid, proId, saved)
            _events.emit(if (saved) "Removed from shortlist" else "Saved to your trusted shortlist")
        }
    }

    fun submitServiceRequest(
        categoryId: String,
        problemTitle: String,
        problemDescription: String,
        photoUris: List<String>,
        address: String,
        locality: String,
        preferredDate: String,
        isRelocationBundle: Boolean = false,
        preferredProId: String? = null,
        onSuccess: (ServiceRequestEntity) -> Unit
    ) {
        val user = uiState.value.currentUser
        val custId = user?.id ?: SessionPreferences.DEFAULT_CUSTOMER_ID
        val custName = user?.name ?: "Pune Guest"
        viewModelScope.launch {
            _isBusy.value = true
            repository.submitServiceRequest(
                customerId = custId,
                customerName = custName,
                categoryId = categoryId,
                problemTitle = problemTitle,
                problemDescription = problemDescription,
                photoUris = photoUris,
                address = address,
                locality = locality,
                preferredDate = preferredDate,
                isRelocationBundle = isRelocationBundle,
                preferredProId = preferredProId
            ).onSuccess { req ->
                _events.emit("Service request submitted!")
                onSuccess(req)
            }.onFailure { err ->
                _events.emit(err.message ?: "Could not submit request")
            }
            _isBusy.value = false
        }
    }

    fun submitQuotation(
        requestId: String,
        priceInr: Int,
        estimatedDuration: String,
        includedServices: String,
        additionalChargesNote: String,
        onSuccess: () -> Unit
    ) {
        val pro = uiState.value.currentProProfile ?: uiState.value.professionals.firstOrNull()
        if (pro == null) {
            viewModelScope.launch { _events.emit("Create a professional profile first") }
            return
        }
        viewModelScope.launch {
            repository.submitQuotation(
                requestId = requestId,
                pro = pro,
                priceInr = priceInr,
                estimatedDuration = estimatedDuration,
                includedServices = includedServices,
                additionalChargesNote = additionalChargesNote
            ).onSuccess {
                _events.emit("Quotation sent to customer")
                onSuccess()
            }.onFailure { err ->
                _events.emit(err.message ?: "Failed to send quotation")
            }
        }
    }

    fun declineRequest(requestId: String) {
        viewModelScope.launch {
            repository.declineRequest(requestId)
            _events.emit("Request marked declined")
        }
    }

    fun confirmBooking(quotationId: String, onSuccess: (BookingEntity) -> Unit) {
        viewModelScope.launch {
            _isBusy.value = true
            repository.confirmBookingFromQuotation(quotationId)
                .onSuccess { booking ->
                    _events.emit("Booking confirmed!")
                    onSuccess(booking)
                }
                .onFailure { err ->
                    _events.emit(err.message ?: "Unable to confirm booking")
                }
            _isBusy.value = false
        }
    }

    fun updateBookingStatus(bookingId: String, newStatus: String, note: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, newStatus, note)
            _events.emit("Booking status updated to ${newStatus.replace("_", " ").lowercase()}")
        }
    }

    fun submitBookingReview(booking: BookingEntity, rating: Int, text: String) {
        viewModelScope.launch {
            repository.submitBookingReview(booking, rating, text)
            _events.emit("Thank you for reviewing ${booking.professionalName}!")
        }
    }

    fun saveHomeProfile(
        homeType: String,
        approxSqFt: Int,
        locality: String,
        preferredSlot: String,
        notes: String
    ) {
        val uid = uiState.value.currentUser?.id ?: SessionPreferences.DEFAULT_CUSTOMER_ID
        viewModelScope.launch {
            repository.saveHomeProfile(
                HomeProfileEntity(
                    userId = uid,
                    homeType = homeType,
                    approxSqFt = approxSqFt,
                    locality = locality,
                    preferredSlot = preferredSlot,
                    notes = notes
                )
            )
            _events.emit("My Home Profile saved")
        }
    }

    fun markNotificationsRead() {
        val uid = uiState.value.currentUser?.id ?: return
        viewModelScope.launch {
            repository.markNotificationsRead(uid)
        }
    }

    fun submitContactInquiry(name: String, email: String, topic: String, message: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.submitContactInquiry(name, email, topic, message)
                .onSuccess {
                    _events.emit("Inquiry saved! Pune support team will respond shortly.")
                    onSuccess()
                }
                .onFailure { err ->
                    _events.emit(err.message ?: "Please complete all fields")
                }
        }
    }
}
