package utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt


class Shapes {
    object Triangle: Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            return Outline.Generic(
                path = trianglePath(size)
            )
        }
        
        private fun trianglePath(size: Size): Path {
            return Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            }
        }
    }
    
    object L: Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            return Outline.Generic(
                path = lPath(size)
            )
        }

        private fun lPath(size: Size): Path {
            return Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height-35)
                lineTo(size.width-35, size.height-35)
                lineTo(size.width-35, size.height)
                lineTo(0f, size.height)
            }
        }
    }
    
    object Pentagon: Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            return Polygon(5).createOutline(size, layoutDirection, density)
        }
    }
    
    object Octagon: Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            return Polygon(8).createOutline(size, layoutDirection, density)
        }
    }
    
    class Polygon(private val sides: Int, private val rotation: Float = 0f): Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            return Outline.Generic(
                Path().apply {
                    val radius = if (size.width > size.height) size.width / 2f else size.height / 2f
                    val angle = 2.0 * Math.PI / sides
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val r = rotation * (Math.PI / 180)
                    moveTo(
                        cx + (radius * cos(0.0 + r).toFloat()),
                        cy + (radius * sin(0.0 + r).toFloat())
                    )
                    for (i in 1 until sides) {
                        lineTo(
                            cx + (radius * cos(angle * i + r).toFloat()),
                            cy + (radius * sin(angle * i + r).toFloat())
                        )
                    }
                    close()
                })
        }
    }
}
