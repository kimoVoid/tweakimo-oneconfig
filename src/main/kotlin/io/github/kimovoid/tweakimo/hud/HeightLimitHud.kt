package io.github.kimovoid.tweakimo.hud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import io.github.kimovoid.tweakimo.Tweakimo
import net.hypixel.data.type.GameType
import net.minecraft.client.Minecraft
import org.polyfrost.compose.composables.PolyBox
import org.polyfrost.compose.composables.PolyColumn
import org.polyfrost.compose.composables.PolyMcText
import org.polyfrost.compose.composables.PolyRow
import org.polyfrost.compose.composables.padding
import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.api.config.v1.annotations.Color
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.hud.v1.Hud
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

private const val MAP_NAME_GAP = 4f

class HeightLimitHud : Hud(
    id = "bedwars_height_limit.json",
    title = "Height Limit",
    category = Category.INFO,
) {

    @Switch(title = "Only Show In-game", description = "Only shows during Bed Wars games,\notherwise it will also show the vanilla height limit.")
    var onlyShowInGame = true

    @Text(title = "Prefix")
    var prefix = "Y: "

    @Color(title = "Bracket Color")
    var bracketColor = PolyColor.GRAY

    @Color(title = "Height Limit Color")
    var limitColor = PolyColor.YELLOW

    @Switch(title = "Show Map Name", subcategory = "Map Name", description = "Only displayed in Bed Wars games.")
    var mapName = false

    @Switch(title = "Bold Text", subcategory = "Map Name")
    var mapNameBold = true

    @Color(title = "Text Color", subcategory = "Map Name")
    var mapNameColor = PolyColor.GRAY

    @Switch(title = "Show Distance", subcategory = "Distance")
    var showDistance = true

    @Color(title = "Color", subcategory = "Distance")
    var distanceColor = PolyColor.YELLOW

    @Color(title = "Color (<= 15 blocks)", subcategory = "Distance")
    var distance15Color = PolyColor(0xFFFFAA01.toInt())

    @Color(title = "Color (<= 5 blocks)", subcategory = "Distance")
    var distance5Color = PolyColor.RED

    @Color(title = "Color (<= 0 blocks)", subcategory = "Distance")
    var distance0Color = PolyColor(0xFFAA0201.toInt())

    private data class Display(
        val map: String?,
        val prefix: String,
        val limit: Int,
        val distance: Int,
        val limitColor: PolyColor,
        val showDistance: Boolean,
        val bracketColor: PolyColor,
        val distanceColor: PolyColor,
        val mapName: Boolean,
        val mapNameColor: PolyColor,
        val mapNameBold: Boolean,
    )

    private var display = mutableStateOf<Display?>(null)

    private var displayOwner: Any = this

    init {
        /*
        addDependency("distanceColor", "showDistance")
        addDependency("distance15Color", "showDistance")
        addDependency("distance5Color", "showDistance")
        addDependency("distance0Color", "showDistance")
         */

        padLeft = 4f
        padRight = 4f
        padTop = 4f
        padBottom = 4f
    }

    override fun canMergeBackground(): Boolean = true

    override fun shouldShow(): Boolean {
        if (!onlyShowInGame) return true
        val location = HypixelUtils.getLocation()
        return location.gameType.orElse(null) == GameType.BEDWARS && location.mapName.isPresent
    }

    override fun update(): Boolean {
        if (displayOwner !== this) {
            display = mutableStateOf(null)
            displayOwner = this
        }

        val map = if (isReal) HypixelUtils.getLocation().mapName.orElse(null) else null
        val limit = Tweakimo.INSTANCE.getLimit(map)
        val playerY = Minecraft.getInstance().player?.y?.toInt() ?: limit
        val distance = limit - playerY

        val next = Display(
            map = map,
            prefix = prefix,
            limit = limit,
            distance = distance,
            limitColor = limitColor,
            bracketColor = bracketColor,
            showDistance = showDistance,
            distanceColor = distanceColorFor(distance),
            mapName = mapName,
            mapNameColor = mapNameColor,
            mapNameBold = mapNameBold,
        )

        if (next == display.value) return false
        display.value = next
        return true
    }

    private fun distanceColorFor(distance: Int): PolyColor = when {
        distance <= 0 -> distance0Color
        distance <= 5 -> distance5Color
        distance <= 15 -> distance15Color
        else -> distanceColor
    }

    @Composable
    override fun Content() {
        val d = display.value ?: return
        val scale = textScale.coerceAtLeast(0.01f)
        val textCol = PolyColor(textColor, textChroma, textChromaSpeed)

        val map = d.map
        val showMap = mapName && map != null

        PolyBox(modifier = hudBackground().padding(padLeft, padTop, padRight, padBottom)) {
            PolyColumn(gap = MAP_NAME_GAP * scale) {
                if (showMap) {
                    PolyMcText((if (d.mapNameBold) "§l" else "") + map, d.mapNameColor, shadow = showShadow, scale = scale)
                }

                PolyRow {
                    PolyMcText(d.prefix, color = textCol, shadow = showShadow, scale = scale)
                    PolyMcText(d.limit.toString(), color = d.limitColor, shadow = showShadow, scale = scale)
                    if (showDistance) {
                        PolyMcText(" (", color = d.bracketColor, shadow = showShadow, scale = scale)
                        PolyMcText(d.distance.toString(), color = d.distanceColor, shadow = showShadow, scale = scale)
                        PolyMcText(")", color = d.bracketColor, shadow = showShadow, scale = scale)
                    }
                }
            }
        }
    }
}
