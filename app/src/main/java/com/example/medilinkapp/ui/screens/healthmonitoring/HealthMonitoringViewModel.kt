package com.example.medilinkapp.ui.screens.healthmonitoring

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medilinkapp.model.HealthData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthMonitoringViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // StateFlow to hold HealthData
    private val _healthData = MutableStateFlow(HealthData())
    val healthData: StateFlow<HealthData> get() = _healthData

    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"

    // Reference to a health data node in Firebase Realtime Database.
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(userId)
        .child("healthData")

    init {
        // Listen for realtime changes to update our state.
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(HealthData::class.java) ?: HealthData()
                _healthData.value = data
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RealtimeDB", "Failed to read health data.", error.toException())
            }
        })
    }

    // ✅ Function to update step goal
    fun updateStepGoal(newGoal: Int) {
        val updatedData = _healthData.value.copy(stepGoal = newGoal)
        updateHealthData(updatedData)
    }

    // ✅ Function to update water intake
    fun updateWaterIntake(newIntake: Int) {
        val updatedData = _healthData.value.copy(waterIntake = newIntake)
        updateHealthData(updatedData)
    }

    // ✅ Function to update steps
    fun updateSteps(newSteps: Int) {
        val updatedData = _healthData.value.copy(steps = newSteps)
        updateHealthData(updatedData)
    }

    // ✅ Function to update the whole health data in Firebase
    private fun updateHealthData(newData: HealthData) {
        viewModelScope.launch {
            dbRef.setValue(newData)
                .addOnSuccessListener { Log.d("RealtimeDB", "Health data updated!") }
                .addOnFailureListener { error -> Log.w("RealtimeDB", "Error updating health data", error) }
        }
    }
}
