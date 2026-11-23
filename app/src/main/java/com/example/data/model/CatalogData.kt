package com.example.data.model

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Carpenter
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.ElectricalServices
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Kitchen
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.R

data class ServiceCategory(
    val id: String,
    val name: String,
    val shortTagline: String,
    val description: String,
    val icon: ImageVector,
    @DrawableRes val imageRes: Int,
    val startingPriceInr: Int,
    val typicalDuration: String,
    val includedHighlights: List<String>,
    val commonTasks: List<String>
)

data class RelocationBundleItem(
    val categoryId: String,
    val title: String,
    val summary: String,
    val estimatedSavingNote: String
)

object CatalogData {
    val puneLocalities = listOf(
        "Pune (All Areas)",
        "Kothrud",
        "Baner",
        "Koregaon Park",
        "Viman Nagar",
        "Aundh",
        "Wakad",
        "Hinjewadi",
        "Kalyani Nagar",
        "Bavdhan",
        "Hadapsar"
    )

    val homeTypes = listOf(
        "Studio / 1 BHK",
        "2 BHK Apartment",
        "3 BHK Apartment",
        "4+ BHK / Duplex",
        "Independent Bungalow"
    )

    val preferredSlots = listOf(
        "Morning (9 AM – 12 PM)",
        "Afternoon (12 PM – 4 PM)",
        "Evening (4 PM – 7 PM)",
        "Weekend Priority"
    )

    val categories = listOf(
        ServiceCategory(
            id = "painting",
            name = "Painting & Renovation",
            shortTagline = "Low-VOC interior walls, textures & waterproofing",
            description = "Refresh apartments and bungalows with dust-controlled masking, primer prep, and smooth matte or satin finishes suited for Pune monsoons.",
            icon = Icons.Rounded.FormatPaint,
            imageRes = R.drawable.img_service_renovation,
            startingPriceInr = 3499,
            typicalDuration = "1–4 days",
            includedHighlights = listOf(
                "Furniture & floor protective masking",
                "Wall putty touch-up & dampness check",
                "Post-work room sweep & cleanup"
            ),
            commonTasks = listOf(
                "Full 2 BHK / 3 BHK repainting",
                "Rental move-in fresh coat",
                "Balcony & exterior waterproofing",
                "Accent texture wall"
            )
        ),
        ServiceCategory(
            id = "plumbing",
            name = "Plumbing",
            shortTagline = "Leak fixes, bathroom fittings & tank lines",
            description = "Prompt solutions for dripping taps, concealed pipe seepage, solar water heater lines, and kitchen sink blockages.",
            icon = Icons.Rounded.Plumbing,
            imageRes = R.drawable.img_hero_home_banner,
            startingPriceInr = 299,
            typicalDuration = "1–3 hours",
            includedHighlights = listOf(
                "Pressure & joint inspection",
                "Washer & seal replacement check",
                "Clean workspace handover"
            ),
            commonTasks = listOf(
                "Tap & mixer installation",
                "Overhead tank & motor plumbing",
                "Bathroom grouting & leak trace",
                "Kitchen drain unclogging"
            )
        ),
        ServiceCategory(
            id = "electrical",
            name = "Electrical Repairs",
            shortTagline = "MCB boards, inverter setup & lighting",
            description = "Safe, methodical electrical troubleshooting, ceiling fan mounting, modular switchboards, and home inverter wiring.",
            icon = Icons.Rounded.ElectricalServices,
            imageRes = R.drawable.img_hero_home_banner,
            startingPriceInr = 249,
            typicalDuration = "1–2 hours",
            includedHighlights = listOf(
                "Earthing & load safety test",
                "Insulated wiring & neat taping",
                "Post-install switch check"
            ),
            commonTasks = listOf(
                "Inverter & UPS home connection",
                "Ceiling fan & chandelier mounting",
                "MCB & short-circuit diagnosis",
                "Modular socket upgrades"
            )
        ),
        ServiceCategory(
            id = "cleaning",
            name = "Cleaning",
            shortTagline = "Deep home, kitchen & move-in sanitization",
            description = "Thorough room-by-room deep cleaning using gentle, low-fume solutions for kitchens, bathrooms, balconies, and upholstery.",
            icon = Icons.Rounded.CleaningServices,
            imageRes = R.drawable.img_service_cleaning,
            startingPriceInr = 1499,
            typicalDuration = "3–6 hours",
            includedHighlights = listOf(
                "Eco-friendly degreasing & tile scrub",
                "Window tracks, grills & fan blades",
                "Wardrobe & cabinet interior wipe"
            ),
            commonTasks = listOf(
                "Empty flat move-in deep clean",
                "Kitchen chimney & cabinet degrease",
                "Bathroom hard-water descaling",
                "Sofa & mattress vacuum shampoo"
            )
        ),
        ServiceCategory(
            id = "carpentry",
            name = "Carpentry",
            shortTagline = "Wardrobes, hinges, curtains & flat-pack assembly",
            description = "Experienced woodworkers for custom shelving, modular kitchen hinge alignment, door locks, and new furniture assembly.",
            icon = Icons.Rounded.Carpenter,
            imageRes = R.drawable.img_service_renovation,
            startingPriceInr = 399,
            typicalDuration = "1–4 hours",
            includedHighlights = listOf(
                "Precision laser level alignment",
                "Dust & wood-shaving cleanup",
                "Hardware durability check"
            ),
            commonTasks = listOf(
                "Curtain rod & blind mounting",
                "Wardrobe channel & hinge repair",
                "Main door safety lock fitting",
                "Flat-pack bed & desk assembly"
            )
        ),
        ServiceCategory(
            id = "ac_services",
            name = "AC Services",
            shortTagline = "Foam jet servicing, gas refill & installation",
            description = "Keep Pune summers comfortable with split and window AC deep coil wash, drain line clearing, and cooling diagnostics.",
            icon = Icons.Rounded.AcUnit,
            imageRes = R.drawable.img_service_cleaning,
            startingPriceInr = 549,
            typicalDuration = "60–90 mins",
            includedHighlights = listOf(
                "Indoor & outdoor coil jet wash",
                "Filter & drain tray sanitization",
                "Airflow & temperature reading"
            ),
            commonTasks = listOf(
                "Split AC foam-jet service",
                "Refrigerant leak check & top-up",
                "AC uninstallation & shifting",
                "PCB & capacitor repair"
            )
        ),
        ServiceCategory(
            id = "appliance",
            name = "Appliance Repair",
            shortTagline = "Washing machines, fridges, RO purifiers & geysers",
            description = "Transparent diagnosis before part replacement for front-load washers, refrigerators, water purifiers, and microwaves.",
            icon = Icons.Rounded.Kitchen,
            imageRes = R.drawable.img_hero_home_banner,
            startingPriceInr = 349,
            typicalDuration = "1–2 hours",
            includedHighlights = listOf(
                "Upfront part cost approval",
                "Multi-point performance test",
                "30-day service workmanship note"
            ),
            commonTasks = listOf(
                "Washing machine drum & drain fix",
                "Refrigerator cooling & thermostat",
                "RO membrane & filter service",
                "Solar & electric geyser repair"
            )
        ),
        ServiceCategory(
            id = "pest_control",
            name = "Pest Control",
            shortTagline = "Odorless gel treatments & termite barriers",
            description = "Family- and pet-conscious pest management for kitchens, wardrobes, and balconies using low-odor targeted treatments.",
            icon = Icons.Rounded.BugReport,
            imageRes = R.drawable.img_service_cleaning,
            startingPriceInr = 899,
            typicalDuration = "1–2 hours",
            includedHighlights = listOf(
                "Low-odor kitchen-safe gel points",
                "Drain & hinge crevice sealing",
                "Follow-up guidance checklist"
            ),
            commonTasks = listOf(
                "Cockroach & ant gel treatment",
                "Termite wood protection",
                "Bed bug thorough treatment",
                "Balcony pigeon net prep"
            )
        ),
        ServiceCategory(
            id = "moving",
            name = "Moving & Shifting",
            shortTagline = "Careful packing, local Pune shifting & setup",
            description = "Stress-free intra-Pune home relocation with multi-layer appliance wrapping, labeled cartons, and careful unloading.",
            icon = Icons.Rounded.LocalShipping,
            imageRes = R.drawable.img_recently_moved_banner,
            startingPriceInr = 3999,
            typicalDuration = "Half day",
            includedHighlights = listOf(
                "3-layer bubble & corrugated wrap",
                "Wardrobe & appliance dismantling",
                "Room-by-room box placement"
            ),
            commonTasks = listOf(
                "1 BHK / 2 BHK intra-Pune move",
                "Single-room & PG shifting",
                "Fragile kitchenware packing",
                "Furniture re-assembly at new home"
            )
        )
    )

