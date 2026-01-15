package com.example.brown_house_android.data.repository

import android.content.Context
import android.util.Log
import com.example.brown_house_android.face.FaceClusterer
import java.io.File

class ExportRepository(private val context: Context) {

    fun exportAsCSV(summary: FaceClusterer.ClusteringSummary): String {
        val file = File(context.getExternalFilesDir(null), "face_clustering_result.csv")
        file.bufferedWriter().use { writer ->
            writer.write("PersonID,FaceCount,ImageIndices\n")
            summary.clusters.forEach { cluster ->
                writer.write("${cluster.personId},${cluster.faceCount},\"${cluster.imageIndices.joinToString(",")}\"\n")
            }
        }
        Log.d("Export", "CSV 저장: ${file.absolutePath}")
        return file.absolutePath
    }

    fun exportAsJSON(summary: FaceClusterer.ClusteringSummary): String {
        val file = File(context.getExternalFilesDir(null), "face_clustering_result.json")
        val json = buildString {
            appendLine("{")
            appendLine("  \"totalFaces\": ${summary.totalFaces},")
            appendLine("  \"totalPeople\": ${summary.totalPeople},")
            appendLine("  \"clusters\": [")
            summary.clusters.forEachIndexed { index, cluster ->
                appendLine("    {")
                appendLine("      \"personId\": ${cluster.personId},")
                appendLine("      \"faceCount\": ${cluster.faceCount},")
                appendLine("      \"imageIndices\": [${cluster.imageIndices.joinToString(", ")}]")
                append("    }")
                if (index < summary.clusters.size - 1) appendLine(",")
                else appendLine()
            }
            appendLine("  ]")
            appendLine("}")
        }
        file.writeText(json)
        Log.d("Export", "JSON 저장: ${file.absolutePath}")
        return file.absolutePath
    }
}
