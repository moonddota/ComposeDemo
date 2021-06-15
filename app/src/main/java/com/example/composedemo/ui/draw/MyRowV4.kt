package com.example.composedemo.ui.draw

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import kotlin.math.roundToInt

@Composable
fun MyRowV4(
    modifier: Modifier = Modifier,
    content: @Composable MyRowScope.() -> Unit /*这样content函数作用域就是MyRowScope了*/
) {
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        // 存放测量后的 child 信息
        val placeables = arrayOfNulls<Placeable>(measurables.size)
        // 通过该parentData 就可以获取到我们设置的MyRowParentData数据了
        val parentDatas =
            Array(measurables.size) { measurables[it].parentData as? MyRowParentData }
        // 记录children 已经占据的空间
        var childrenSpace = 0
        // 所有权重之和
        var totalWeight = 0f
        // 设置了weight的child个数
        var weightChildrenCount = 0
        measurables.forEachIndexed { index, child ->
            val weight = parentDatas[index]?.weight
            if (weight != null) { // 有weight的child 记录一下
                totalWeight += weight
                weightChildrenCount++
            } else { // 没有weight的child直接测量
                val placeable = child.measure(
                    Constraints(
                        0,
                        constraints.maxWidth - childrenSpace, // 限制child在剩余空间范围内
                        0,
                        constraints.maxHeight
                    )
                )
                // 每次测量后更新 children 占据的空间
                childrenSpace += placeable.width
                placeables[index] = placeable
            }
        }
        // 把剩下的空间平均分布
        val weightUnitSpace =
            if (totalWeight > 0) (constraints.maxWidth - childrenSpace) / totalWeight else 0f
        measurables.forEachIndexed { index, child ->
            val weight = parentDatas[index]?.weight
            if (weight != null) {
                // 根据 child 的 weight 分配空间
                val distributionSpace = (weightUnitSpace * weight).roundToInt()
                val placeable = child.measure(
                    Constraints(
                        distributionSpace,
                        distributionSpace,
                        0,
                        constraints.maxHeight
                    )
                )
                placeables[index] = placeable
            }
        }

        var xPosition = 0
        layout(constraints.minWidth, constraints.minHeight) {
            placeables.forEach { placeable ->
                if (placeable == null) {
                    return@layout
                }
                // 放置每一个 child
                placeable.placeRelative(xPosition, 0)
                xPosition += placeable.width
            }
        }
    }
    Layout(content = { MyRowScope.content() }, modifier = modifier, measurePolicy = measurePolicy)
}

data class MyRowWeightModifier(val weight: Float/*要设置的权重*/) : ParentDataModifier {
    // 修改parentData的值
    override fun Density.modifyParentData(parentData: Any?): Any {
        // 如果参数类型正确且不为空，就修改一下直接返回，否则创建一个新的对象，修改后再返回
        var data = parentData as? MyRowParentData?
        if (data == null) {
            data = MyRowParentData()
        }
        data.weight = weight
        return data
    }
}

// 用于保存ParentData数据模型
data class MyRowParentData(var weight: Float = 0f)

interface MyRowScope {
    //then 是 Modifier 的一个方法，用于形成 Modifier 链
    fun Modifier.weight(weight: Float) = this.then(MyRowWeightModifier(weight))

    // 创建一个单例
    companion object : MyRowScope
}