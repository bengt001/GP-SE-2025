<script lang="ts" setup>
const props = defineProps<{
  title?: string
  values: number[]
  labels: string[]
  barColors: string[]
  maxHeight?: number
  barWidth?: number
  barGap?: number
}>()

const maxHeight = props.maxHeight ?? 100
const barWidth = props.barWidth ?? 20
const barGap = props.barGap ?? 10
const maxValue = Math.max(...props.values, 1) // avoid 0
</script>

<template>
  <div class="sparkline-container">
    <h3 class="sparkline-title">
      {{ title }}
    </h3>

    <svg
      class="sparkline-svg"
      :width="(barWidth + barGap) * values.length"
      :height="maxHeight"
    >
      <g v-for="(val, idx) in values"
         :key="idx">
        <rect
          :x="idx * (barWidth + barGap)"
          :y="maxHeight - (val / maxValue) * maxHeight"
          :width="barWidth"
          :height="(val / maxValue) * maxHeight"
          :fill="barColors[idx] || '#000'"
        />
      </g>
    </svg>

    <div class="labels-row">
      <div
        v-for="(label, idx) in labels"
        :key="idx"
        class="label"
        :style="{ width: barWidth + 'px', marginRight: barGap + 'px' }"
      >
        {{ label }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.sparkline-title {
  font-weight: bold;
  margin-bottom: 8px;
  text-align: center;
  font-size: 1rem;
}
.sparkline-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.sparkline-svg {
  display: block;
}
.labels-row {
  display: flex;
  justify-content: flex-start;
  padding-top: 4px;
}
.label {
  text-align: center;
  font-size: 0.6rem;
  transform: rotate(45deg);
  transform-origin: bottom center;
  white-space: nowrap;
  overflow: visible;
  display: inline-block;
  padding-right: 4px;
}
</style>
