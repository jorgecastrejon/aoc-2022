data class PointXY(val x: Int = 0, val y: Int = 0) {

    fun plus(x: Int = 0, y: Int = 0): PointXY =
        copy(x = this.x + x, y = this.y + y)
}