package com.enigma.kitchenorders.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import com.enigma.kitchenorders.ui.viewmodel.MenuAggregation
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PdfExporter(private val context: Context) {
    
    private val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    private val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
    
    fun exportAndShare(
        businessName: String,
        deliveryDate: Long,
        aggregations: List<MenuAggregation>
    ) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        
        drawContent(canvas, businessName, deliveryDate, aggregations)
        
        pdfDocument.finishPage(page)
        
        // Save file
        val fileName = "Pesanan_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.pdf"
        val file = File(context.cacheDir, fileName)
        
        FileOutputStream(file).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()
        
        // Share
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Pesanan $businessName - ${dateFormat.format(Date(deliveryDate))}")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan PDF").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
    
    private fun drawContent(
        canvas: Canvas,
        businessName: String,
        deliveryDate: Long,
        aggregations: List<MenuAggregation>
    ) {
        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }
        
        val headerPaint = Paint().apply {
            textSize = 18f
            isFakeBoldText = true
            color = android.graphics.Color.DKGRAY
        }
        
        val normalPaint = Paint().apply {
            textSize = 14f
            color = android.graphics.Color.BLACK
        }
        
        val smallPaint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.GRAY
        }
        
        val notePaint = Paint().apply {
            textSize = 11f
            color = android.graphics.Color.rgb(180, 100, 0)
        }
        
        var yPos = 50f
        val leftMargin = 40f
        val lineHeight = 20f
        
        // Header
        canvas.drawText(businessName, leftMargin, yPos, titlePaint)
        yPos += 30f
        
        canvas.drawText("Tanggal Pengiriman: ${dateFormat.format(Date(deliveryDate))}", leftMargin, yPos, normalPaint)
        yPos += lineHeight
        
        canvas.drawText("Dicetak: ${dateFormat.format(Date())} ${timeFormat.format(Date())}", leftMargin, yPos, smallPaint)
        yPos += 40f
        
        // Divider line
        canvas.drawLine(leftMargin, yPos, 555f, yPos, normalPaint)
        yPos += 30f
        
        // Total
        val totalQty = aggregations.sumOf { it.totalQuantity }
        canvas.drawText("TOTAL PRODUKSI: $totalQty porsi", leftMargin, yPos, headerPaint)
        yPos += 40f
        
        // Menu list
        for (aggregation in aggregations) {
            if (yPos > 780f) break // Stop if near page end
            
            // Menu name with quantity
            canvas.drawText(
                "${aggregation.menuName}  [${aggregation.totalQuantity}]",
                leftMargin,
                yPos,
                headerPaint
            )
            yPos += 25f
            
            // Details
            for (detail in aggregation.details) {
                if (yPos > 800f) break
                
                val detailText = "  - ${detail.customerName} (${detail.quantity})"
                canvas.drawText(detailText, leftMargin, yPos, normalPaint)
                yPos += lineHeight
                
                if (!detail.notes.isNullOrBlank()) {
                    canvas.drawText("      ${detail.notes}", leftMargin, yPos, notePaint)
                    yPos += lineHeight
                }
            }
            
            yPos += 15f
        }
    }
}
