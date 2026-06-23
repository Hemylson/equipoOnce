package com.example.equipoonce.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

/**
 * HU 2.0 — Criterio 15.
 *
 * Dibuja las ondas blancas de la parte inferior de la ventana Login/Registro
 * por medio de [Canvas] (sin usar imágenes png/jpg, como exige el criterio).
 *
 * Se pintan dos capas de onda senoidal superpuestas, una con opacidad menor,
 * para dar la sensación de profundidad que se observa en el pantallazo.
 */
class WaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val path = Path()

    /** Cantidad de crestas del armónico principal a lo ancho de la vista. */
    var waveCount: Float = 7f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * Armónico secundario, de menor frecuencia, que se suma al principal para
     * generar los desniveles irregulares entre crestas (no quedan todas iguales).
     * Mayor amplitud y menor frecuencia = desniveles más dispersos.
     */
    private val secondaryCount = 1.5f
    private val secondaryAmplitudeFactor = 0.9f
    private val secondaryPhase = (Math.PI * 0.7).toFloat()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w == 0f || h == 0f) return

        // Única onda blanca opaca (sin capa gris semitransparente).
        paint.alpha = 255
        drawWave(canvas, w, h, amplitude = h * 0.18f, baseline = h * 0.40f, phase = 0f)
    }

    private fun drawWave(
        canvas: Canvas,
        w: Float,
        h: Float,
        amplitude: Float,
        baseline: Float,
        phase: Float
    ) {
        val twoPi = 2f * Math.PI.toFloat()
        val secondaryAmplitude = amplitude * secondaryAmplitudeFactor

        // 1) Muestreo de la onda en puntos.
        val samples = 48
        val xs = FloatArray(samples + 1)
        val ys = FloatArray(samples + 1)
        for (i in 0..samples) {
            val t = i.toFloat() / samples
            val x = t * w
            // Armónico principal + armónico secundario desfasado = crestas con desniveles.
            val y1 = amplitude * sin(t * waveCount * twoPi + phase)
            val y2 = secondaryAmplitude * sin(t * secondaryCount * twoPi + secondaryPhase + phase)
            xs[i] = x
            ys[i] = baseline - y1 - y2
        }

        // 2) Trazo suave: curvas cuadráticas pasando por los puntos medios.
        path.reset()
        path.moveTo(0f, h)
        path.lineTo(xs[0], ys[0])
        for (i in 1 until samples) {
            val midX = (xs[i] + xs[i + 1]) / 2f
            val midY = (ys[i] + ys[i + 1]) / 2f
            path.quadTo(xs[i], ys[i], midX, midY)
        }
        path.quadTo(xs[samples], ys[samples], xs[samples], ys[samples])
        path.lineTo(w, h)
        path.close()
        canvas.drawPath(path, paint)
    }
}
