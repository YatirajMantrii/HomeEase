package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Database(
    entities = [
        UserAccountEntity::class,
        ProfessionalEntity::class,
        ShortlistEntity::class,
        RecentlyViewedEntity::class,
        ServiceRequestEntity::class,
        QuotationEntity::class,
        BookingEntity::class,
        HomeProfileEntity::class,
        NotificationEntity::class,
        ReviewEntity::class,
        ContactInquiryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HomeEaseDatabase : RoomDatabase() {
    abstract fun homeEaseDao(): HomeEaseDao

    companion object {
        @Volatile
        private var INSTANCE: HomeEaseDatabase? = null

        fun getInstance(context: Context): HomeEaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HomeEaseDatabase::class.java,
                    "homeease_pune.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun hashPassword(raw: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = digest.digest(raw.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        suspend fun seedIfEmpty(dao: HomeEaseDao) {
            if (dao.getProfessionalCount() > 0) return

            val now = System.currentTimeMillis()
            val custId = SessionPreferences.DEFAULT_CUSTOMER_ID
            val proUserId = SessionPreferences.DEFAULT_PRO_ID

            dao.insertUser(
                UserAccountEntity(
                    id = custId,
                    name = "Aarav Kulkarni",
                    email = "aarav@pune.homeease.local",
                    phone = "+91 98220 45120",
                    role = "CUSTOMER",
                    locality = "Kothrud",
                    passwordHash = hashPassword("homeease123"),
                    createdAt = now - 86400_000L * 14
                )
            )
            dao.insertUser(
                UserAccountEntity(
                    id = proUserId,
                    name = "Rohan Deshmukh",
                    email = "rohan.pro@pune.homeease.local",
                    phone = "+91 97650 31890",
                    role = "PROFESSIONAL",
                    locality = "Baner",
                    passwordHash = hashPassword("homeease123"),
                    createdAt = now - 86400_000L * 30
                )
            )

            val pros = listOf(
                ProfessionalEntity(
                    id = "pro_rohan",
                    userId = proUserId,
                    name = "Rohan Deshmukh",
                    businessName = "Sahyadri Craft Painters",
                    categoryId = "painting",
                    experienceYears = 9,
                    rating = 4.9,
                    reviewCount = 84,
                    completedJobs = 215,
                    startingPriceInr = 3499,
                    serviceAreasCsv = "Kothrud, Baner, Aundh, Bavdhan",
                    bio = "Specializing in low-VOC interior emulsions, damp-proof primer prep, and tidy furniture masking for Pune apartments.",
                    skillsCsv = "Interior Repainting, Monsoon Waterproofing, Accent Textures, Putty Finish",
                    portfolioHighlightsCsv = "2 BHK Move-In Refresh in Baner|Accent Lime-Wash Wall in Kothrud|Balcony Damp Seal in Bavdhan",
                    responseTimeText = "Usually quotes within 2 hours",
                    pricingModel = "Starting Price"
                ),
                ProfessionalEntity(
                    id = "pro_sneha",
                    userId = null,
                    name = "Sneha Joshi",
                    businessName = "Deccan FreshSpace Care",
                    categoryId = "cleaning",
                    experienceYears = 6,
                    rating = 4.8,
                    reviewCount = 67,
                    completedJobs = 310,
                    startingPriceInr = 1499,
                    serviceAreasCsv = "Kothrud, Karve Nagar, Koregaon Park, Viman Nagar",
                    bio = "Detail-oriented residential deep cleaning crew focused on move-in flat sanitization, kitchen degreasing, and eco-conscious products.",
                    skillsCsv = "Move-In Deep Clean, Kitchen Degreasing, Hard-Water Descaling, Sofa Sanitization",
                    portfolioHighlightsCsv = "3 BHK Pre-Move Deep Clean in Koregaon Park|Modular Kitchen Oil Recovery in Aundh",
                    responseTimeText = "Usually quotes within 1 hour",
                    pricingModel = "Starting Price"
                ),
                ProfessionalEntity(
                    id = "pro_vikram",
                    userId = null,
                    name = "Vikram Pawar",
                    businessName = "Pawar Precision Plumbing",
                    categoryId = "plumbing",
                    experienceYears = 11,
                    rating = 4.9,
                    reviewCount = 112,
                    completedJobs = 440,
                    startingPriceInr = 299,
                    serviceAreasCsv = "Kothrud, Wakad, Baner, Hinjewadi, Aundh",
                    bio = "Methodical diagnosis of concealed bathroom leaks, solar heater piping, Jaquar/Kohler mixer installations, and tank pressure lines.",
                    skillsCsv = "Leak Detection, Mixer & Diverter Fitment, Solar Line Repair, Drain Clearing",
                    portfolioHighlightsCsv = "Concealed Shower Valve Rebuild in Baner|Washing Machine & RO Point Setup in Wakad",
                    responseTimeText = "Usually quotes within 45 mins",
                    pricingModel = "Quote-Based"
                ),
                ProfessionalEntity(
                    id = "pro_amit",
                    userId = null,
                    name = "Amit Gokhale",
                    businessName = "Gokhale SafeVolt Solutions",
                    categoryId = "electrical",
                    experienceYears = 8,
                    rating = 4.7,
                    reviewCount = 59,
                    completedJobs = 265,
                    startingPriceInr = 249,
                    serviceAreasCsv = "Kothrud, Bavdhan, Baner, Viman Nagar",
                    bio = "Clean wiring, inverter backup integration, MCB distribution upgrades, and ceiling fan or pendant lighting installation.",
                    skillsCsv = "Inverter Setup, MCB & Earthing, Fan & Light Mounting, Modular Switches",
                    portfolioHighlightsCsv = "Luminous Inverter Home Backup in Kothrud|Living Room Pendant & Track Lights in Viman Nagar",
                    responseTimeText = "Usually quotes within 1 hour",
                    pricingModel = "Starting Price"
                ),
                ProfessionalEntity(
                    id = "pro_farhan",
                    userId = null,
                    name = "Farhan Shaikh",
                    businessName = "Timberline Woodcraft Pune",
                    categoryId = "carpentry",
                    experienceYears = 12,
                    rating = 4.9,
                    reviewCount = 93,
                    completedJobs = 290,
                    startingPriceInr = 399,
                    serviceAreasCsv = "Koregaon Park, Kalyani Nagar, Viman Nagar, Hadapsar",
                    bio = "Bespoke sliding wardrobe repair, soft-close kitchen hinges, curtain pelmets, and clean flat-pack furniture assembly.",
                    skillsCsv = "Wardrobe Channels, Soft-Close Hinges, Door Locks, Furniture Assembly",
                    portfolioHighlightsCsv = "Sliding Wardrobe Track Restore in Kalyani Nagar|Study Desk & Shelving Install in Viman Nagar",
                    responseTimeText = "Usually quotes within 2 hours",
                    pricingModel = "Quote-Based"
                ),
                ProfessionalEntity(
                    id = "pro_kiran",
                    userId = null,
                    name = "Kiran Bhosale",
                    businessName = "Bhosale CoolCare HVAC",
                    categoryId = "ac_services",
                    experienceYears = 7,
                    rating = 4.8,
                    reviewCount = 78,
                    completedJobs = 385,
                    startingPriceInr = 549,
                    serviceAreasCsv = "Wakad, Hinjewadi, Baner, Aundh, Kothrud",
                    bio = "Split & inverter AC foam-jet servicing with protective water-catch jackets, leak pressure tests, and neat shifting.",
                    skillsCsv = "Foam-Jet Service, Gas Leak Test, Split AC Shifting, Drain Line Unblock",
                    portfolioHighlightsCsv = "Dual Split AC Summer Prep in Hinjewadi|Inverter AC Relocation in Baner",
                    responseTimeText = "Usually quotes within 1 hour",
                    pricingModel = "Starting Price"
                ),
                ProfessionalEntity(
                    id = "pro_meera",
                    userId = null,
                    name = "Meera & Team Appliances",
                    businessName = "Sahakar Appliance Clinic",
                    categoryId = "appliance",
                    experienceYears = 10,
                    rating = 4.7,
                    reviewCount = 52,
                    completedJobs = 198,
                    startingPriceInr = 349,
                    serviceAreasCsv = "Kothrud, Aundh, Baner, Bavdhan",
                    bio = "Transparent component-level inspection for front-load washing machines, double-door fridges, RO purifiers, and geysers.",
                    skillsCsv = "Front-Load Washers, Refrigerator Cooling, RO Filter Service, Geyser Elements",
                    portfolioHighlightsCsv = "Bosch Drum Bearing Replacement in Aundh|RO Membrane & TDS Calibration in Kothrud",
                    responseTimeText = "Usually quotes within 2 hours",
                    pricingModel = "Quote-Based"
                ),
                ProfessionalEntity(
                    id = "pro_sanjay",
                    userId = null,
                    name = "Sanjay Kulkarni",
                    businessName = "GreenShield Eco Pest Care",
                    categoryId = "pest_control",
                    experienceYears = 9,
                    rating = 4.8,
                    reviewCount = 61,
                    completedJobs = 275,
                    startingPriceInr = 899,
                    serviceAreasCsv = "Koregaon Park, Viman Nagar, Hadapsar, Baner, Kothrud",
                    bio = "Odorless herbal gel treatments and safe termite barrier care designed for homes with children and pets.",
                    skillsCsv = "Odorless Gel Baiting, Termite Barrier, Bed Bug Care, Kitchen Crevice Seal",
                    portfolioHighlightsCsv = "2 BHK Move-In Pest Shield in Wakad|Wooden Wardrobe Termite Care in Koregaon Park",
                    responseTimeText = "Usually quotes within 3 hours",
                    pricingModel = "Starting Price"
                ),
                ProfessionalEntity(
                    id = "pro_nitin",
                    userId = null,
                    name = "Nitin Jadhav",
                    businessName = "Sahyadri SafeMovers Pune",
                    categoryId = "moving",
                    experienceYears = 11,
                    rating = 4.8,
                    reviewCount = 74,
                    completedJobs = 240,
                    startingPriceInr = 3999,
                    serviceAreasCsv = "Kothrud, Wakad, Hinjewadi, Baner, Viman Nagar, Hadapsar",
                    bio = "Punctual intra-Pune apartment shifting with labeled corrugated boxes, appliance bubble wrapping, and bed re-assembly.",
                    skillsCsv = "2 BHK Intra-Pune Shift, Fragile Packing, Wardrobe Dismantling, Appliance Wrap",
                    portfolioHighlightsCsv = "Kothrud to Baner 2 BHK Smooth Move|Hinjewadi Studio Setup Shift",
                    responseTimeText = "Usually quotes within 2 hours",
                    pricingModel = "Quote-Based"
                ),
                ProfessionalEntity(
                    id = "pro_prasad",
                    userId = null,
                    name = "Prasad Patwardhan",
                    businessName = "Patwardhan Wall & Brush",
                    categoryId = "painting",
                    experienceYears = 7,
                    rating = 4.7,
                    reviewCount = 41,
                    completedJobs = 132,
                    startingPriceInr = 3199,
                    serviceAreasCsv = "Kothrud, Bavdhan, Aundh, Karve Nagar",
                    bio = "Clean single-room refreshes, rental handover painting, and moisture-resistant balcony coats on flexible weekend schedules.",
                    skillsCsv = "Rental Repainting, Single Room Refresh, Balcony Exterior Coat",
                    portfolioHighlightsCsv = "2 BHK Rental Handover Coat in Kothrud",
                    responseTimeText = "Usually quotes within 3 hours",
                    pricingModel = "Quote-Based"
                ),
                ProfessionalEntity(
                    id = "pro_deepak",
                    userId = null,
                    name = "Deepak Mane",
                    businessName = "Mane Interiors & Paint",
                    categoryId = "painting",
                    experienceYears = 14,
                    rating = 4.9,
                    reviewCount = 105,
                    completedJobs = 320,
                    startingPriceInr = 3899,
                    serviceAreasCsv = "Baner, Koregaon Park, Kalyani Nagar, Aundh",
                    bio = "Master finishing team delivering Royale matte walls, wooden door polish, and meticulous post-job home handover.",
                    skillsCsv = "Royale Matte Finish, Wood Door Polish, Full Home Renovation Paint",
                    portfolioHighlightsCsv = "3 BHK Designer Ivory Finish in Koregaon Park",
                    responseTimeText = "Usually quotes within 2 hours",
                    pricingModel = "Starting Price"
                )
            )
            dao.insertProfessionals(pros)

            dao.insertReviews(
                listOf(
                    ReviewEntity("rev_1", "pro_rohan", "Meenakshi R.", 5, "Covered every piece of furniture neatly and finished our 2 BHK living room ahead of schedule.", "Painting & Renovation", "Oct 2026"),
                    ReviewEntity("rev_2", "pro_rohan", "Siddharth K.", 5, "Very transparent material breakdown—zero surprise add-ons at handover.", "Painting & Renovation", "Sep 2026"),
                    ReviewEntity("rev_3", "pro_sneha", "Ananya D.", 5, "Our new flat in Baner looked spotless before the movers arrived. Kitchen tiles shine!", "Cleaning", "Nov 2026"),
                    ReviewEntity("rev_4", "pro_vikram", "Rahul M.", 5, "Identified the bathroom seepage source in 15 minutes without breaking extra tiles.", "Plumbing", "Oct 2026"),
                    ReviewEntity("rev_5", "pro_amit", "Priya N.", 5, "Neat inverter wiring and clear labeling on the MCB box.", "Electrical Repairs", "Oct 2026"),
                    ReviewEntity("rev_6", "pro_farhan", "Karthik S.", 5, "Fixed three jammed wardrobe sliders and hung our living room curtains cleanly.", "Carpentry", "Nov 2026"),
                    ReviewEntity("rev_7", "pro_kiran", "Neha T.", 5, "Used a proper drain jacket so no water splashed on our bedroom wall.", "AC Services", "Oct 2026")
                )
            )

            // Home profile for default customer
            dao.upsertHomeProfile(
                HomeProfileEntity(
                    userId = custId,
                    homeType = "2 BHK Apartment",
                    approxSqFt = 1050,
                    locality = "Kothrud",
                    preferredSlot = "Morning (9 AM – 12 PM)",
                    notes = "Society gate pass needed at main desk; pet-friendly low-odor materials preferred.",
                    updatedAt = now - 86400_000L * 3
                )
            )

            // Shortlist & Recently viewed
            dao.addToShortlist(ShortlistEntity(custId, "pro_rohan", now - 3600_000L))
            dao.addToShortlist(ShortlistEntity(custId, "pro_sneha", now - 7200_000L))
            dao.upsertRecentlyViewed(RecentlyViewedEntity(custId, "pro_rohan", now - 1800_000L))
            dao.upsertRecentlyViewed(RecentlyViewedEntity(custId, "pro_vikram", now - 5400_000L))
            dao.upsertRecentlyViewed(RecentlyViewedEntity(custId, "pro_sneha", now - 9000_000L))

            // Sample Open Request with 3 Quotations ready to compare side-by-side!
            val sampleReqId = "req_sample_paint"
            dao.insertServiceRequest(
                ServiceRequestEntity(
                    id = sampleReqId,
                    customerId = custId,
                    customerName = "Aarav Kulkarni",
                    categoryId = "painting",
                    categoryName = "Painting & Renovation",
                    problemTitle = "Living Room & Hallway Warm Ivory Repaint",
                    problemDescription = "Just shifted into a 2 BHK near Paud Road. Looking for 2 coats of low-VOC warm ivory matte paint in the living room and hallway, plus minor nail-hole putty touch-up.",
                    photoUrisCsv = "",
                    address = "Flat 402, Vanaz Residency, Paud Road",
                    locality = "Kothrud",
                    preferredDate = "28 Nov 2026",
                    status = "QUOTES_RECEIVED",
                    isRelocationBundle = false,
                    createdAt = now - 3600_000L * 5
                )
            )

            // Second open request (visible in Professional Dashboard incoming requests!)
            dao.insertServiceRequest(
                ServiceRequestEntity(
                    id = "req_sample_plumb",
                    customerId = custId,
                    customerName = "Aarav Kulkarni",
                    categoryId = "plumbing",
                    categoryName = "Plumbing",
                    problemTitle = "Kitchen Sink Angle Valve & RO Inlet Fitment",
                    problemDescription = "Need replacement of a dripping under-sink angle cock and a dedicated tee-joint for our water purifier.",
                    photoUrisCsv = "",
                    address = "Flat 402, Vanaz Residency, Paud Road",
                    locality = "Kothrud",
                    preferredDate = "26 Nov 2026",
                    status = "OPEN_FOR_QUOTES",
                    isRelocationBundle = false,
                    createdAt = now - 3600_000L * 2
                )
            )

            // 3 Quotations on the painting request for immediate side-by-side comparison
            dao.insertQuotations(
                listOf(
                    QuotationEntity(
                        id = "quote_1",
                        requestId = sampleReqId,
                        professionalId = "pro_rohan",
                        professionalName = "Rohan Deshmukh",
                        professionalBusinessName = "Sahyadri Craft Painters",
                        professionalExperienceYears = 9,
                        professionalRating = 4.9,
                        professionalReviewCount = 84,
                        priceInr = 6800,
                        estimatedDuration = "1.5 days",
                        includedServicesCsv = "Furniture plastic masking|Nail-hole putty & sanding|2 coats Asian Paints Royale Matte|Post-work floor cleanup",
                        additionalChargesNote = "No extra charge unless exterior balcony waterproofing is added.",
                        status = "PENDING",
                        createdAt = now - 3600_000L * 4
                    ),
                    QuotationEntity(
                        id = "quote_2",
                        requestId = sampleReqId,
                        professionalId = "pro_prasad",
                        professionalName = "Prasad Patwardhan",
                        professionalBusinessName = "Patwardhan Wall & Brush",
                        professionalExperienceYears = 7,
                        professionalRating = 4.7,
                        professionalReviewCount = 41,
                        priceInr = 5950,
                        estimatedDuration = "2 days",
                        includedServicesCsv = "Basic drop-sheet cover|Spot putty & primer|2 coats low-VOC tractor/apcolite emulsion",
                        additionalChargesNote = "Royale Matte upgrade +₹950 if preferred over standard matte.",
                        status = "PENDING",
                        createdAt = now - 3600_000L * 3
                    ),
                    QuotationEntity(
                        id = "quote_3",
                        requestId = sampleReqId,
                        professionalId = "pro_deepak",
                        professionalName = "Deepak Mane",
                        professionalBusinessName = "Mane Interiors & Paint",
                        professionalExperienceYears = 14,
                        ratingOrDefault = 4.9,
                        professionalReviewCount = 105,
                        priceInr = 7600,
                        estimatedDuration = "1 day (2-person team)",
                        includedServicesCsv = "Full room & curtain masking|Laser damp check & putty skim|2 coats Royale Health Shield|Deep room handover clean",
                        additionalChargesNote = "All masking tape, rollers, and cleanup included.",
                        status = "PENDING",
                        createdAt = now - 3600_000L * 2
                    )
                )
            )

            // Active & Past Bookings
            dao.insertBooking(
                BookingEntity(
                    id = "book_active_1",
                    requestId = "req_past_clean",
                    quotationId = "quote_past_clean",
                    customerId = custId,
                    customerName = "Aarav Kulkarni",
                    professionalId = "pro_sneha",
                    professionalName = "Sneha Joshi",
                    categoryId = "cleaning",
                    categoryName = "Cleaning",
                    serviceTitle = "Pre-Move 2 BHK Deep Cleaning",
                    address = "Flat 402, Vanaz Residency, Paud Road",
                    locality = "Kothrud",
                    scheduledDate = "25 Nov 2026 • 9:30 AM",
                    agreedPriceInr = 2450,
                    status = "CONFIRMED",
                    proStatusNote = "2-member team assigned with eco-friendly kitchen degreaser.",
                    createdAt = now - 86400_000L
                )
            )

            dao.insertBooking(
                BookingEntity(
                    id = "book_history_1",
                    requestId = "req_past_ac",
                    quotationId = "quote_past_ac",
                    customerId = custId,
                    customerName = "Aarav Kulkarni",
                    professionalId = "pro_kiran",
                    professionalName = "Kiran Bhosale",
                    categoryId = "ac_services",
                    categoryName = "AC Services",
                    serviceTitle = "Split AC Foam-Jet Servicing",
                    address = "Flat 402, Vanaz Residency, Paud Road",
                    locality = "Kothrud",
                    scheduledDate = "10 Nov 2026 • 11:00 AM",
                    agreedPriceInr = 599,
                    status = "COMPLETED",
                    proStatusNote = "Coil cleaned, drain flushed, cooling verified at 16°C outlet.",
                    customerReviewRating = 5,
                    customerReviewText = "Prompt, tidy, and no mess on the bedroom wall.",
                    createdAt = now - 86400_000L * 12
                )
            )

            // Extra booking for Rohan's Professional Dashboard so earnings/history feel alive
            dao.insertBooking(
                BookingEntity(
                    id = "book_pro_rohan_1",
                    requestId = "req_ext_1",
                    quotationId = "quote_ext_1",
                    customerId = "cust_ext_1",
                    customerName = "Pallavi Deshpande",
                    professionalId = "pro_rohan",
                    professionalName = "Rohan Deshmukh",
                    categoryId = "painting",
                    categoryName = "Painting & Renovation",
                    serviceTitle = "Master Bedroom Accent Texture & Coat",
                    address = "B-12, Clover Park, Baner",
                    locality = "Baner",
                    scheduledDate = "18 Nov 2026 • 10:00 AM",
                    agreedPriceInr = 5400,
                    status = "COMPLETED",
                    proStatusNote = "Completed and inspected with homeowner.",
                    customerReviewRating = 5,
                    customerReviewText = "Smooth finish and great color advice.",
                    createdAt = now - 86400_000L * 5
                )
            )

            dao.insertNotification(
                NotificationEntity(
                    id = "notif_1",
                    userId = custId,
                    title = "3 New Quotations Received",
                    message = "Rohan, Prasad, and Deepak sent quotes for your Living Room & Hallway Repaint request.",
                    isRead = false,
                    relatedRoute = "compare_quotes/$sampleReqId",
                    createdAt = now - 3600_000L * 2
                )
            )
            dao.insertNotification(
                NotificationEntity(
                    id = "notif_2",
                    userId = custId,
                    title = "Booking Confirmed for 25 Nov",
                    message = "Sneha Joshi (Deccan FreshSpace Care) confirmed your 9:30 AM deep cleaning slot.",
                    isRead = false,
                    relatedRoute = "bookings",
                    createdAt = now - 86400_000L
                )
            )
            dao.insertNotification(
                NotificationEntity(
                    id = "notif_pro_1",
                    userId = proUserId,
                    title = "Incoming Request in Kothrud",
                    message = "Aarav Kulkarni requested a quote for Living Room & Hallway Repaint.",
                    isRead = false,
                    relatedRoute = "pro_dashboard",
                    createdAt = now - 3600_000L * 3
                )
            )
        }

        private fun QuotationEntity(
            id: String,
            requestId: String,
            professionalId: String,
            professionalName: String,
            professionalBusinessName: String,
            professionalExperienceYears: Int,
            ratingOrDefault: Double,
            professionalReviewCount: Int,
            priceInr: Int,
            estimatedDuration: String,
            includedServicesCsv: String,
            additionalChargesNote: String,
            status: String,
            createdAt: Long
        ) = QuotationEntity(
            id = id,
            requestId = requestId,
            professionalId = professionalId,
            professionalName = professionalName,
            professionalBusinessName = professionalBusinessName,
            professionalExperienceYears = professionalExperienceYears,
            professionalRating = ratingOrDefault,
            professionalReviewCount = professionalReviewCount,
            priceInr = priceInr,
            estimatedDuration = estimatedDuration,
            includedServicesCsv = includedServicesCsv,
            additionalChargesNote = additionalChargesNote,
            status = status,
            createdAt = createdAt
        )
    }
}
