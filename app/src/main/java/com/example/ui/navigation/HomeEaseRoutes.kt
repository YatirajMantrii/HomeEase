package com.example.ui.navigation

object HomeEaseRoutes {
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val SERVICE_DETAIL = "service_detail/{categoryId}"
    const val PRO_LISTING = "pro_listing/{categoryId}"
    const val PRO_PROFILE = "pro_profile/{proId}"
    const val REQUEST_QUOTE = "request_quote/{categoryId}?bundle={bundle}&proId={proId}"
    const val COMPARE_QUOTES = "compare_quotes/{requestId}"
    const val BOOKING_CONFIRM = "booking_confirm/{bookingId}"
    const val BOOKINGS = "bookings"
    const val CUSTOMER_DASHBOARD = "customer_dashboard"
    const val PRO_DASHBOARD = "pro_dashboard"
    const val HOME_PROFILE = "home_profile"
    const val RECENTLY_MOVED = "recently_moved"
    const val ABOUT = "about"
    const val CONTACT = "contact"
    const val AUTH = "auth/{tab}"

    fun serviceDetail(categoryId: String) = "service_detail/$categoryId"
    fun proListing(categoryId: String = "all") = "pro_listing/$categoryId"
    fun proProfile(proId: String) = "pro_profile/$proId"
    fun requestQuote(categoryId: String = "painting", bundle: Boolean = false, proId: String = "") =
        "request_quote/$categoryId?bundle=$bundle&proId=$proId"
    fun compareQuotes(requestId: String) = "compare_quotes/$requestId"
    fun bookingConfirm(bookingId: String) = "booking_confirm/$bookingId"
    fun auth(tab: String = "login") = "auth/$tab"
}
