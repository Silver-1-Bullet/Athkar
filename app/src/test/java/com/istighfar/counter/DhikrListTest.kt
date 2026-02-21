package com.istighfar.counter

import com.istighfar.counter.data.CounterRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DhikrListTest {

    @Test
    fun dhikrList_hasEightEntries() {
        assertEquals(8, CounterRepository.DHIKR_LIST.size)
    }

    @Test
    fun dhikrList_allEntriesNonEmpty() {
        CounterRepository.DHIKR_LIST.forEach { dhikr ->
            assertFalse("Dhikr entry should not be empty", dhikr.isEmpty())
        }
    }

    @Test
    fun dhikrList_containsIstighfar() {
        assertTrue(CounterRepository.DHIKR_LIST.contains("أستغفر الله"))
    }

    @Test
    fun dhikrList_containsSubhanallah() {
        assertTrue(CounterRepository.DHIKR_LIST.contains("سبحان الله"))
    }

    @Test
    fun dhikrList_containsAlhamdulillah() {
        assertTrue(CounterRepository.DHIKR_LIST.contains("الحمد لله"))
    }

    @Test
    fun dhikrList_containsAllahuAkbar() {
        assertTrue(CounterRepository.DHIKR_LIST.contains("الله أكبر"))
    }

    @Test
    fun dhikrList_noDuplicates() {
        val unique = CounterRepository.DHIKR_LIST.toSet()
        assertEquals(CounterRepository.DHIKR_LIST.size, unique.size)
    }
}
