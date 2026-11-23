package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeEaseDao {
    // Users
    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE id = :id LIMIT 1")
    fun observeUserById(id: String): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_accounts WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserAccountEntity)

    // Professionals
    @Query("SELECT * FROM professionals ORDER BY rating DESC, completedJobs DESC")
    fun observeAllProfessionals(): Flow<List<ProfessionalEntity>>

    @Query("SELECT * FROM professionals WHERE id = :id LIMIT 1")
    fun observeProfessionalById(id: String): Flow<ProfessionalEntity?>

    @Query("SELECT * FROM professionals WHERE userId = :userId LIMIT 1")
    fun observeProfessionalByUserId(userId: String): Flow<ProfessionalEntity?>

    @Query("SELECT * FROM professionals WHERE userId = :userId LIMIT 1")
    suspend fun getProfessionalByUserId(userId: String): ProfessionalEntity?

    @Query("SELECT * FROM professionals WHERE id = :id LIMIT 1")
    suspend fun getProfessionalById(id: String): ProfessionalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfessionals(pros: List<ProfessionalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfessional(pro: ProfessionalEntity)

    // Shortlist
    @Query("SELECT professionalId FROM shortlist WHERE userId = :userId ORDER BY savedAt DESC")
    fun observeShortlistedProIds(userId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToShortlist(entry: ShortlistEntity)

    @Query("DELETE FROM shortlist WHERE userId = :userId AND professionalId = :proId")
    suspend fun removeFromShortlist(userId: String, proId: String)

    // Recently Viewed
    @Query("SELECT professionalId FROM recently_viewed WHERE userId = :userId ORDER BY viewedAt DESC LIMIT 8")
    fun observeRecentlyViewedProIds(userId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecentlyViewed(entry: RecentlyViewedEntity)

    // Service Requests
    @Query("SELECT * FROM service_requests WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun observeRequestsForCustomer(customerId: String): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests ORDER BY createdAt DESC")
    fun observeAllRequests(): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests WHERE id = :requestId LIMIT 1")
    fun observeRequestById(requestId: String): Flow<ServiceRequestEntity?>

    @Query("SELECT * FROM service_requests WHERE id = :requestId LIMIT 1")
    suspend fun getRequestById(requestId: String): ServiceRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceRequest(request: ServiceRequestEntity)

    @Query("UPDATE service_requests SET status = :status WHERE id = :requestId")
    suspend fun updateRequestStatus(requestId: String, status: String)

    // Quotations
    @Query("SELECT * FROM quotations WHERE requestId = :requestId ORDER BY priceInr ASC")
    fun observeQuotationsForRequest(requestId: String): Flow<List<QuotationEntity>>

    @Query("SELECT * FROM quotations ORDER BY createdAt DESC")
    fun observeAllQuotations(): Flow<List<QuotationEntity>>

    @Query("SELECT * FROM quotations WHERE id = :quotationId LIMIT 1")
    suspend fun getQuotationById(quotationId: String): QuotationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotation(quotation: QuotationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotations(quotations: List<QuotationEntity>)

    @Query("UPDATE quotations SET status = :status WHERE id = :quotationId")
    suspend fun updateQuotationStatus(quotationId: String, status: String)

    @Query("UPDATE quotations SET status = 'DECLINED' WHERE requestId = :requestId AND id != :acceptedQuotationId")
    suspend fun declineOtherQuotations(requestId: String, acceptedQuotationId: String)

    // Bookings
    @Query("SELECT * FROM bookings WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun observeBookingsForCustomer(customerId: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE professionalId = :proId ORDER BY createdAt DESC")
    fun observeBookingsForProfessional(proId: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun observeAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :bookingId LIMIT 1")
    fun observeBookingById(bookingId: String): Flow<BookingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status, proStatusNote = :note WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: String, status: String, note: String)

    @Query("UPDATE bookings SET customerReviewRating = :rating, customerReviewText = :review WHERE id = :bookingId")
    suspend fun saveBookingReview(bookingId: String, rating: Int, review: String)

    // Home Profile
    @Query("SELECT * FROM home_profiles WHERE userId = :userId LIMIT 1")
    fun observeHomeProfile(userId: String): Flow<HomeProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHomeProfile(profile: HomeProfileEntity)

    // Notifications
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeNotifications(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllNotificationsRead(userId: String)

    // Reviews
    @Query("SELECT * FROM reviews WHERE professionalId = :proId ORDER BY id DESC")
    fun observeReviewsForProfessional(proId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun observeAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    // Contact Inquiries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactInquiry(inquiry: ContactInquiryEntity)

    @Query("SELECT COUNT(*) FROM professionals")
    suspend fun getProfessionalCount(): Int
}