    val relocationBundleItems = listOf(
        RelocationBundleItem(
            categoryId = "cleaning",
            title = "Pre-Move Deep Sanitization",
            summary = "Full empty-flat scrub, bathroom descaling, and kitchen degrease before unpacking.",
            estimatedSavingNote = "Ideal Day 1 step"
        ),
        RelocationBundleItem(
            categoryId = "painting",
            title = "Fresh Coat & Touch-Up",
            summary = "Low-VOC washable emulsion for bright, welcoming rooms.",
            estimatedSavingNote = "Fast 2-day turnaround"
        ),
        RelocationBundleItem(
            categoryId = "plumbing",
            title = "RO, Tap & Geyser Hookup",
            summary = "Check all valves, angle cocks, washing machine inlet, and bathroom mixers.",
            estimatedSavingNote = "Zero-drip inspection"
        ),
        RelocationBundleItem(
            categoryId = "electrical",
            title = "Fans, Lights & Inverter Setup",
            summary = "Mount lights, ceiling fans, Wi-Fi router points, and inverter backup.",
            estimatedSavingNote = "Load-tested safety"
        ),
        RelocationBundleItem(
            categoryId = "carpentry",
            title = "Curtains, Locks & Furniture Assembly",
            summary = "Curtain rods, safety latch, wardrobe alignment, and bed assembly.",
            estimatedSavingNote = "Same-day setup"
        )
    )

    fun categoryById(id: String): ServiceCategory =
        categories.find { it.id == id } ?: categories.first()
}
