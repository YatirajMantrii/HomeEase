package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.HomeEaseDatabase
import com.example.data.local.SessionPreferences
import com.example.data.model.CatalogData
import com.example.data.repository.HomeEaseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var db: HomeEaseDatabase
    private lateinit var repository: HomeEaseRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, HomeEaseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = HomeEaseRepository(db.homeEaseDao(), SessionPreferences(context))
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `app name and catalog categories match HomeEase spec`() {
        assertEquals("HomeEase", context.getString(R.string.app_name))
        assertEquals(9, CatalogData.categories.size)
        assertTrue(CatalogData.puneLocalities.contains("Kothrud"))
    }

    @Test
    fun `service request quotation and booking confirmation flow persists`() = runTest {
        repository.ensureSeeded()
        val pros = repository.allProfessionals.first()
        assertTrue(pros.isNotEmpty())

        val reqResult = repository.submitServiceRequest(
            customerId = SessionPreferences.DEFAULT_CUSTOMER_ID,
            customerName = "Aarav Kulkarni",
            categoryId = "plumbing",
            problemTitle = "Bathroom Mixer Repair",
            problemDescription = "Leaking shower diverter knob in master bathroom.",
            photoUris = emptyList(),
            address = "Flat 301, Baner Road",
            locality = "Baner",
            preferredDate = "29 Nov 2026"
        )
        assertTrue(reqResult.isSuccess)
        val req = reqResult.getOrThrow()

        val quoteResult = repository.submitQuotation(
            requestId = req.id,
            pro = pros.first(),
            priceInr = 750,
            estimatedDuration = "2 hours",
            includedServices = "Diverter cartridge check, Seal replacement",
            additionalChargesNote = "Parts billed at MRP if needed"
        )
        assertTrue(quoteResult.isSuccess)
        val quote = quoteResult.getOrThrow()

        val bookingResult = repository.confirmBookingFromQuotation(quote.id)
        assertTrue(bookingResult.isSuccess)
        val booking = bookingResult.getOrThrow()
        assertEquals(750, booking.agreedPriceInr)
        assertEquals("CONFIRMED", booking.status)

        val updatedReq = repository.observeRequest(req.id).first()
        assertEquals("BOOKED", updatedReq?.status)
    }
}
