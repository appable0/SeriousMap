package com.seriousmap.data

sealed class EdgeTile(val orientation: Orientation) : Tile() {
    class Empty(orientation: Orientation) : EdgeTile(orientation)
    class Door(val type: TileType, orientation: Orientation) : EdgeTile(orientation)
    class Separator(val type: TileType, orientation: Orientation) : EdgeTile(orientation)

    override fun transition(corner: MapColor?, center: MapColor?, puzzle: String?): EdgeTile {
        val tileCorner = TileType.fromColor(corner)
        val tileCenter = TileType.fromColor(center)
        return when {
            tileCorner == TileType.Room -> Separator(TileType.Room, orientation)
            tileCenter != null -> {
                val isOpenedWitherDoor = (this as? Door)?.type?.let {
                    ((it != tileCenter && it == TileType.Wither) || it == TileType.Opened)
                } == true
                if (isOpenedWitherDoor) Door(TileType.Opened, orientation) else Door(tileCenter, orientation)
            }
            else -> Empty(orientation)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Empty -> "EdgeTile.Empty($orientation)"
            is Door -> "EdgeTile.Door($type, $orientation)"
            is Separator -> "EdgeTile.Separator($type, $orientation)"
        }
    }
}
/*

    override fun draw(angle: Float) {
        tileType?.let {
            val renderPos = getRenderPos()
            GlStateManager.translate(renderPos.x.toDouble(), renderPos.y.toDouble(), 0.0)
            val smallSize = Config.doorThickness
            val bigSize = 20.0 - Config.doorThickness

            val width = when {
                orientation == Orientation.VERTICAL -> smallSize
                edgeType == EdgeType.DOOR -> Config.doorWidth
                edgeType == EdgeType.SEPARATOR -> bigSize
                else -> null
            }!!.toDouble()

            val height = when {
                orientation == Orientation.HORIZONTAL -> smallSize
                edgeType == EdgeType.DOOR -> Config.doorWidth
                edgeType == EdgeType.SEPARATOR -> bigSize
                else -> null
            }!!.toDouble()

            val color = if (edgeType == EdgeType.DOOR) {
                TileType.toColor(it).scale(Config.doorDarken)
            } else TileType.toColor(it)

            RenderUtils.renderRect(-width / 2, -height / 2, width, height, color)
            GlStateManager.translate(-renderPos.x.toDouble(), -renderPos.y.toDouble(), 0.0)
        }
    }



    override fun toString(): String {
        return "EdgeTile(type=$tileType, orientation=${orientation}, edgeType=${edgeType})"
    }
    */

