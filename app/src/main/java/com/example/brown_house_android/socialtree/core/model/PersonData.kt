package com.example.brown_house_android.socialtree.core.model

/**
 * Person-specific data for nodes with NodeType.PERSON
 * Based on HTML design specifications from document folder
 */
data class PersonData(
    val profileImageUrl: String? = null,
    val relationship: PersonRelationship = PersonRelationship.OTHER,
    val birthDateMode: BirthDateMode = BirthDateMode.AGE,
    val age: Int? = null,
    val birthYear: Int? = null,
    val birthDate: BirthDate? = null,
    val phoneNumber: String? = null,
    val memo: String? = null
)

/**
 * Relationship types for persons
 * Matches the HTML design options: 부모/형제자매/배우자/자녀/친구/기타
 */
enum class PersonRelationship(val label: String) {
    PARENT("부모"),
    SIBLING("형제/자매"),
    SPOUSE("배우자"),
    CHILD("자녀"),
    FRIEND("친구"),
    OTHER("기타")
}

/**
 * Birth date input mode
 * Toggle between age input and birth year input
 */
enum class BirthDateMode {
    AGE,      // 나이 입력 모드
    YEAR      // 년도 입력 모드
}

/**
 * Birth date information with both solar and lunar calendar
 * Example: solar = "1965년 4월 5일", lunar = "음력 3월 5일"
 */
data class BirthDate(
    val solarDate: String,    // 양력 생년월일
    val lunarDate: String?    // 음력 생년월일 (선택사항)
)
