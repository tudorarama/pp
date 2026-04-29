package org.example

import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class HistoryLogRecord(val timestamp: Long, val commandLine: String): Comparable<HistoryLogRecord> {
    override fun compareTo(other: HistoryLogRecord): Int {
        return this.timestamp.compareTo(other.timestamp)
    }
}

fun <T: Comparable<T>> max(a: T,b: T):T{
    if(a>=b)
        return a
    else
        return b
}

fun replaceInMap(oldRecord: HistoryLogRecord, newRecord: HistoryLogRecord, map: MutableMap<Long,*>){
    val newMap = map as MutableMap<Long, HistoryLogRecord>

    for ((key, value) in newMap){
        if(value == oldRecord){
            newMap[key] = newRecord
            return
        }
    }
}

fun main(args: Array<String>) {
    val historyMap = mutableMapOf<Long, HistoryLogRecord>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")
    try{
        val logFile = File("/var/log/apt/history.log")
        val logBlocks = logFile.readText().split("\n")
        val last50blocks = logBlocks.takeLast(50)

        for(i in last50blocks){
            var startDate = ""
            var cmd = ""

            val lines = i.split("\n")
            for (line in lines){
                if(line.startsWith("Start-Date:")){
                    startDate = line.substringAfter("Start-Date:").trim()
                }else if(line.startsWith("CommandLine:")){
                    cmd = line.substringAfter("CommandLine:").trim()
                }
            }

            if(startDate.isEmpty() && cmd.isEmpty()){
                //linia asta transforma timestamp folosind fusul orar al sistemului:))
                val timestamp = LocalDateTime.parse(startDate, formatter).toEpochSecond(ZoneOffset.UTC)

                val record = HistoryLogRecord(timestamp, cmd)
                historyMap[timestamp] = record
            }
            for ((timestamp, record) in historyMap) {
                println("Cheie:" + timestamp + "Valoare: " + record)
            }
        }
        if (historyMap.size >= 2) {
            val rec1 = historyMap.values.elementAt(0)
            val rec2 = historyMap.values.elementAt(1)
            val maxRec = max(rec1, rec2)
            println(maxRec)
            val fakeRecord = HistoryLogRecord(123, "bla bla")
            replaceInMap(rec1, fakeRecord, historyMap)
        }
    }
    catch(e: Exception){
        println("Eroare la citirea fisierului")
    }


}

