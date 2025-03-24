package com.example.medilinkapp.ui.screens.healthmonitoring

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthMonitoringViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // StateFlow for the cumulative steps loaded from Firebase.
    private val _cumulativeSteps = MutableStateFlow(0)
    val cumulativeSteps: StateFlow<Int> = _cumulativeSteps

    // We'll store the session's baseline sensor count in the ViewModel so it persists.
    var sessionStartSteps: Int = 0

    // Get the current user ID (ensure you have persistent authentication)
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"

    // Reference to the cumulative health data node.
    private val cumulativeRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("cumulativeHealthData")

    init {
        // Listen to Firebase and update _cumulativeSteps.
        cumulativeRef.child("steps").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _cumulativeSteps.value = snapshot.getValue(Int::class.java) ?: 0
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("RealtimeDB", "Failed to read cumulative steps", error.toException())
            }
        })
    }

    /**
     * Call this to update Firebase with new cumulative data.
     * @param currentSensorSteps: the latest sensor reading
     * @param heartRate: latest heart rate
     * @param sleepHours: computed sleep hours
     */
    fun updateCumulativeData(currentSensorSteps: Int, heartRate: Int, sleepHours: Float) {
        // Calculate new steps: existing cumulative + steps taken during this session.
        val sessionIncrement = currentSensorSteps - sessionStartSteps
        val newCumulativeSteps = _cumulativeSteps.value + sessionIncrement

        val healthData = mapOf(
            "steps" to newCumulativeSteps,
            "heartRate" to heartRate,
            "sleepHours" to sleepHours,
            "timestamp" to System.currentTimeMillis()
        )

        viewModelScope.launch {
            cumulativeRef.setValue(healthData)
                .addOnSuccessListener {
                    Log.d("RealtimeDB", "Cumulative data updated!")
                    _cumulativeSteps.value = newCumulativeSteps
                    // Reset the baseline for future sessions.
                    sessionStartSteps = currentSensorSteps
                }
                .addOnFailureListener { error ->
                    Log.w("RealtimeDB", "Error updating cumulative data", error)
                }
        }
    }
}
