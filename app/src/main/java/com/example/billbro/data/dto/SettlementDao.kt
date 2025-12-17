package com.example.billbro.data.dto
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.billbro.data.entity.SettlementEntity
@Dao
interface SettlementDao {
    @Insert
    suspend fun insertSettlement(settlement: SettlementEntity)
    @Query("SELECT * FROM settlements")
    fun getSettlements(): LiveData<List<SettlementEntity>>
}