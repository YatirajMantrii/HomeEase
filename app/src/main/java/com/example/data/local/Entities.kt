package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // "CUSTOMER" or "PROFESSIONAL"
    val locality: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "professionals")
data class ProfessionalEntity(
    @PrimaryKey val id: String,
    val userId: String? = null,
    val name: String,
    val businessName: String,
    val categoryId: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewCount: Int,
    val completedJobs: Int,
    val startingPriceInr: Int,
    val serviceAreasCsv: String,
    val bio: String,
    val skillsCsv: String,
    val portfolioHighlightsCsv: String,
    val responseTimeText: String,
    val pricingModel: String // "Starting Price" or "Quote-Based"
)

@Entity(tableName = "shortlist", primaryKeys = ["userId", "professionalId"])
data class ShortlistEntity(
    val userId: String,
    val professionalId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "recently_viewed", primaryKeys = ["userId", "professionalId"])
data class RecentlyViewedEntity(
    val userId: String,
    val professionalId: String,
    val viewedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
    @PrimaryKey val id: String,
    val customerId: String,
    val customerName: String,
    val categoryId: String,
    val categoryName: String,
    val problemTitle: String,
    val problemDescription: String,
    val photoUrisCsv: String,
    val address: String,
    val locality: String,
    val preferredDate: String,
    val status: String, // "OPEN_FOR_QUOTES", "QUOTES_RECEIVED", "BOOKED", "DECLINED"
    val isRelocationBundle: Boolean = false,
    val preferredProId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quotations")
data class QuotationEntity(
    @PrimaryKey val id: String,
    val requestId: String,
    val professionalId: String,
    val professionalName: String,
    val professionalBusinessName: String,
    val professionalExperienceYears: Int,
    val professionalRating: Double,
    val professionalReviewCount: Int,
    val priceInr: Int,
    val estimatedDuration: String,
    val includedServicesCsv: String,
    val additionalChargesNote: String,
    val status: String, // "PENDING", "ACCEPTED", "DECLINED"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val requestId: String,
    val quotationId: String,
    val customerId: String,
    val customerName: String,
    val professionalId: String,
    val professionalName: String,
    val categoryId: String,
    val categoryName: String,
    val serviceTitle: String,
    val address: String,
    val locality: String,
    val scheduledDate: String,
    val agreedPriceInr: Int,
    val status: String, // "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    val proStatusNote: String = "Booking confirmed. Professional will arrive on schedule.",
    val customerReviewRating: Int? = null,
    val customerReviewText: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "home_profiles")
data class HomeProfileEntity(
    @PrimaryKey val userId: String,
    val homeType: String,
    val approxSqFt: Int,
    val locality: String,
    val preferredSlot: String,
    val notes: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val isRead: Boolean = false,
    val relatedRoute: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val professionalId: String,
    val customerName: String,
    val rating: Int,
    val comment: String,
    val serviceName: String,
    val dateLabel: String
)

@Entity(tableName = "contact_inquiries")
data class ContactInquiryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val topic: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)
