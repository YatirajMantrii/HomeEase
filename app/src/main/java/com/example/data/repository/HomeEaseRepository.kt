package com.example.data.repository

import com.example.data.local.BookingEntity
import com.example.data.local.ContactInquiryEntity
import com.example.data.local.HomeEaseDao
import com.example.data.local.HomeEaseDatabase
import com.example.data.local.HomeProfileEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.ProfessionalEntity
import com.example.data.local.QuotationEntity
import com.example.data.local.RecentlyViewedEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.ServiceRequestEntity
import com.example.data.local.SessionPreferences
import com.example.data.local.ShortlistEntity
import com.example.data.local.UserAccountEntity
import com.example.data.model.CatalogData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeEaseRepository(
    private val dao: HomeEaseDao,
    private val prefs: SessionPreferences
) {
    val loggedInUserId: Flow<String> = prefs.loggedInUserId
    val selectedLocality: Flow<String> = prefs.selectedLocality

    val currentUser: Flow<UserAccountEntity?> = loggedInUserId.flatMapLatest { uid ->
        if (uid == SessionPreferences.GUEST_USER_ID) flowOf(null)
        else dao.observeUserById(uid)
    }

    val currentProfessionalProfile: Flow<ProfessionalEntity?> = loggedInUserId.flatMapLatest { uid ->
        if (uid == SessionPreferences.GUEST_USER_ID) flowOf(null)
        else dao.observeProfessionalByUserId(uid)
    }

    val allProfessionals: Flow<List<ProfessionalEntity>> = dao.observeAllProfessionals()
    val allRequests: Flow<List<ServiceRequestEntity>> = dao.observeAllRequests()
    val allQuotations: Flow<List<QuotationEntity>> = dao.observeAllQuotations()
    val allBookings: Flow<List<BookingEntity>> = dao.observeAllBookings()
    val allReviews: Flow<List<ReviewEntity>> = dao.observeAllReviews()

    val customerRequests: Flow<List<ServiceRequestEntity>> = loggedInUserId.flatMapLatest { uid ->
        dao.observeRequestsForCustomer(uid)
    }

    val customerBookings: Flow<List<BookingEntity>> = loggedInUserId.flatMapLatest { uid ->
        dao.observeBookingsForCustomer(uid)
    }

    val shortlistedProIds: Flow<List<String>> = loggedInUserId.flatMapLatest { uid ->
        dao.observeShortlistedProIds(uid)
    }

    val recentlyViewedProIds: Flow<List<String>> = loggedInUserId.flatMapLatest { uid ->
        dao.observeRecentlyViewedProIds(uid)
    }

    val homeProfile: Flow<HomeProfileEntity?> = loggedInUserId.flatMapLatest { uid ->
        dao.observeHomeProfile(uid)
    }

    val notifications: Flow<List<NotificationEntity>> = loggedInUserId.flatMapLatest { uid ->
        dao.observeNotifications(uid)
    }

    suspend fun ensureSeeded() {
        HomeEaseDatabase.seedIfEmpty(dao)
    }

    suspend fun setLocality(locality: String) {
        prefs.setSelectedLocality(locality)
    }

    fun observeProfessional(proId: String): Flow<ProfessionalEntity?> =
        dao.observeProfessionalById(proId)

    fun observeReviewsForProfessional(proId: String): Flow<List<ReviewEntity>> =
        dao.observeReviewsForProfessional(proId)

    fun observeRequest(requestId: String): Flow<ServiceRequestEntity?> =
        dao.observeRequestById(requestId)

    fun observeQuotationsForRequest(requestId: String): Flow<List<QuotationEntity>> =
        dao.observeQuotationsForRequest(requestId)

    fun observeBooking(bookingId: String): Flow<BookingEntity?> =
        dao.observeBookingById(bookingId)

    suspend fun recordRecentlyViewed(userId: String, proId: String) {
        if (userId == SessionPreferences.GUEST_USER_ID) return
        dao.upsertRecentlyViewed(RecentlyViewedEntity(userId, proId, System.currentTimeMillis()))
    }

    suspend fun toggleShortlist(userId: String, proId: String, currentlySaved: Boolean) {
        if (userId == SessionPreferences.GUEST_USER_ID) return
        if (currentlySaved) {
            dao.removeFromShortlist(userId, proId)
        } else {
            dao.addToShortlist(ShortlistEntity(userId, proId, System.currentTimeMillis()))
        }
    }

    suspend fun signIn(email: String, password: String): Result<UserAccountEntity> {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Please enter both email and password."))
        }
        val user = dao.getUserByEmail(cleanEmail)
            ?: return Result.failure(IllegalArgumentException("No local account found for $cleanEmail."))
        if (user.passwordHash != HomeEaseDatabase.hashPassword(password)) {
            return Result.failure(IllegalArgumentException("Incorrect password. Try again or use a demo account."))
        }
        prefs.setLoggedInUserId(user.id)
        prefs.setSelectedLocality(user.locality)
        return Result.success(user)
    }

    suspend fun quickSwitchDemoAccount(userId: String) {
        val user = dao.getUserById(userId) ?: return
        prefs.setLoggedInUserId(user.id)
        prefs.setSelectedLocality(user.locality)
    }

    suspend fun signOut() {
        prefs.setLoggedInUserId(SessionPreferences.GUEST_USER_ID)
    }

    suspend fun signUpCustomer(
        name: String,
        email: String,
        phone: String,
        locality: String,
        password: String
    ): Result<UserAccountEntity> {
        val cleanEmail = email.trim().lowercase()
        if (name.isBlank() || cleanEmail.isBlank() || phone.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Complete all fields (password min 6 characters)."))
        }
        if (dao.getUserByEmail(cleanEmail) != null) {
            return Result.failure(IllegalArgumentException("An account with $cleanEmail already exists."))
        }
        val userId = "user_${UUID.randomUUID().toString().take(8)}"
        val user = UserAccountEntity(
            id = userId,
            name = name.trim(),
            email = cleanEmail,
            phone = phone.trim(),
            role = "CUSTOMER",
            locality = locality,
            passwordHash = HomeEaseDatabase.hashPassword(password)
        )
        dao.insertUser(user)
        dao.insertNotification(
            NotificationEntity(
                id = "notif_${UUID.randomUUID().toString().take(8)}",
                userId = userId,
                title = "Welcome to HomeEase Pune",
                message = "Save your Home Profile or request your first local service quotation.",
                relatedRoute = "home_profile"
            )
        )
        prefs.setLoggedInUserId(userId)
        prefs.setSelectedLocality(locality)
        return Result.success(user)
    }

    suspend fun registerProfessional(
        name: String,
        email: String,
        phone: String,
        password: String,
        businessName: String,
        categoryId: String,
        experienceYears: Int,
        startingPriceInr: Int,
        serviceAreas: List<String>,
        bio: String
    ): Result<UserAccountEntity> {
        val cleanEmail = email.trim().lowercase()
        if (name.isBlank() || cleanEmail.isBlank() || businessName.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Please fill name, email, business name, and 6+ char password."))
        }
        if (dao.getUserByEmail(cleanEmail) != null) {
            return Result.failure(IllegalArgumentException("An account with $cleanEmail already exists."))
        }
        val userId = "user_pro_${UUID.randomUUID().toString().take(8)}"
        val primaryLocality = serviceAreas.firstOrNull() ?: "Pune"
        val user = UserAccountEntity(
            id = userId,
            name = name.trim(),
            email = cleanEmail,
            phone = phone.trim(),
            role = "PROFESSIONAL",
            locality = primaryLocality,
            passwordHash = HomeEaseDatabase.hashPassword(password)
        )
        dao.insertUser(user)

        val category = CatalogData.categoryById(categoryId)
        val pro = ProfessionalEntity(
            id = "pro_${UUID.randomUUID().toString().take(8)}",
            userId = userId,
            name = name.trim(),
            businessName = businessName.trim(),
            categoryId = categoryId,
            experienceYears = experienceYears.coerceAtLeast(1),
            rating = 5.0,
            reviewCount = 0,
            completedJobs = 0,
            startingPriceInr = startingPriceInr.coerceAtLeast(199),
            serviceAreasCsv = if (serviceAreas.isEmpty()) "Pune" else serviceAreas.joinToString(", "),
            bio = bio.ifBlank { "Independent ${category.name} specialist serving Pune households." },
            skillsCsv = category.commonTasks.joinToString(", "),
            portfolioHighlightsCsv = "New Pune Local Partner Profile",
            responseTimeText = "Usually quotes within 2 hours",
            pricingModel = "Quote-Based"
        )
        dao.upsertProfessional(pro)
        prefs.setLoggedInUserId(userId)
        prefs.setSelectedLocality(primaryLocality)
        return Result.success(user)
    }

    suspend fun updateProfessionalProfile(pro: ProfessionalEntity) {
        dao.upsertProfessional(pro)
    }

    suspend fun submitServiceRequest(
        customerId: String,
        customerName: String,
        categoryId: String,
        problemTitle: String,
        problemDescription: String,
        photoUris: List<String>,
        address: String,
        locality: String,
        preferredDate: String,
        isRelocationBundle: Boolean = false,
        preferredProId: String? = null
    ): Result<ServiceRequestEntity> {
        if (problemTitle.isBlank() || problemDescription.isBlank() || address.isBlank() || preferredDate.isBlank()) {
            return Result.failure(IllegalArgumentException("Please complete title, description, address, and preferred date."))
        }
        val category = CatalogData.categoryById(categoryId)
        val reqId = "req_${UUID.randomUUID().toString().take(8)}"
        val request = ServiceRequestEntity(
            id = reqId,
            customerId = customerId,
            customerName = customerName,
            categoryId = categoryId,
            categoryName = if (isRelocationBundle) "Move-In Bundle (${category.name})" else category.name,
            problemTitle = problemTitle.trim(),
            problemDescription = problemDescription.trim(),
            photoUrisCsv = photoUris.joinToString("|"),
            address = address.trim(),
            locality = locality,
            preferredDate = preferredDate,
            status = "OPEN_FOR_QUOTES",
            isRelocationBundle = isRelocationBundle,
            preferredProId = preferredProId
        )
        dao.insertServiceRequest(request)
        dao.insertNotification(
            NotificationEntity(
                id = "notif_${UUID.randomUUID().toString().take(8)}",
                userId = customerId,
                title = "Service Request Posted",
                message = "\"${request.problemTitle}\" is now open in $locality. Switch to Professional Dashboard to test quoting or await local quotes.",
                relatedRoute = "compare_quotes/$reqId"
            )
        )
        // Also notify default demo pro if relevant
        dao.insertNotification(
            NotificationEntity(
                id = "notif_${UUID.randomUUID().toString().take(8)}",
                userId = SessionPreferences.DEFAULT_PRO_ID,
                title = "New Customer Request ($locality)",
                message = "${request.customerName} needs ${request.categoryName}: ${request.problemTitle}.",
                relatedRoute = "pro_dashboard"
            )
        )
        return Result.success(request)
    }

    suspend fun submitQuotation(
        requestId: String,
        pro: ProfessionalEntity,
        priceInr: Int,
        estimatedDuration: String,
        includedServices: String,
        additionalChargesNote: String
    ): Result<QuotationEntity> {
        if (priceInr <= 0 || estimatedDuration.isBlank() || includedServices.isBlank()) {
            return Result.failure(IllegalArgumentException("Enter valid price, estimated completion time, and included scope."))
        }
        val quoteId = "quote_${UUID.randomUUID().toString().take(8)}"
        val cleanIncluded = includedServices
            .split("\n", ",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString("|")
        val quote = QuotationEntity(
            id = quoteId,
            requestId = requestId,
            professionalId = pro.id,
            professionalName = pro.name,
            professionalBusinessName = pro.businessName,
            professionalExperienceYears = pro.experienceYears,
            professionalRating = pro.rating,
            professionalReviewCount = pro.reviewCount,
            priceInr = priceInr,
            estimatedDuration = estimatedDuration.trim(),
            includedServicesCsv = cleanIncluded,
            additionalChargesNote = additionalChargesNote.ifBlank { "No hidden charges; standard tools included." },
            status = "PENDING"
        )
        dao.insertQuotation(quote)
        dao.updateRequestStatus(requestId, "QUOTES_RECEIVED")

        val req = dao.getRequestById(requestId)
        if (req != null) {
            dao.insertNotification(
                NotificationEntity(
                    id = "notif_${UUID.randomUUID().toString().take(8)}",
                    userId = req.customerId,
                    title = "New Quotation from ${pro.name}",
                    message = "₹$priceInr (${estimatedDuration.trim()}) for ${req.problemTitle}. Tap to compare.",
                    relatedRoute = "compare_quotes/$requestId"
                )
            )
        }
        return Result.success(quote)
    }

    suspend fun declineRequest(requestId: String) {
        dao.updateRequestStatus(requestId, "DECLINED")
    }

    suspend fun confirmBookingFromQuotation(quotationId: String): Result<BookingEntity> {
        val quote = dao.getQuotationById(quotationId)
            ?: return Result.failure(IllegalArgumentException("Quotation not found."))
        val req = dao.getRequestById(quote.requestId)
            ?: return Result.failure(IllegalArgumentException("Service request not found."))

        val bookingId = "BK-${UUID.randomUUID().toString().take(6).uppercase()}"
        val booking = BookingEntity(
            id = bookingId,
            requestId = req.id,
            quotationId = quote.id,
            customerId = req.customerId,
            customerName = req.customerName,
            professionalId = quote.professionalId,
            professionalName = quote.professionalName,
            categoryId = req.categoryId,
            categoryName = req.categoryName,
            serviceTitle = req.problemTitle,
            address = req.address,
            locality = req.locality,
            scheduledDate = req.preferredDate,
            agreedPriceInr = quote.priceInr,
            status = "CONFIRMED",
            proStatusNote = "${quote.professionalBusinessName} confirmed your slot (${quote.estimatedDuration})."
        )
        dao.insertBooking(booking)
        dao.updateQuotationStatus(quote.id, "ACCEPTED")
        dao.declineOtherQuotations(req.id, quote.id)
        dao.updateRequestStatus(req.id, "BOOKED")

        dao.insertNotification(
            NotificationEntity(
                id = "notif_${UUID.randomUUID().toString().take(8)}",
                userId = req.customerId,
                title = "Booking Confirmed ($bookingId)",
                message = "${quote.professionalName} is booked for ${req.preferredDate} at ₹${quote.priceInr}.",
                relatedRoute = "bookings"
            )
        )
        return Result.success(booking)
    }

    suspend fun updateBookingStatus(bookingId: String, newStatus: String, note: String) {
        dao.updateBookingStatus(bookingId, newStatus, note)
    }

    suspend fun submitBookingReview(
        booking: BookingEntity,
        rating: Int,
        reviewText: String
    ) {
        val clamped = rating.coerceIn(1, 5)
        dao.saveBookingReview(booking.id, clamped, reviewText.trim())
        dao.insertReview(
            ReviewEntity(
                id = "rev_${UUID.randomUUID().toString().take(8)}",
                professionalId = booking.professionalId,
                customerName = booking.customerName,
                rating = clamped,
                comment = reviewText.trim().ifBlank { "Great work and smooth experience." },
                serviceName = booking.categoryName,
                dateLabel = "Nov 2026"
            )
        )
        val pro = dao.getProfessionalById(booking.professionalId)
        if (pro != null) {
            val totalScore = pro.rating * pro.reviewCount + clamped
            val newCount = pro.reviewCount + 1
            val newAvg = ((totalScore / newCount) * 10.0).toInt() / 10.0
            dao.upsertProfessional(
                pro.copy(
                    rating = newAvg,
                    reviewCount = newCount,
                    completedJobs = pro.completedJobs + 1
                )
            )
        }
    }

    suspend fun saveHomeProfile(profile: HomeProfileEntity) {
        dao.upsertHomeProfile(profile)
    }

    suspend fun markNotificationsRead(userId: String) {
        dao.markAllNotificationsRead(userId)
    }

    suspend fun submitContactInquiry(
        name: String,
        email: String,
        topic: String,
        message: String
    ): Result<Unit> {
        if (name.isBlank() || email.isBlank() || message.isBlank()) {
            return Result.failure(IllegalArgumentException("Please enter your name, email, and message."))
        }
        dao.insertContactInquiry(
            ContactInquiryEntity(
                id = "inq_${UUID.randomUUID().toString().take(8)}",
                name = name.trim(),
                email = email.trim(),
                topic = topic.ifBlank { "General Support" },
                message = message.trim()
            )
        )
        return Result.success(Unit)
    }
}
